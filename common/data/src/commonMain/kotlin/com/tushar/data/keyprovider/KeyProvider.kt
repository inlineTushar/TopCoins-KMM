package com.tushar.data.keyprovider

interface KeyProvider {
    operator fun get(key: String): String?

    companion object {
        const val KEY_COIN_AUTH = "COIN_AUTH_KEY"
    }
}
