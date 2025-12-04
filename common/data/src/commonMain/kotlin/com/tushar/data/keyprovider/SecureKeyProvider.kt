package com.tushar.data.keyprovider

/**
 * Expect declaration for platform-specific secure key provider.
 *
 * - Android: Uses encrypted key from res/raw with Context and javax.crypto
 * - iOS: Uses plain text key embedded at build time via BuildKonfig
 *
 * Note: Constructor parameters are platform-specific (expect/actual pattern).
 * Use platform-specific Koin modules to provide dependencies.
 */
expect class SecureKeyProvider : KeyProvider {
    override fun get(key: String): String?
}
