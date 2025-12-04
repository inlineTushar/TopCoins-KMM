package com.tushar.data.keyprovider

import com.tushar.data.BuildKonfig

/**
 * iOS implementation of SecureKeyProvider.
 *
 * Uses plain text keys embedded at build time via BuildKonfig.
 * The key is read from secret/key.properties during compilation and embedded into the binary.
 *
 * Note: In production, consider using iOS Keychain for runtime security.
 */
actual class SecureKeyProvider : KeyProvider {

    actual override fun get(key: String): String? {
        return when (key) {
            KeyProvider.KEY_COIN_AUTH -> BuildKonfig.COIN_AUTH_KEY
            else -> null
        }
    }
}
