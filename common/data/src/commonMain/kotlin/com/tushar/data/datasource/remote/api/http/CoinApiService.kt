package com.tushar.data.datasource.remote.api.http

import com.tushar.data.datasource.remote.api.RemoteService
import com.tushar.data.datasource.remote.model.CoinApiDetailResponse
import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.data.datasource.remote.model.ConversionRateApiResponse

interface CoinApiService : RemoteService {
    suspend fun getCoins(): CoinsApiResponse
    suspend fun getCoin(coinName: String): CoinApiDetailResponse
    suspend fun getRates(currency: String): ConversionRateApiResponse
}
