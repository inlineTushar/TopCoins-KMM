package com.tushar.data.datasource.remote.api

import com.tushar.data.datasource.remote.model.CoinApiDetailResponse
import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.data.datasource.remote.model.ConversionRateApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinApiService : CoinCoroutinesRemoteService {

    @GET("assets")
    suspend fun getCoins(): CoinsApiResponse

    @GET("assets/{coin_name}")
    suspend fun getCoin(@Path("coin_name") coinName: String): CoinApiDetailResponse

    @GET("rates/{currency}")
    suspend fun getRates(@Path("currency") currency: String): ConversionRateApiResponse
}
