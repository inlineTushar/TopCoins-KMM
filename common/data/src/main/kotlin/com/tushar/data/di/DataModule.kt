package com.tushar.data.di

import com.tushar.data.BuildConfig
import com.tushar.data.datasource.remote.api.CoinApiService
import com.tushar.data.datasource.remote.instrumentation.BigDecimalSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.SecureKeyProvider
import com.tushar.data.repository.CoinRepositoryImpl
import com.tushar.domain.repository.CoinRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

private const val COIN_API = "https://rest.coincap.io/v3"

/**
 * Koin module for data layer dependencies.
 *
 * Provides:
 * - Network configuration (Ktor HTTP Client with JSON serialization)
 * - API services
 * - Repositories
 * - Key providers
 */
val dataModule = module {

    // JSON configuration
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

    // Key Provider
    singleOf(::SecureKeyProvider) { bind<KeyProvider>() }

    // Ktor HttpClient
    single {
        HttpClient(Android) {
            // JSON Content Negotiation
            install(ContentNegotiation) {
                json(get<Json>())
            }

            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        if (BuildConfig.DEBUG) {
                            println("Ktor: $message")
                        }
                    }
                }
                level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
            }

            // Authentication
            install(Auth) {
                bearer {
                    loadTokens {
                        val keyProvider: KeyProvider = get()
                        val token = keyProvider[SecureKeyProvider.KEY_COIN_AUTH]
                        token?.let { BearerTokens(it, "") }
                    }
                }
            }

            // Default request configuration
            defaultRequest {
                contentType(ContentType.Application.Json)
            }

            // Engine configuration
            engine {
                connectTimeout = 30.seconds.inWholeMilliseconds.toInt()
                socketTimeout = 30.seconds.inWholeMilliseconds.toInt()
            }
        }
    }

    // Coin API Service
    single {
        CoinApiService(
            client = get(),
            baseUrl = COIN_API
        )
    }

    // Repository
    singleOf(::CoinRepositoryImpl) { bind<CoinRepository>() }
}
