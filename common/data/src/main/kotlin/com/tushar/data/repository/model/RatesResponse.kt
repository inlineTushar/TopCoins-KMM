package com.tushar.data.repository.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class RatesApiResponse(
    @Contextual
    val timestamp: Instant,
    @SerialName("data")
    val rate: RateApiModel,
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class RateApiModel(
    val symbol: String,
    val rateUsd: String,
    val currencySymbol: String,
    val id: String,
    val type: String
)
