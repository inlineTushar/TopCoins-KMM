package com.tushar.topcoins

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.tushar.coinlist.CoinListScreen
import com.tushar.navigation.MainNavGraph
import com.tushar.priceupdate.PriceLiveUpdateScreen
import com.tushar.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                MainNavGraph(
                    navController = navController,
                    coinListScreenComposable = { CoinListScreen(navController) },
                    priceLiveUpdateScreenComposable = { PriceLiveUpdateScreen(navController) }
                )
            }
        }
    }
}
