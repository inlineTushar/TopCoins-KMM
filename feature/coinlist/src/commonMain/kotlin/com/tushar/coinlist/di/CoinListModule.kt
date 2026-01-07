package com.tushar.coinlist.di

import com.tushar.coinlist.CoinListViewModel
import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.coinlist.formatter.PercentageFormatterContract
import com.tushar.coinlist.formatter.TimeFormatter
import com.tushar.coinlist.formatter.TimeFormatterContract
import org.koin.dsl.module

/**
 * Koin module for CoinList feature dependencies.
 *
 * Provides:
 * - ViewModels
 * - Formatters (platform-specific implementations via expect/actual)
 */
val coinListModule = module {

    // Formatters - platform-specific implementations bound to interfaces
    single<PercentageFormatterContract> { PercentageFormatter() }
    single<TimeFormatterContract> { TimeFormatter() }

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
