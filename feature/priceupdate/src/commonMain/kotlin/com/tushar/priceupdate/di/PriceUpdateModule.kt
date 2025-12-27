package com.tushar.priceupdate.di

import com.tushar.priceupdate.PriceLiveUpdateViewModel
import org.koin.dsl.module

val priceUpdateModule = module {
    factory {
        PriceLiveUpdateViewModel(
            realtimePriceUpdateService = get(),
            currencyFormatter = get()
        )
    }
}
