package com.tushar.coinlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tushar.coinlist.CoinsUiState.Loading
import com.tushar.coinlist.formatter.CurrencyFormatter
import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.coinlist.formatter.TimeFormatter
import com.tushar.domain.DomainError
import com.tushar.domain.GetCoinUseCase
import com.tushar.domain.model.CoinsDomainModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CoinListViewModel(
    private val useCase: GetCoinUseCase,
    private val currencyFormatter: CurrencyFormatter,
    private val percentageFormatter: PercentageFormatter,
    private val timeFormatter: TimeFormatter
) : ViewModel() {

    private val _uiState = MutableStateFlow<CoinsUiState>(Loading)
    val uiState: StateFlow<CoinsUiState> = _uiState.asStateFlow()

    init {
        loadInitialCoins()
    }

    private fun loadInitialCoins() {
        viewModelScope.launch {
            loadCoins(DEFAULT_SORT_TYPE)
        }
    }

    fun onSort(type: SortType) {
        val currentState = _uiState.value
        require(currentState is CoinsUiState.Content) {
            "State must be of type Content but found $currentState"
        }
        if (currentState.type == type) return
        viewModelScope.launch {
            loadCoins(sort = type)
        }
    }

    fun onReload() {
        val currentState = _uiState.value
        if (currentState is CoinsUiState.Content && !currentState.isRefreshing) {
            _uiState.update { currentState.copy(isRefreshing = true) }
            viewModelScope.launch {
                loadCoins(sort = currentState.type, shouldReload = true)
            }
        }
    }

    fun onRetry() {
        _uiState.update { Loading }
        viewModelScope.launch {
            loadCoins(DEFAULT_SORT_TYPE, shouldReload = true)
        }
    }

    private suspend fun loadCoins(
        sort: SortType,
        shouldReload: Boolean = false,
    ) {
        when (sort) {
            SortType.BEST_PERFORM -> useCase.sortByBestPriceChange(
                refresh = shouldReload
            )

            SortType.WORST_PERFORM -> useCase.sortByWorstPriceChange(
                refresh = shouldReload
            )
        }.onSuccess { result: CoinsDomainModel ->
            _uiState.update {
                CoinsUiState.Content(
                    type = sort,
                    updatedAt = timeFormatter.format(result.timestamp),
                    isRefreshing = false,
                    items = result.coins
                        .asUiModel(currencyFormatter, percentageFormatter)
                        .toPersistentList()
                )
            }
        }.onFailure { th -> _uiState.update { CoinsUiState.Error(th.asErrorString()) } }
    }

    private fun Throwable.asErrorString(): String = when (this) {
        DomainError.NoConnectivity,
        DomainError.NetworkTimeout -> "Connection problem. Please try again."

        DomainError.UnknownError -> "Something went wrong! Try again later"
        else -> "Something went wrong! Try again later"
    }

    companion object {
        private val DEFAULT_SORT_TYPE = SortType.BEST_PERFORM
    }
}