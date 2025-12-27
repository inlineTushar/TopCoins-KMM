package com.tushar.priceupdate

sealed class PriceLiveUpdateUiState {
    data object Loading : PriceLiveUpdateUiState()
    class Tick(
        val symbol: String,
        val currencyName: String,
        val price: String,
        val isHiked: Boolean?
    ) : PriceLiveUpdateUiState()
}
