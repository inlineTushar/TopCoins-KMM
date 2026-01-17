@file:OptIn(ExperimentalTime::class)

package com.tushar.data.repository

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.tushar.data.datasource.remote.api.http.CoinApiService
import com.tushar.data.datasource.remote.api.http.model.CoinApiModel
import com.tushar.data.datasource.remote.api.http.model.CoinsApiResponse
import com.tushar.core.model.BigDecimal
import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.CoinsInUsdDomainModel
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import dev.mokkery.verify.VerifyMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
import com.tushar.domain.SocketTimeoutException
import com.tushar.domain.UnknownHostException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.ExperimentalTime
import kotlinx.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CoinRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: CoinApiService
    private lateinit var repository: CoinRepositoryImpl
    private lateinit var testCoinsApiResponse: CoinsApiResponse
    private lateinit var expectedDomainModel: CoinsInUsdDomainModel
    private val testTimestamp = Instant.fromEpochMilliseconds(1705329045000L)

    @BeforeTest
    fun setup() {
        api = mock(mode = MockMode.autofill)
        repository = CoinRepositoryImpl(api, testDispatcher)

        testCoinsApiResponse = CoinsApiResponse(
            timestamp = testTimestamp,
            coins = listOf(
                CoinApiModel(
                    id = "btc",
                    name = "Bitcoin",
                    symbol = "BTC",
                    priceInUsd = BigDecimal("12345.67"),
                    changePercent24Hr = 1.23
                )
            )
        )

        expectedDomainModel = CoinsInUsdDomainModel(
            timestamp = testTimestamp,
            coins = listOf(
                CoinInUsdDomainModel(
                    id = "btc",
                    name = "Bitcoin",
                    symbol = "BTC",
                    priceInUsd = BigDecimal("12345.67"),
                    changePercent24Hr = 1.23
                )
            )
        )
    }

    @Test
    fun `returns cached value when forceRefresh is false`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } returns testCoinsApiResponse

        val result1 = repository.getCoins(refresh = true).first()
        val result2 = repository.getCoins(refresh = false).first()

        assertThat(result1).isEqualTo(expectedDomainModel)
        assertThat(result2).isEqualTo(expectedDomainModel)
        assertThat(result1.coins).hasSize(1)
        assertThat(result1.timestamp).isEqualTo(testTimestamp)

        verifySuspend(mode = VerifyMode.exactly(1)) { api.getCoins() }
    }

    @Test
    fun `calls remote API when forceRefresh is true`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } returns testCoinsApiResponse

        repository.getCoins(refresh = true).first()
        repository.getCoins(refresh = true).first()

        verifySuspend(mode = VerifyMode.exactly(2)) { api.getCoins() }
    }

    @Test
    fun `returns cached value when initial cache exists and forceRefresh is false`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } returns testCoinsApiResponse
        val initial = repository.getCoins(refresh = true).first()
        val cached = repository.getCoins(refresh = false).first()

        assertThat(initial).isEqualTo(cached)
        assertThat(initial.timestamp).isEqualTo(testTimestamp)
        verifySuspend(mode = VerifyMode.exactly(1)) { api.getCoins() }
    }

    @Test
    fun `fetches new data when forceRefresh is true even with cache`() = runTest(testDispatcher) {
        val initialResponse = testCoinsApiResponse
        val updatedTimestamp = Instant.fromEpochMilliseconds(1805329045000L)
        val updatedResponse = testCoinsApiResponse.copy(
            timestamp = updatedTimestamp,
            coins = listOf(
                CoinApiModel(
                    id = "eth",
                    name = "Ethereum",
                    symbol = "ETH",
                    priceInUsd = BigDecimal("9999.99"),
                    changePercent24Hr = 5.67
                )
            )
        )

        everySuspend { api.getCoins() } sequentially {
            returns(initialResponse)
            returns(updatedResponse)
        }

        val result1 = repository.getCoins(refresh = true).first()
        val result2 = repository.getCoins(refresh = true).first()

        assertThat(result1.coins.first().id).isEqualTo("btc")
        assertThat(result1.timestamp).isEqualTo(testTimestamp)
        assertThat(result2.coins.first().id).isEqualTo("eth")
        assertThat(result2.timestamp).isEqualTo(updatedTimestamp)
        verifySuspend(mode = VerifyMode.exactly(2)) { api.getCoins() }
    }

    @Test
    fun `retries on network IOException and eventually succeeds`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } sequentially {
            throws(IOException("Network error 1"))
            throws(IOException("Network error 2"))
            returns(testCoinsApiResponse)
        }

        val result = repository.getCoins(refresh = true).first()
        advanceUntilIdle()
        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        verifySuspend(mode = VerifyMode.exactly(3)) { api.getCoins() }
    }

    @Test
    fun `retries on SocketTimeoutException and eventually succeeds`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } sequentially {
            throws(SocketTimeoutException("Exception 1"))
            throws(SocketTimeoutException("Exception 2"))
            returns(testCoinsApiResponse)
        }

        val result = repository.getCoins(refresh = true).first()

        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        verifySuspend(mode = VerifyMode.exactly(3)) { api.getCoins() }
    }

    @Test
    fun `retries on UnknownHostException and eventually succeeds`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } sequentially {
            throws(UnknownHostException("Unknown host"))
            throws(UnknownHostException("Unknown host 2"))
            returns(testCoinsApiResponse)
        }

        val result = repository.getCoins(refresh = true).first()

        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        verifySuspend(mode = VerifyMode.exactly(3)) { api.getCoins() }
    }

    @Test
    fun `throws exception after max retry attempts`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } throws IOException("Persistent failure")
        repository.getCoins(refresh = true).first()
    }

    @Test
    fun `does not retry on non-network exceptions`() = runTest(testDispatcher) {
        everySuspend { api.getCoins() } throws IllegalArgumentException("Bad request")
        repository.getCoins(refresh = true).first()

        verifySuspend(mode = VerifyMode.exactly(1)) { api.getCoins() }
    }
}
