package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.SecureKeyProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Android-specific data module.
 * Provides SecureKeyProvider with Android Context for decryption.
 */
actual val platformDataModule = module {
    singleOf(::SecureKeyProvider) { bind<KeyProvider>() }
}
