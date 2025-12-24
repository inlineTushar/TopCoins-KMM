package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun createHttpClient(
    json: Json,
    isDebug: Boolean,
    isAuthRequired: Boolean,
    keyProvider: KeyProvider,
    wsBlock: (() -> HttpClientPlugin<*, WebSockets>)? = null
): HttpClient

/**
 * Common HttpClient configuration shared across all platforms.
 * This is called by platform-specific implementations to apply common settings.
 */
fun <T : HttpClientEngineConfig> HttpClientConfig<T>.applyConfig(
    json: Json,
    isDebug: Boolean,
    isAuthRequired: Boolean,
    keyProvider: KeyProvider,
    engineConfig: HttpClientConfig<T>.() -> Unit,
    wsBlock: (() -> HttpClientPlugin<*, WebSockets>)? = null
) {
    // JSON Content Negotiation
    install(ContentNegotiation) {
        json(json)
    }

    // Logging
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                if (isDebug) {
                    println("Ktor: $message")
                }
            }
        }
        level = if (isDebug) LogLevel.ALL else LogLevel.NONE
    }

    if (isAuthRequired) {
        install(Auth) {
            bearer {
                loadTokens {
                    val token = keyProvider[KeyProvider.KEY_COIN_AUTH]
                    token?.let { BearerTokens(it, "") }
                }
            }
        }
    }

    engine { engineConfig() }

    wsBlock?.let { install(it()) }

    // Default request configuration
    defaultRequest {
        contentType(ContentType.Application.Json)
    }
}
