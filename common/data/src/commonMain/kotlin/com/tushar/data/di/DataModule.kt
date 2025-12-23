package com.tushar.data.di

import com.tushar.data.datasource.remote.api.coin.CoinApiService
import com.tushar.data.datasource.remote.api.coin.CoinApiServiceImpl
import com.tushar.data.datasource.remote.instrumentation.BigDecimalSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.repository.CoinRepositoryImpl
import com.tushar.domain.repository.CoinRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

private const val COIN_API = "https://rest.coincap.io/v3"

/**
 * Koin module for data layer dependencies.
 * Now fully multiplatform!
 *
 * Platform-specific dependencies (SecureKeyProvider) should be provided
 * by platformDataModule in each platform's source set.
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

    // Ktor HttpClient - uses platform-specific engine
    single {
        createHttpClient(
            json = get(),
            isDebug = true, // TODO: Make configurable per build type
            keyProvider = get()
        )
    }

    // Coin API Service
    single<CoinApiService> {
        CoinApiServiceImpl(
            client = get(),
            baseUrl = COIN_API
        )
    }

    // Repository
    singleOf(::CoinRepositoryImpl) { bind<CoinRepository>() }
}
