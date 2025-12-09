package com.tushar.core.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual val coroutineModule = module {
    single<CoroutineDispatcher> { Dispatchers.Default }
}
