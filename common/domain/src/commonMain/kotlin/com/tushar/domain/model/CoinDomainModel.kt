package com.tushar.domain.model

import com.tushar.core.model.BigDecimal
import kotlin.time.Instant

data class CoinsInUsdDomainModel(
    val timestamp: Instant,
    val coins: List<CoinInUsdDomainModel>
)

data class CoinInUsdDomainModel(
    val id: String,
    val name: String,
    val symbol: String,
    val priceInUsd: BigDecimal,
    val changePercent24Hr: Double,
)

data class CoinsDomainModel(
    val timestamp: Instant,
    val coins: List<CoinDomainModel>
)

data class CoinDomainModel(
    val id: String,
    val name: String,
    val symbol: String,
    val price: BigDecimal,
    val coinCurrency: CoinCurrency,
    val changePercent24Hr: Double,
)

data class CoinCurrency(
    val id: String,
    val code: String,
    val symbol: String,
    val type: String,
    val rateInUsd: BigDecimal
)

data class ConversionRateDomainModel(
    val symbol: String,
    val rateInUsd: BigDecimal,
    val currencySymbol: String,
    val id: String,
    val type: String
)