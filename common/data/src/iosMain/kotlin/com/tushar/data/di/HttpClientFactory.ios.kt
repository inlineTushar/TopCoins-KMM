package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import kotlinx.serialization.json.Json

/**
 * iOS implementation of HttpClient using Darwin engine.
 */
actual fun createHttpClient(
    json: Json,
    isDebug: Boolean,
    keyProvider: KeyProvider
): HttpClient {
    return HttpClient(Darwin) {
        // Apply common configuration
        applyCommonConfig(json, isDebug, keyProvider)

        // Darwin (iOS) engine configuration
        engine {
            configureRequest {
                setTimeoutInterval(30.0)
            }
        }
    }
}
