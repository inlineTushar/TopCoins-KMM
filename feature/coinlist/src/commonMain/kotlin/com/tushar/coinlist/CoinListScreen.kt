package com.tushar.coinlist

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import com.tushar.feature.coinlist.generated.resources.Res
import com.tushar.feature.coinlist.generated.resources.feature_coinlist_title
import com.tushar.feature.coinlist.generated.resources.ic_live_24
import com.tushar.navigation.Route
import com.tushar.ui.component.AppBar
import com.tushar.ui.component.ErrorComposable
import com.tushar.ui.component.HeaderComposable
import com.tushar.ui.component.ProgressBarComposable
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinListScreen(
    backStack: NavBackStack<Route>,
    modifier: Modifier = Modifier,
    vm: CoinListViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        vm.navEvent.collect { event ->
            when (event) {
                NavEvent.ToPriceLiveUpdate -> backStack.add(Route.PriceLiveUpdate)
            }
        }
    }

    CoinListComposable(
        viewState = state,
        listState = listState,
        onOptionPriceLiveClick = { vm.onOptionPriceLiveClick() },
        onClickSort = { vm.onSort(it) },
        onRefresh = { vm.onReload() },
        onRetry = { vm.onRetry() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun CoinListComposable(
    viewState: CoinsUiState,
    listState: LazyListState,
    onOptionPriceLiveClick: () -> Unit,
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
                title = stringResource(Res.string.feature_coinlist_title),
                isBackVisible = false,
                actionItemComposable = {
                    IconButton(onClick = onOptionPriceLiveClick) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_live_24),
                            contentDescription = "Live Update",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
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
                    errorText = viewState.error,
                    onRetry = onRetry
                )

                is CoinsUiState.Content -> {
                    val pullToRefreshState = rememberPullToRefreshState()
                    PullToRefreshBox(
                        isRefreshing = viewState.isRefreshing,
                        onRefresh = { onRefresh() },
                        modifier = modifier,
                        state = pullToRefreshState
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
        onOptionPriceLiveClick = {},
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
        onOptionPriceLiveClick = {},
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
        onOptionPriceLiveClick = {},
        onRefresh = {},
        onRetry = {},
        modifier = Modifier
    )
}
