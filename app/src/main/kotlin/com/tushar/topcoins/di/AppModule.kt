package com.tushar.topcoins.di

import com.tushar.coinlist.di.coinListModule
import com.tushar.data.di.dataModule
import com.tushar.domain.di.domainModule
import org.koin.dsl.module

/**
 * Main Koin module that aggregates all feature and layer modules.
 */
val appModule = module {
    includes(
        dataModule,
        domainModule,
        coinListModule
    )
}
