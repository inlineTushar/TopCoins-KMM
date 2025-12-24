package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.serialization.json.Json

actual fun createHttpClient(
    json: Json,
    isDebug: Boolean,
    isAuthRequired: Boolean,
    keyProvider: KeyProvider,
    wsBlock: (() -> HttpClientPlugin<*, WebSockets>)?
): HttpClient = HttpClient(Darwin) {
    applyConfig(
        json = json,
        isDebug = isDebug,
        isAuthRequired = isAuthRequired,
        keyProvider = keyProvider,
        engineConfig = {
            engine {
                configureSession {
                    setTimeoutIntervalForRequest(30.0)
                    setTimeoutIntervalForResource(30.0)
                }
                configureRequest {
                    setTimeoutInterval(30.0)
                }
            }
        },
        wsBlock = wsBlock
    )
}
