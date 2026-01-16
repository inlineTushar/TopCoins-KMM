package com.tushar.data.datasource.remote.api.http.model

import com.tushar.core.model.BigDecimal
import kotlin.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ConversionRateApiResponse(
    @Contextual
    val timestamp: Instant,
    @SerialName("data")
    val conversion: ConversionRateApiModel,
)

@OptIn(InternalSerializationApi::class)
@Serializable
data class ConversionRateApiModel(
    val symbol: String,
    @SerialName("rateUsd")
    @Contextual
    val rateInUsd: BigDecimal,
    val currencySymbol: String,
    val id: String,
    val type: String
)
