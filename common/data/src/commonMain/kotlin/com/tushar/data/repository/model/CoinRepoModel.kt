package com.tushar.data.repository.model

import com.tushar.core.model.BigDecimal
import kotlinx.datetime.Instant

data class CoinRepoModel(
    val id: String,
    val name: String,
    val symbol: String,
    val priceInUsd: BigDecimal,
    val changePercent24Hr: Double,
)

data class CoinsRepoModel(
    val timestamp: Instant,
    val coins: List<CoinRepoModel>
)
