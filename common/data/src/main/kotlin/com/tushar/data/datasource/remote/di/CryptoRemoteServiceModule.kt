package com.tushar.data.datasource.remote.di

import com.tushar.data.datasource.remote.api.CoinApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

private const val COIN_API = "https://rest.coincap.io/v3/"

@Module
@InstallIn(SingletonComponent::class)
object CoinRemoteServiceModule {

    @Provides
    @Singleton
    fun provideCoinApiService(builder: Retrofit.Builder): CoinApiService =
        builder
            .baseUrl(COIN_API)
            .build()
            .create(CoinApiService::class.java)
}
