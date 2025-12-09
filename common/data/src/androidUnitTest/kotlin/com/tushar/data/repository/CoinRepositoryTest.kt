package com.tushar.data.repository

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.tushar.data.datasource.remote.api.CoinApiService
import com.tushar.data.datasource.remote.model.CoinApiModel
import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.domain.model.BigDecimal
import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.CoinsInUsdDomainModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.test.BeforeTest
import kotlin.test.Test

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
        api = mockk(relaxed = true)
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
        coEvery { api.getCoins() } returns testCoinsApiResponse

        val result1 = repository.getCoins(refresh = true).first()
        val result2 = repository.getCoins(refresh = false).first()

        assertThat(result1).isEqualTo(expectedDomainModel)
        assertThat(result2).isEqualTo(expectedDomainModel)
        assertThat(result1.coins).hasSize(1)
        assertThat(result1.timestamp).isEqualTo(testTimestamp)

        coVerify(exactly = 1) { api.getCoins() }
    }

    @Test
    fun `calls remote API when forceRefresh is true`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } returns testCoinsApiResponse

        repository.getCoins(refresh = true).first()
        repository.getCoins(refresh = true).first()

        coVerify(exactly = 2) { api.getCoins() }
    }

    @Test
    fun `returns cached value when initial cache exists and forceRefresh is false`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } returns testCoinsApiResponse
        val initial = repository.getCoins(refresh = true).first()
        val cached = repository.getCoins(refresh = false).first()

        assertThat(initial).isEqualTo(cached)
        assertThat(initial.timestamp).isEqualTo(testTimestamp)
        coVerify(exactly = 1) { api.getCoins() }
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

        coEvery { api.getCoins() } returns initialResponse andThen updatedResponse

        val result1 = repository.getCoins(refresh = true).first()
        val result2 = repository.getCoins(refresh = true).first()

        assertThat(result1.coins.first().id).isEqualTo("btc")
        assertThat(result1.timestamp).isEqualTo(testTimestamp)
        assertThat(result2.coins.first().id).isEqualTo("eth")
        assertThat(result2.timestamp).isEqualTo(updatedTimestamp)
        coVerify(exactly = 2) { api.getCoins() }
    }

    @Test
    fun `retries on network IOException and eventually succeeds`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } throws
                IOException("Network error 1") andThenThrows
                IOException("Network error 2") andThen
                testCoinsApiResponse

        val result = repository.getCoins(refresh = true).first()
        advanceUntilIdle()
        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        coVerify(exactly = 3) { api.getCoins() }
    }

    @Test
    fun `retries on SocketTimeoutException and eventually succeeds`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } throws
                SocketTimeoutException("Exception 1") andThenThrows
                SocketTimeoutException("Exception 2") andThen
                testCoinsApiResponse

        val result = repository.getCoins(refresh = true).first()

        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        coVerify(exactly = 3) { api.getCoins() }
    }

    @Test
    fun `retries on UnknownHostException and eventually succeeds`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } throws
                UnknownHostException("Unknown host") andThenThrows
                UnknownHostException("Unknown host 2") andThen
                testCoinsApiResponse

        val result = repository.getCoins(refresh = true).first()

        assertThat(result).isEqualTo(expectedDomainModel)
        assertThat(result.timestamp).isEqualTo(testTimestamp)
        coVerify(exactly = 3) { api.getCoins() }
    }

    @Test(expected = IOException::class)
    fun `throws exception after max retry attempts`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } throws IOException("Persistent failure")
        repository.getCoins(refresh = true).first()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `does not retry on non-network exceptions`() = runTest(testDispatcher) {
        coEvery { api.getCoins() } throws IllegalArgumentException("Bad request")
        repository.getCoins(refresh = true).first()

        coVerify(exactly = 1) { api.getCoins() }
    }
}
