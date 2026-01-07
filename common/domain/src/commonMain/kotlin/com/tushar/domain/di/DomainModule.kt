package com.tushar.domain.di

import com.tushar.domain.GetCoinUseCase
import com.tushar.domain.GetCoinUseCaseImpl
import org.koin.dsl.module

/**
 * Koin module for domain layer dependencies.
 *
 * Provides:
 * - Use cases
 */
val domainModule = module {
    single<GetCoinUseCase> {
        GetCoinUseCaseImpl(
            repository = get(),
            dispatcher = get()
        )
    }
}
