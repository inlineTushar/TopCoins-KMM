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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.tushar.feature.coinlist.generated.resources.Res
import com.tushar.feature.coinlist.generated.resources.feature_coinlist_title
import com.tushar.feature.coinlist.generated.resources.ic_live_24
import com.tushar.navigation.Route
import com.tushar.ui.component.AppBar
import com.tushar.ui.component.ErrorComposable
import com.tushar.ui.component.HeaderComposable
import com.tushar.ui.component.ProgressBarComposable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CoinListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    vm: CoinListViewModel = koinViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        vm.navEvent.collect { event ->
            when (event) {
                NavEvent.ToPriceLiveUpdate -> navController.navigate(Route.PriceLiveUpdate.value)
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
                    @OptIn(ExperimentalMaterial3Api::class)
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
