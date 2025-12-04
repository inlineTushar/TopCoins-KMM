package com.tushar.coinlist.di

import com.tushar.coinlist.CoinListViewModel
import com.tushar.coinlist.formatter.CurrencyFormatter
import com.tushar.coinlist.formatter.DefaultCurrencyFormatter
import com.tushar.coinlist.formatter.DefaultPercentageFormatter
import com.tushar.coinlist.formatter.DefaultTimeFormatter
import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.coinlist.formatter.TimeFormatter
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for CoinList feature dependencies.
 *
 * Provides:
 * - ViewModels
 * - Formatters
 */
val coinListModule = module {

    // Formatters
    singleOf(::DefaultCurrencyFormatter) { bind<CurrencyFormatter>() }
    singleOf(::DefaultPercentageFormatter) { bind<PercentageFormatter>() }
    singleOf(::DefaultTimeFormatter) { bind<TimeFormatter>() }

    // ViewModel
    viewModelOf(::CoinListViewModel)
}
