package com.tushar.data.repository.model

import com.tushar.core.model.BigDecimal
import kotlinx.datetime.Instant

sealed interface PriceUpdateRepoModel {
    val event: String
}

data class SubscribeStatusRepoModel(
    override val event: String,
    val success: List<SuccessItemRepoModel>,
    val status: String
) : PriceUpdateRepoModel

data class SuccessItemRepoModel(
    val symbol: String,
    val exchange: String,
    val type: String
)

data class PriceUpdateTickRepoModel(
    override val event: String,
    val symbol: String,
    val exchange: String,
    val type: String,
    val currencyName: String,
    val price: BigDecimal,
    val currencyBase: String,
    val timestamp: Instant
) : PriceUpdateRepoModel

fun PriceUpdateTickRepoModel.compareTo(another: PriceUpdateTickRepoModel): Int = price.compareTo(another.price)