package com.tushar.data.di

import org.koin.core.module.Module

/**
 * Expect declaration for platform-specific data module.
 *
 * Provides platform-specific implementations:
 * - Android: SecureKeyProvider(Context) with encryption
 * - iOS: SecureKeyProvider() with BuildKonfig
 */
expect val platformDataModule: Module
