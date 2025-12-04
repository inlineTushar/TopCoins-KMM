package com.tushar.data.datasource.remote.model

import com.tushar.domain.model.BigDecimal
import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class CoinApiDetailResponse(
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
