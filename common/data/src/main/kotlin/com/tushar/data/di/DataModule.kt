package com.tushar.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tushar.data.BuildConfig
import com.tushar.data.datasource.remote.api.CoinApiService
import com.tushar.data.datasource.remote.instrumentation.BigDecimalSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.datasource.remote.instrumentation.TokenInterceptor
import com.tushar.data.keyprovider.KeyProvider
import com.tushar.data.keyprovider.SecureKeyProvider
import com.tushar.data.repository.CoinRepositoryImpl
import com.tushar.domain.repository.CoinRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val COIN_API = "https://rest.coincap.io/v3/"

/**
 * Koin module for data layer dependencies.
 *
 * Provides:
 * - Network configuration (OkHttp, Retrofit, JSON serialization)
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

    // HTTP Logging Interceptor
    single {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    // Token Interceptor
    single { TokenInterceptor(keyProvider = get()) }

    // List of interceptors
    single<List<Interceptor>> {
        listOf(
            get<TokenInterceptor>(),
            get<HttpLoggingInterceptor>()
        )
    }

    // OkHttp Client
    single {
        buildOkHttpClient(
            interceptors = get(),
            timeout = 30.seconds
        )
    }

    // Retrofit Builder
    single {
        Retrofit.Builder()
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .client(get())
    }

    // Coin API Service
    single {
        get<Retrofit.Builder>()
            .baseUrl(COIN_API)
            .build()
            .create(CoinApiService::class.java)
    }

    // Key Provider
    singleOf(::SecureKeyProvider) { bind<KeyProvider>() }

    // Repository
    singleOf(::CoinRepositoryImpl) { bind<CoinRepository>() }
}

private fun buildOkHttpClient(
    interceptors: List<Interceptor>,
    timeout: Duration = 30.seconds,
): OkHttpClient {
    val builder = OkHttpClient.Builder()
    for (interceptor in interceptors) {
        builder.addInterceptor(interceptor)
    }
    builder.readTimeout(timeout.toJavaDuration())
    return builder.build()
}
