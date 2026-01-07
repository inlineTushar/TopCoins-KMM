package com.tushar.core.di

import com.tushar.core.formatter.CurrencyFormatter
import com.tushar.core.formatter.CurrencyFormatterContract
import org.koin.dsl.module

val formatterModule = module {
    // Formatters - platform-specific implementations
    single<CurrencyFormatterContract> { CurrencyFormatter() }
}