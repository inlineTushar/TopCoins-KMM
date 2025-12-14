package com.tushar.shared

import com.tushar.coinlist.di.coinListModule
import com.tushar.core.di.coroutineModule
import com.tushar.data.di.dataModule
import com.tushar.data.di.platformDataModule
import com.tushar.domain.di.domainModule
import org.koin.core.context.startKoin

/**
 * Initialize Koin for iOS.
 * This should be called once when the iOS app starts.
 */
fun startKoin() {
    startKoin {
        modules(
            platformDataModule,  // iOS-specific implementations
            dataModule,          // Data layer
            domainModule,        // Domain layer
            coroutineModule,     // Core module
            coinListModule       // Feature modules
        )
    }
}