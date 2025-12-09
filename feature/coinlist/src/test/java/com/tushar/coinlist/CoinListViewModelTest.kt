package com.tushar.coinlist

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import com.tushar.coinlist.formatter.CurrencyFormatter
import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.coinlist.formatter.TimeFormatter
import com.tushar.domain.DomainError
import com.tushar.domain.GetCoinUseCase
import com.tushar.domain.model.BigDecimal
import com.tushar.domain.model.CoinCurrency
import com.tushar.domain.model.CoinDomainModel
import com.tushar.domain.model.CoinsDomainModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Instant
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoinListViewModelTest {

    private val mockUseCase: GetCoinUseCase = mockk()
    private val mockCurrencyFormatter: CurrencyFormatter = mockk(relaxed = true)
    private val mockPercentFormatter: PercentageFormatter = mockk(relaxed = true)
    private val mockTimeFormatter: TimeFormatter = mockk(relaxed = true)

    private lateinit var testCoinsDomain: CoinsDomainModel
    private lateinit var viewModel: CoinListViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val testCurrency = CoinCurrency(
            id = "eur",
            code = "EUR",
            symbol = "€",
            type = "fiat",
            rateInUsd = BigDecimal("1.07")
        )

        testCoinsDomain = CoinsDomainModel(
            timestamp = Instant.fromEpochMilliseconds(1705329045000L),
            coins = listOf(
                CoinDomainModel(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    price = BigDecimal("50000.0"),
                    coinCurrency = testCurrency,
                    changePercent24Hr = 1.5
                ),
                CoinDomainModel(
                    id = "2",
                    name = "Ethereum",
                    symbol = "ETH",
                    price = BigDecimal("4000.0"),
                    coinCurrency = testCurrency,
                    changePercent24Hr = -2.0
                )
            )
        )

        every { mockCurrencyFormatter.format(any(), any()) } returns "€123.45"
        every { mockPercentFormatter.format(any(), any()) } returns "+1.5"
        every { mockTimeFormatter.format(any(), any()) } returns "12:00:00"
        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.success(testCoinsDomain)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        assertThat(viewModel.uiState.value).isInstanceOf(CoinsUiState.Loading::class)
    }

    @Test
    fun `on successful load, state is Content with correct data`() = runTest {
        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(CoinsUiState.Content::class)

        val contentState = state as CoinsUiState.Content
        assertThat(contentState.updatedAt).isEqualTo("12:00:00")
        assertThat(contentState.items.size).isEqualTo(2)
        assertThat(contentState.items[0].name).isEqualTo("Bitcoin")
        assertThat(contentState.items[0].symbol).isEqualTo("BTC")
        assertThat(contentState.type).isEqualTo(SortType.BEST_PERFORM)
    }

    @Test
    fun `on network error, state is Error with correct error string`() = runTest {
        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.failure(DomainError.NoConnectivity)

        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(CoinsUiState.Error::class)
        val errorState = state as CoinsUiState.Error
        assertThat(errorState.errorString).isEqualTo("Connection problem. Please try again.")
    }

    @Test
    fun `on generic error, state is Error with generic error string`() = runTest {
        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.failure(RuntimeException("Test error"))

        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(CoinsUiState.Error::class)
        val errorState = state as CoinsUiState.Error
        assertThat(errorState.errorString).isEqualTo("Something went wrong! Try again later")
    }

    @Test
    fun `onSort with same type does not trigger reload`() = runTest {
        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        viewModel.onSort(SortType.BEST_PERFORM)

        advanceUntilIdle()
        coVerify(exactly = 1) { mockUseCase.sortByBestPriceChange(any(), any()) }
    }

    @Test
    fun `onSort with different type triggers reload`() = runTest {
        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )

        advanceUntilIdle()

        val worstCoins = testCoinsDomain.copy(
            coins = testCoinsDomain.coins.reversed()
        )

        coEvery {
            mockUseCase.sortByWorstPriceChange(any(), any())
        } returns Result.success(worstCoins)

        viewModel.onSort(SortType.WORST_PERFORM)

        advanceUntilIdle()

        coVerify { mockUseCase.sortByWorstPriceChange(any(), any()) }
        val state = viewModel.uiState.value as CoinsUiState.Content
        assertThat(state.type).isEqualTo(SortType.WORST_PERFORM)
    }

    @Test
    fun `onReload sets isRefreshing and calls use case with forceRefresh true`() = runTest {
        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        val refreshedTimestamp = Instant.fromEpochMilliseconds(1705329045000L)
        val refreshedCoins = testCoinsDomain.copy(timestamp = refreshedTimestamp)
        every { mockTimeFormatter.format(refreshedTimestamp, any()) } returns "13:00:00"
        coEvery {
            mockUseCase.sortByBestPriceChange(true, any())
        } returns Result.success(refreshedCoins)

        viewModel.onReload()
        advanceUntilIdle()

        coVerify { mockUseCase.sortByBestPriceChange(any(), any()) }

        val state = viewModel.uiState.value as CoinsUiState.Content
        assertThat(state.isRefreshing).isFalse()
        assertThat(state.updatedAt).isEqualTo("13:00:00")
    }

    @Test
    fun `onRetry sets Loading state and retries with last sort type`() = runTest {
        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.failure(DomainError.NoConnectivity)

        viewModel = CoinListViewModel(
            mockUseCase,
            mockCurrencyFormatter,
            mockPercentFormatter,
            mockTimeFormatter
        )
        advanceUntilIdle()

        assertThat(viewModel.uiState.value).isInstanceOf(CoinsUiState.Error::class)

        coEvery {
            mockUseCase.sortByBestPriceChange(any(), any())
        } returns Result.success(testCoinsDomain)

        viewModel.onRetry()
        advanceUntilIdle()

        coVerify(atLeast = 2) { mockUseCase.sortByBestPriceChange(any(), any()) }
        assertThat(viewModel.uiState.value).isInstanceOf(CoinsUiState.Content::class)
    }
}
