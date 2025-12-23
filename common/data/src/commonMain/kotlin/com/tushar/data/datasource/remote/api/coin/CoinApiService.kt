package com.tushar.data.datasource.remote.api.coin

import com.tushar.data.datasource.remote.model.CoinApiDetailResponse
import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.data.datasource.remote.model.ConversionRateApiResponse

interface CoinApiService : CoinCoroutinesRemoteService {
    suspend fun getCoins(): CoinsApiResponse
    suspend fun getCoin(coinName: String): CoinApiDetailResponse
    suspend fun getRates(currency: String): ConversionRateApiResponse
}
