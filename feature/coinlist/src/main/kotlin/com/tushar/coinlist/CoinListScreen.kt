package com.tushar.coinlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tushar.feature.coinlist.R
import com.tushar.ui.R.string
import com.tushar.ui.component.AppBar
import com.tushar.ui.component.ErrorComposable
import com.tushar.ui.component.HeaderComposable
import com.tushar.ui.component.ProgressBarComposable
import kotlinx.collections.immutable.persistentListOf

@Composable
fun CoinListScreen(
    modifier: Modifier = Modifier,
    vm: CoinListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    CoinListComposable(
        viewState = state,
        listState = listState,
        onClickSort = { vm.onSort(it) },
        onRefresh = { vm.onReload() },
        onRetry = { vm.onRetry() },
        modifier = modifier
    )
}

@Composable
private fun CoinListComposable(
    viewState: CoinsUiState,
    listState: LazyListState,
    onClickSort: (SortType) -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            ),
        topBar = {
            AppBar(
                title = stringResource(id = R.string.feature_coinlist_title),
                isBackVisible = false
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (viewState) {
                CoinsUiState.Loading -> ProgressBarComposable()
                is CoinsUiState.Error -> ErrorComposable(
                    errorText = stringResource(viewState.errorResId),
                    onRetry = onRetry
                )

                is CoinsUiState.Content -> {
                    PullToRefreshBox(
                        isRefreshing = viewState.isRefreshing,
                        onRefresh = { onRefresh() },
                        modifier = modifier
                    ) {
                        LazyColumn(state = listState) {
                            stickyHeader {
                                HeaderComposable(
                                    top = {
                                        SortingItemComposable(
                                            sortType = viewState.type,
                                            onClickSort = onClickSort
                                        )
                                    },
                                    bottom = {
                                        LastUpdateTimeStampComposable(updatedAtFormated = viewState.updatedAt)
                                    }
                                )

                            }
                            items(
                                items = viewState.items,
                                key = { it.id },
                            ) { item ->
                                CoinItemComposable(
                                    coinName = item.name,
                                    coinSymbol = item.symbol,
                                    coinPrice = item.price,
                                    coinChange = item.changePercent24Hr
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

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
        viewState = CoinsUiState.Error(errorResId = string.common_ui_error_generic),
        listState = LazyListState(),
        onClickSort = {},
        onRefresh = {},
        onRetry = {},
        modifier = Modifier
    )
}
