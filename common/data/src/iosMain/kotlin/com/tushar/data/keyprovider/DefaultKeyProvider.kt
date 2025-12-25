package com.tushar.data.keyprovider

import com.tushar.common.data.BuildKonfig

/**
 * iOS implementation of SecureKeyProvider.
 *
 * Uses plain text keys embedded at build time via BuildKonfig.
 * The key is read from secret/key.properties during compilation and embedded into the binary.
 *
 * Note: In production, consider using iOS Keychain for runtime security.
 */
class DefaultKeyProvider : KeyProvider {
    override fun get(key: String): String? = when (key) {
        KeyProvider.KEY_COIN_AUTH -> BuildKonfig.KEY_COIN_AUTH
        KeyProvider.KEY_12DATA -> BuildKonfig.KEY_12DATA
        else -> null
    }
}
