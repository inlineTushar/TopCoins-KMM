package com.tushar.data.di

import com.tushar.data.datasource.remote.api.http.CoinApiService
import com.tushar.data.datasource.remote.api.http.CoinApiServiceImpl
import com.tushar.data.datasource.remote.api.realtime.RealtimePriceUpdateService
import com.tushar.data.datasource.remote.api.realtime.SocketPriceUpdateServiceImpl
import com.tushar.data.datasource.remote.instrumentation.BigDecimalSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.KeyProvider.Companion.KEY_12DATA
import com.tushar.data.repository.CoinRepositoryImpl
import com.tushar.data.repository.RealtimePriceUpdateRepository
import com.tushar.data.repository.RealtimePriceUpdateRepositoryImpl
import com.tushar.domain.repository.CoinRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private const val COIN_API = "https://rest.coincap.io/v3"
private const val PRICE_UPDATE_LIVE_API = "wss://ws.twelvedata.com/v1/quotes/price"

val dataModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            serializersModule = SerializersModule {
                contextual(BigDecimalSerializer)
                contextual(EpochMillisInstantSerializer)
            }
        }
    }

    single<HttpClient>(named("HTTP")) {
        createHttpClient(
            json = get(),
            isDebug = true,
            keyProvider = get(),
            isAuthRequired = true
        )
    }

    single<HttpClient>(named("WS")) {
        createHttpClient(
            json = get(),
            isDebug = true,
            keyProvider = get(),
            isAuthRequired = false,
            wsBlock = {
                val socket = WebSockets
                socket.prepare { pingInterval = 15.seconds }
                socket
            }
        )
    }

    // Coin API Service
    single<CoinApiService> {
        CoinApiServiceImpl(
            client = get(named("HTTP")),
            baseUrl = COIN_API
        )
    }

    single<RealtimePriceUpdateService> {
        val keyProvider = get<KeyProvider>()
        val apiKey = keyProvider[KEY_12DATA]
        SocketPriceUpdateServiceImpl(
            json = get(),
            socketClient = get(named("WS")),
            priceUpdateUrl = PRICE_UPDATE_LIVE_API.plus("?apikey=$apiKey"),
        )
    }

    // Repository
    singleOf(::CoinRepositoryImpl) { bind<CoinRepository>() }
    singleOf(::RealtimePriceUpdateRepositoryImpl) { bind<RealtimePriceUpdateRepository>() }
}
