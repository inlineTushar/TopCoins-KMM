package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

/**
 * Android implementation of HttpClient using Android engine.
 */
actual fun createHttpClient(
    json: Json,
    isDebug: Boolean,
    keyProvider: KeyProvider
): HttpClient {
    return HttpClient(Android) {
        // Apply common configuration
        applyCommonConfig(json, isDebug, keyProvider)

        // Android-specific engine configuration
        engine {
            connectTimeout = 30.seconds.inWholeMilliseconds.toInt()
            socketTimeout = 30.seconds.inWholeMilliseconds.toInt()
        }
    }
}
