package com.tushar.priceupdate

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import com.tushar.core.formatter.CurrencyFormatter
import com.tushar.core.model.BigDecimal
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest.Symbol
import com.tushar.data.repository.RealtimePriceUpdateRepository
import com.tushar.data.repository.model.PriceUpdateRepoModel
import com.tushar.data.repository.model.PriceUpdateTickRepoModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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
class PriceLiveUpdateViewModelTest {

    private val mockRepository: RealtimePriceUpdateRepository = mockk(relaxed = true)
    private val mockCurrencyFormatter: CurrencyFormatter = mockk(relaxed = true)

    private lateinit var priceUpdateFlow: MutableSharedFlow<PriceUpdateRepoModel>
    private lateinit var viewModel: PriceLiveUpdateViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        priceUpdateFlow = MutableSharedFlow()
        every { mockRepository.priceUpdate } returns priceUpdateFlow
        every { mockCurrencyFormatter.format(any(), any()) } answers {
            "$${firstArg<BigDecimal>().toPlainString()}"
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): PriceLiveUpdateViewModel {
        return PriceLiveUpdateViewModel(mockRepository, mockCurrencyFormatter)
    }

    private fun createTick(
        price: String,
        symbol: String = "BTC/USD",
        currencyName: String = "Bitcoin"
    ) = PriceUpdateTickRepoModel(
        event = "price",
        symbol = symbol,
        exchange = "coinbase",
        type = "crypto",
        currencyName = currencyName,
        price = BigDecimal(price),
        currencyBase = "USD",
        timestamp = Instant.fromEpochMilliseconds(System.currentTimeMillis())
    )

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = createViewModel()

        assertThat(viewModel.uiState.value).isInstanceOf(PriceLiveUpdateUiState.Loading::class)
    }

    @Test
    fun `connects to repository with BTC-USD symbol on init`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        coVerify { mockRepository.connect(listOf(Symbol("BTC/USD"))) }
    }

    @Test
    fun `first price update transitions state to Tick with null isHiked`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("50000.00"))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state).isInstanceOf(PriceLiveUpdateUiState.Tick::class)

        val tickState = state as PriceLiveUpdateUiState.Tick
        assertThat(tickState.symbol).isEqualTo("BTC/USD")
        assertThat(tickState.currencyName).isEqualTo("Bitcoin")
        assertThat(tickState.isHiked).isNull()
    }

    @Test
    fun `price increase sets isHiked to true`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("50000.00"))
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("51000.00"))
        advanceUntilIdle()

        val state = viewModel.uiState.value as PriceLiveUpdateUiState.Tick
        assertThat(state.isHiked).isEqualTo(true)
    }

    @Test
    fun `price decrease sets isHiked to false`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("50000.00"))
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("49000.00"))
        advanceUntilIdle()

        val state = viewModel.uiState.value as PriceLiveUpdateUiState.Tick
        assertThat(state.isHiked).isEqualTo(false)
    }

    @Test
    fun `same price is filtered by distinctUntilChangedBy`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("50000.00"))
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("51000.00"))
        advanceUntilIdle()

        val stateAfterIncrease = viewModel.uiState.value as PriceLiveUpdateUiState.Tick
        assertThat(stateAfterIncrease.isHiked).isEqualTo(true)

        // Emit same price - should be filtered out, state unchanged
        priceUpdateFlow.emit(createTick("51000.00"))
        advanceUntilIdle()

        val stateAfterSame = viewModel.uiState.value as PriceLiveUpdateUiState.Tick
        assertThat(stateAfterSame.isHiked).isEqualTo(true)
    }

    @Test
    fun `currency formatter is called with correct parameters`() = runTest {
        val testPrice = BigDecimal("45678.90")
        every { mockCurrencyFormatter.format(testPrice, "USD") } returns "$45,678.90"

        viewModel = createViewModel()
        advanceUntilIdle()

        priceUpdateFlow.emit(createTick("45678.90"))
        advanceUntilIdle()

        val state = viewModel.uiState.value as PriceLiveUpdateUiState.Tick
        assertThat(state.price).isEqualTo("$45,678.90")
    }

    @Test
    fun `multiple price changes track direction correctly`() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        // First tick
        priceUpdateFlow.emit(createTick("50000.00"))
        advanceUntilIdle()
        assertThat((viewModel.uiState.value as PriceLiveUpdateUiState.Tick).isHiked).isNull()

        // Price goes up
        priceUpdateFlow.emit(createTick("52000.00"))
        advanceUntilIdle()
        assertThat((viewModel.uiState.value as PriceLiveUpdateUiState.Tick).isHiked).isEqualTo(true)

        // Price goes down
        priceUpdateFlow.emit(createTick("51000.00"))
        advanceUntilIdle()
        assertThat((viewModel.uiState.value as PriceLiveUpdateUiState.Tick).isHiked).isEqualTo(false)

        // Price goes up again
        priceUpdateFlow.emit(createTick("53000.00"))
        advanceUntilIdle()
        assertThat((viewModel.uiState.value as PriceLiveUpdateUiState.Tick).isHiked).isEqualTo(true)
    }
}
