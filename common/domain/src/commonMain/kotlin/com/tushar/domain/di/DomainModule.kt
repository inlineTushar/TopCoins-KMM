package com.tushar.domain.di

import com.tushar.domain.GetCoinUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module for domain layer dependencies.
 *
 * Provides:
 * - Use cases
 */
val domainModule = module {
    singleOf(::GetCoinUseCase)
}
