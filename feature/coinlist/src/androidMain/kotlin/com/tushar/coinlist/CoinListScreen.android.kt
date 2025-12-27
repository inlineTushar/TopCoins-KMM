package com.tushar.coinlist

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf

@Preview(showBackground = true)
@Composable
private fun CoinListComposableContentPreview() {
    CoinListComposable(
        viewState = CoinsUiState.Content(
            type = SortType.BEST_PERFORM,
            updatedAt = "12:34:56",
            isRefreshing = false,
            items = persistentListOf(
                CoinUIModel(
                    id = "1",
                    name = "Bitcoin",
                    symbol = "BTC",
                    currencyCode = "EUR",
                    price = "€6459.50",
                    changePercent24Hr = "+4.44%"
                ),
                CoinUIModel(
                    id = "2",
                    name = "Ethereum",
                    symbol = "ETH",
                    currencyCode = "EUR",
                    price = "€480.21",
                    changePercent24Hr = "-1.22%"
                )
            )
        ),
        listState = LazyListState(),
        onClickSort = {},
        onRefresh = {},
        onRetry = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CoinListComposableLoadingPreview() {
    CoinListComposable(
        viewState = CoinsUiState.Loading,
        listState = LazyListState(),
        onClickSort = {},
        onRefresh = {},
        onRetry = {},
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun CoinListComposableErrorPreview() {
    CoinListComposable(
        viewState = CoinsUiState.Error(error = "Something went wrong"),
        listState = LazyListState(),
        onClickSort = {},
        onRefresh = {},
        onRetry = {},
        modifier = Modifier
    )
}
