package com.tushar.data.datasource.remote.api.realtime.model

import com.tushar.data.datasource.remote.api.realtime.instrumentation.PriceUpdateResponseSerializer
import com.tushar.data.datasource.remote.instrumentation.EpochMillisInstantSerializer
import com.tushar.data.datasource.remote.instrumentation.LongToBigDecimalSerializer
import com.tushar.core.model.BigDecimal
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = PriceUpdateResponseSerializer::class)
sealed interface PriceUpdateApiResponse {
    val event: String
}

@Serializable
data class SubscribeStatusResponse(
    override val event: String,
    val success: List<SuccessItemApiResponse>,
    val status: String
) : PriceUpdateApiResponse

@Serializable
data class SuccessItemApiResponse(
    val symbol: String,
    val exchange: String,
    val type: String
)

@Serializable
data class PriceUpdateTick(
    override val event: String,
    val symbol: String,
    val exchange: String,
    val type: String,

    @SerialName("currency_quote")
    val currencyName: String,

    @Serializable(with = LongToBigDecimalSerializer::class)
    val price: BigDecimal,

    @SerialName("currency_base")
    val currencyBase: String,
    @Serializable(with = EpochMillisInstantSerializer::class)
    val timestamp: Instant
) : PriceUpdateApiResponse
