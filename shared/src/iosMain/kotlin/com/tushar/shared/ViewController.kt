package com.tushar.shared

import androidx.compose.ui.window.ComposeUIViewController
import androidx.navigation.compose.rememberNavController
import com.tushar.coinlist.CoinListScreen
import com.tushar.navigation.MainNavGraph
import com.tushar.priceupdate.PriceLiveUpdateScreen
import com.tushar.ui.theme.AppTheme
import platform.UIKit.UIViewController

/**
 * iOS entry point for the Compose UI
 *
 * This function creates a UIViewController that hosts the Compose UI.
 * It should be called from Swift/Objective-C code in the iOS app.
 */
fun iOSEntryPointViewController(): UIViewController {
    return ComposeUIViewController {
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
