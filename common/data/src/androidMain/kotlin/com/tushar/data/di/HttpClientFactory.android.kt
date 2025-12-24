package com.tushar.data.di

import com.tushar.data.keyprovider.KeyProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

actual fun createHttpClient(
    json: Json,
    isDebug: Boolean,
    isAuthRequired: Boolean,
    keyProvider: KeyProvider,
    wsBlock: (() -> HttpClientPlugin<*, WebSockets>)?
): HttpClient = HttpClient(OkHttp) {
    applyConfig(
        json = json,
        isDebug = isDebug,
        isAuthRequired = isAuthRequired,
        keyProvider = keyProvider,
        engineConfig = {
            engine {
                config {
                    connectTimeout(30.seconds.toJavaDuration())
                    readTimeout(30.seconds.toJavaDuration())
                    writeTimeout(30.seconds.toJavaDuration())
                }
            }
        },
        wsBlock = wsBlock
    )
}
