package com.tushar.data.datasource.remote.api

import com.tushar.data.datasource.remote.model.CoinApiDetailResponse
import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.data.datasource.remote.model.ConversionRateApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Ktor-based API service for cryptocurrency data.
 * Replaces Retrofit with Ktor for more idiomatic Kotlin networking.
 */
class CoinApiServiceImpl(
    private val client: HttpClient,
    private val baseUrl: String
) : CoinApiService {

    /**
     * Get list of all coins
     */
    override suspend fun getCoins(): CoinsApiResponse {
        return client.get("$baseUrl/assets").body()
    }

    /**
     * Get details of a specific coin
     */
    override suspend fun getCoin(coinName: String): CoinApiDetailResponse {
        return client.get("$baseUrl/assets/$coinName").body()
    }

    /**
     * Get conversion rates for a specific currency
     */
    override suspend fun getRates(currency: String): ConversionRateApiResponse {
        return client.get("$baseUrl/rates/$currency").body()
    }
}
