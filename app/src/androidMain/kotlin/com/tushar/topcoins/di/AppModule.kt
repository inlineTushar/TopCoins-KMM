package com.tushar.topcoins.di

import com.tushar.coinlist.di.coinListModule
import com.tushar.core.di.coroutineModule
import com.tushar.core.di.formatterModule
import com.tushar.data.di.dataModule
import com.tushar.data.di.platformDataModule
import com.tushar.domain.di.domainModule
import com.tushar.priceupdate.di.priceUpdateModule
import org.koin.dsl.module

/**
 * Main Koin module that aggregates all feature and layer modules.
 */
val appModule = module {
    includes(
        platformDataModule,  // Platform-specific (Android SecureKeyProvider)
        dataModule,
        domainModule,
        coroutineModule,
        formatterModule,
        coinListModule,
        priceUpdateModule
    )
}
