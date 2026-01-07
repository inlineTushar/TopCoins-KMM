package com.tushar.priceupdate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tushar.core.formatter.CurrencyFormatterContract
import com.tushar.core.zipWithNext
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateRequest.Symbol
import com.tushar.data.repository.RealtimePriceUpdateRepository
import com.tushar.data.repository.model.PriceUpdateTickRepoModel
import com.tushar.priceupdate.PriceLiveUpdateUiState.Loading
import com.tushar.priceupdate.PriceLiveUpdateUiState.Tick
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PriceLiveUpdateViewModel(
    private val realtimePriceUpdateService: RealtimePriceUpdateRepository,
    private val currencyFormatter: CurrencyFormatterContract,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PriceLiveUpdateUiState>(Loading)
    val uiState: StateFlow<PriceLiveUpdateUiState> = _uiState.asStateFlow()

    init {
        startLivePriceUpdate()
    }

    private fun startLivePriceUpdate() {
        viewModelScope.launch {
            realtimePriceUpdateService.connect(symbols = listOf(Symbol("BTC/USD")))
            realtimePriceUpdateService.priceUpdate
                .filterIsInstance<PriceUpdateTickRepoModel>()
                .distinctUntilChangedBy { it.price }
                .zipWithNext { old, new ->
                    _uiState.update { new.asUiState(currencyFormatter, old?.isPriceHiked(new)) }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun PriceUpdateTickRepoModel?.isPriceHiked(new: PriceUpdateTickRepoModel): Boolean? {
        val result = this?.price?.compareTo(new.price) ?: 0
        return when {
            result < 0 -> true
            result > 0 -> false
            else -> null
        }
    }

    private fun PriceUpdateTickRepoModel.asUiState(
        currencyFormatter: CurrencyFormatterContract,
        isHiked: Boolean?
    ): PriceLiveUpdateUiState = Tick(
        symbol = symbol,
        currencyName = currencyName,
        price = currencyFormatter.format(price, code = "USD"),
        isHiked = isHiked
    )

    override fun onCleared() {
        viewModelScope.launch { realtimePriceUpdateService.disconnect() }
        super.onCleared()
    }
}