package com.tushar.topcoins

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tushar.coinlist.CoinListScreen
import com.tushar.navigation.Route
import com.tushar.priceupdate.PriceLiveUpdateScreen
import com.tushar.ui.theme.AppTheme

@Composable
internal fun MainNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.PriceLiveUpdate,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.value,
    ) {
        composable(route = Route.CoinList.value) {
            CoinListScreen()
        }
        composable(route = Route.PriceLiveUpdate.value) {
            PriceLiveUpdateScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainNavGraphPreview() {
    AppTheme {
        MainNavGraph()
    }
}
