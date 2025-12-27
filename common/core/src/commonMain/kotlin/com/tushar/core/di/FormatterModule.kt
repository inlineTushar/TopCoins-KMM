package com.tushar.core.di

import com.tushar.core.formatter.CurrencyFormatter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val formatterModule = module {
    // Formatters - platform-specific implementations
    singleOf(::CurrencyFormatter)
}