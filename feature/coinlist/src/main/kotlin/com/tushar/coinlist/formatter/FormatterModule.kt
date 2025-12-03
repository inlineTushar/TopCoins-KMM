package com.tushar.coinlist.formatter

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FormatterModule {

    @Binds
    abstract fun bindCurrencyFormatter(
        real: DefaultCurrencyFormatter
    ): CurrencyFormatter

    @Binds
    abstract fun bindPercentageFormatter(
        real: DefaultPercentageFormatter
    ): PercentageFormatter

    @Binds
    abstract fun bindTimeFormatter(
        real: DefaultTimeFormatter
    ): TimeFormatter
}
