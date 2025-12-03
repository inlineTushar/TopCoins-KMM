package com.tushar.data.datasource.remote.di

import com.tushar.data.BuildConfig
import com.tushar.data.datasource.remote.instrumentation.BigDecimalSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.datasource.remote.instrumentation.TokenInterceptor
import com.tushar.data.keyprovider.KeyProvider
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@Module
@InstallIn(SingletonComponent::class)
object HttpConfigModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            serializersModule = SerializersModule {
                contextual(BigDecimalSerializer)
                contextual(EpochMillisInstantSerializer)
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(keyProvider: KeyProvider) = TokenInterceptor(keyProvider)

    @Provides
    @Singleton
    fun provideInterceptors(
        tokenInterceptor: TokenInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): @JvmSuppressWildcards List<Interceptor> = listOf(tokenInterceptor, loggingInterceptor)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: @JvmSuppressWildcards List<Interceptor>
    ): OkHttpClient = buildOkHttpClient(interceptors = interceptors)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        json: Json,
        okHttpClient: OkHttpClient
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .client(okHttpClient)

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
}
