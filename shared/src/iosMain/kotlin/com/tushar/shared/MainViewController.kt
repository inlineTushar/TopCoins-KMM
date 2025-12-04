package com.tushar.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.tushar.coinlist.CoinListScreen
import com.tushar.coinlist.di.coinListModule
import com.tushar.data.di.dataModule
import com.tushar.data.di.platformDataModule
import com.tushar.domain.di.domainModule
import com.tushar.ui.theme.AppTheme
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

/**
 * Initialize Koin for iOS.
 * This should be called once when the iOS app starts.
 */
fun initKoin() {
    startKoin {
        modules(
            platformDataModule,  // iOS-specific implementations
            dataModule,          // Data layer
            domainModule,        // Domain layer
            coinListModule       // Feature modules
        )
    }
}

/**
 * iOS entry point for the Compose UI
 *
 * This function creates a UIViewController that hosts the Compose UI.
 * It should be called from Swift/Objective-C code in the iOS app.
 */
fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        AppTheme {
            CoinListScreen()
        }
    }
}
