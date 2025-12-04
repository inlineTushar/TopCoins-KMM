package com.tushar.coinlist

import com.tushar.feature.coinlist.generated.resources.Res
import com.tushar.feature.coinlist.generated.resources.feature_coinlist_sort_best
import com.tushar.feature.coinlist.generated.resources.feature_coinlist_sort_worst
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.StringResource

data class CoinUIModel(
    val id: String,
    val name: String,
    val symbol: String,
    val currencyCode: String,
    val changePercent24Hr: String,
    val price: String,
)

enum class SortType(val stringResource: StringResource) {
    BEST_PERFORM(Res.string.feature_coinlist_sort_best),
    WORST_PERFORM(Res.string.feature_coinlist_sort_worst),
}

sealed class CoinsUiState {
    data object Loading : CoinsUiState()

    data class Error(val errorString: String) : CoinsUiState()

    data class Content(
        val type: SortType,
        val updatedAt: String,
        val isRefreshing: Boolean = false,
        val items: ImmutableList<CoinUIModel>
    ) : CoinsUiState()
}
