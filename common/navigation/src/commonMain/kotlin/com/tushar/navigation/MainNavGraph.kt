package com.tushar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

@Composable
fun MainNavGraph(
    backStack: NavBackStack<Route>,
    coinListScreenComposable: @Composable () -> Unit,
    priceLiveUpdateScreenComposable: @Composable () -> Unit
) {
    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                Route.CoinList -> NavEntry(key) { coinListScreenComposable() }
                Route.PriceLiveUpdate -> NavEntry(key) { priceLiveUpdateScreenComposable() }
            }
        },
    )
}
