package com.tushar.coinlist

import androidx.annotation.StringRes
import com.tushar.feature.coinlist.R
import kotlinx.collections.immutable.ImmutableList

data class CoinUIModel(
    val id: String,
    val name: String,
    val symbol: String,
    val currencyCode: String,
    val changePercent24Hr: String,
    val price: String,
)

enum class SortType(@field:StringRes val stringId: Int) {
    BEST_PERFORM(R.string.feature_coinlist_sort_best),
    WORST_PERFORM(R.string.feature_coinlist_sort_worst),
}

sealed class CoinsUiState {
    data object Loading : CoinsUiState()

    data class Error(@field:StringRes val errorResId: Int) : CoinsUiState()

    data class Content(
        val type: SortType,
        val updatedAt: String,
        val isRefreshing: Boolean = false,
        val items: ImmutableList<CoinUIModel>
    ) : CoinsUiState()
}
