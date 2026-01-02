package com.tushar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavGraph(
    navController: NavHostController,
    startDestination: Route = Route.CoinList,
    coinListScreenComposable: @Composable () -> Unit,
    priceLiveUpdateScreenComposable: @Composable () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.value,
    ) {
        composable(route = Route.CoinList.value) {
            coinListScreenComposable()
        }
        composable(route = Route.PriceLiveUpdate.value) {
            priceLiveUpdateScreenComposable()
        }
    }
}
