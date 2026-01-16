package com.tushar.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isSuccess
import com.tushar.core.model.BigDecimal
import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.CoinsInUsdDomainModel
import com.tushar.domain.model.ConversionRateDomainModel
import com.tushar.core.model.RoundingMode
import com.tushar.core.model.divide
import com.tushar.domain.repository.CoinRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCoinUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: CoinRepository
    private lateinit var useCase: GetCoinUseCase

    private lateinit var testCoinsInUsdDomainModel: CoinsInUsdDomainModel
    private lateinit var testRateDomainModel: ConversionRateDomainModel

    @BeforeTest
    fun setUp() {
        repository = mockk()
        useCase = GetCoinUseCase(repository, testDispatcher)

        testCoinsInUsdDomainModel = CoinsInUsdDomainModel(
            timestamp = Instant.fromEpochMilliseconds(1705329045000L),
            coins = listOf(
                CoinInUsdDomainModel(
                    id = "bitcoin",
                    name = "Bitcoin",
                    symbol = "BTC",
                    priceInUsd = BigDecimal("50000.00"),
                    changePercent24Hr = 5.5
                ),
                CoinInUsdDomainModel(
                    id = "ethereum",
                    name = "Ethereum",
                    symbol = "ETH",
                    priceInUsd = BigDecimal("4000.00"),
                    changePercent24Hr = -2.3
                ),
                CoinInUsdDomainModel(
                    id = "cardano",
                    name = "Cardano",
                    symbol = "ADA",
                    priceInUsd = BigDecimal("2.50"),
                    changePercent24Hr = 3.1
                )
            )
        )

        testRateDomainModel = ConversionRateDomainModel(
            id = "euro",
            symbol = "EUR",
            rateInUsd = BigDecimal("1.07"),
            currencySymbol = "€",
            type = "fiat"
        )
    }

    @Test
    fun `sortByBestPriceChange returns coins sorted by best performance`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isSuccess()
        val domainModel = result.getOrNull()!!

        assertThat(domainModel.coins[0].id).isEqualTo("bitcoin")
        assertThat(domainModel.coins[1].id).isEqualTo("cardano")
        assertThat(domainModel.coins[2].id).isEqualTo("ethereum")

        assertThat(domainModel.timestamp).isEqualTo(Instant.fromEpochMilliseconds(1705329045000L))
    }

    @Test
    fun `sortByWorstPriceChange returns coins sorted by worst performance`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByWorstPriceChange(refresh = false, topCount = 10)

        assertThat(result).isSuccess()
        val domainModel = result.getOrNull()!!

        assertThat(domainModel.coins[0].id).isEqualTo("ethereum")
        assertThat(domainModel.coins[1].id).isEqualTo("cardano")
        assertThat(domainModel.coins[2].id).isEqualTo("bitcoin")
    }

    @Test
    fun `topCount limits number of returned coins`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 2)

        assertThat(result).isSuccess()
        val domainModel = result.getOrNull()!!

        assertThat(domainModel.coins.size).isEqualTo(2)
        assertThat(domainModel.coins[0].id).isEqualTo("bitcoin")
        assertThat(domainModel.coins[1].id).isEqualTo("cardano")
    }

    @Test
    fun `price is correctly adjusted from USD to EUR`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isSuccess()
        val domainModel = result.getOrNull()!!

        val btcPrice = domainModel.coins[0].price
        assertThat(btcPrice).isEqualTo(
            BigDecimal("50000.00").divide(
                BigDecimal("1.07"),
                5,
                RoundingMode.HALF_UP
            )
        )

        assertThat(domainModel.coins[0].coinCurrency.id).isEqualTo("euro")
        assertThat(domainModel.coins[0].coinCurrency.code).isEqualTo("EUR")
        assertThat(domainModel.coins[0].coinCurrency.symbol).isEqualTo("€")
    }

    @Test
    fun `shouldReload true forces repository refresh`() = runTest(testDispatcher) {
        every { repository.getCoins(true) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(true, "euro") } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = true, topCount = 10)

        coVerify { repository.getCoins(refresh = true) }
        coVerify { repository.getRate(refresh = true, targetCurrency = "euro") }
        assertThat(result).isSuccess()
    }

    @Test
    fun `shouldReload false uses cached data`() = runTest(testDispatcher) {
        every { repository.getCoins(false) } returns flowOf(testCoinsInUsdDomainModel)
        every { repository.getRate(false, "euro") } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        coVerify { repository.getCoins(refresh = false) }
        coVerify { repository.getRate(refresh = false, targetCurrency = "euro") }
        assertThat(result).isSuccess()
    }

    @Test
    fun `network error returns failure with NoConnectivity`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } returns flowOf(testCoinsInUsdDomainModel)
        every {
            repository.getRate(
                any(),
                any()
            )
        } throws UnknownHostException("No network")

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()!!).isInstanceOf(DomainError.NoConnectivity::class)
    }

    @Test
    fun `timeout error returns failure with NetworkTimeout`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } throws SocketTimeoutException("Timeout")
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()!!).isInstanceOf(DomainError.NetworkTimeout::class)
    }

    @Test
    fun `generic exception returns failure with UnknownError`() = runTest(testDispatcher) {
        every { repository.getCoins(any()) } throws RuntimeException("Something went wrong")
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()!!).isInstanceOf(DomainError.UnknownError::class)
    }

    @Test
    fun `empty coin list returns empty result`() = runTest(testDispatcher) {
        val emptyCoins = CoinsInUsdDomainModel(
            timestamp = Instant.fromEpochMilliseconds(1705329045000L),
            coins = emptyList()
        )
        every { repository.getCoins(any()) } returns flowOf(emptyCoins)
        every { repository.getRate(any(), any()) } returns flowOf(testRateDomainModel)

        val result = useCase.sortByBestPriceChange(refresh = false, topCount = 10)

        assertThat(result).isSuccess()
        val domainModel = result.getOrNull()!!
        assertThat(domainModel.coins.size).isEqualTo(0)
    }
}
