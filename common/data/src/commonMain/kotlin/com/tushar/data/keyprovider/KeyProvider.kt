package com.tushar.data.keyprovider

interface KeyProvider {
    operator fun get(key: String): String?

    companion object {
        const val KEY_COIN_AUTH = "KEY_COIN_AUTH"
        const val KEY_12DATA = "KEY_12DATA"
    }
}
