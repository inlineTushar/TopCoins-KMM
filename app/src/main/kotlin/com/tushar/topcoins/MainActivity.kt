package com.tushar.topcoins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import com.tushar.coinlist.CoinListScreen
import com.tushar.navigation.MainNavGraph
import com.tushar.navigation.Route
import com.tushar.priceupdate.PriceLiveUpdateScreen
import com.tushar.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = remember { NavBackStack<Route>(Route.CoinList) }
            AppTheme {
                MainNavGraph(
                    backStack = backStack,
                    coinListScreenComposable = { CoinListScreen(backStack) },
                    priceLiveUpdateScreenComposable = { PriceLiveUpdateScreen(backStack) }
                )
            }
        }
    }
}
