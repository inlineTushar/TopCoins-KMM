package com.tushar.coinlist.di

import com.tushar.coinlist.CoinListViewModel
import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.coinlist.formatter.TimeFormatter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for CoinList feature dependencies.
 *
 * Provides:
 * - ViewModels
 * - Formatters (platform-specific implementations via expect/actual)
 */
val coinListModule = module {

    // Formatters - platform-specific implementations
    singleOf(::PercentageFormatter)
    singleOf(::TimeFormatter)

    // ViewModel - using factory for KMP
    factory {
        CoinListViewModel(
            useCase = get(),
            currencyFormatter = get(),
            percentageFormatter = get(),
            timeFormatter = get()
        )
    }
}
