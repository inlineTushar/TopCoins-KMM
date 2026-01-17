@file:OptIn(ExperimentalTime::class)

package com.tushar.data.datasource.remote.api.http.model

import com.tushar.core.model.BigDecimal
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@OptIn(InternalSerializationApi::class)
@Serializable
data class CoinApiDetailResponse(
    @Contextual
    val timestamp: Instant,
    @SerialName("data")
    val coin: CoinApiModel
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class CoinApiModel(
    val id: String,
    val name: String,
    val symbol: String,
    @SerialName("priceUsd")
    @Contextual val priceInUsd: BigDecimal,
    val changePercent24Hr: Double,
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class CoinsApiResponse(
    @Contextual
    val timestamp: Instant,
    @SerialName("data")
    val coins: List<CoinApiModel>
)
