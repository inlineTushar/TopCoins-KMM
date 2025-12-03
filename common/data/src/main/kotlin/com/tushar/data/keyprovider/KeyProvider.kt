package com.tushar.data.keyprovider

interface KeyProvider {
    operator fun get(key: String): String?
}
