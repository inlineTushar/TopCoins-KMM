package com.tushar.shared

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.tushar.coinlist.CoinListScreen
import com.tushar.navigation.MainNavGraph
import androidx.navigation3.runtime.NavBackStack
import com.tushar.navigation.Route
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
