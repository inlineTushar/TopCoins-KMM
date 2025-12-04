package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.SecureKeyProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android-specific data module.
 * Provides SecureKeyProvider with Android Context for decryption.
 */
actual val platformDataModule = module {
    single<KeyProvider> {
        SecureKeyProvider(androidContext())
    }
}
