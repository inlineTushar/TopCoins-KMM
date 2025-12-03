package com.tushar.data.repository.model

import java.math.BigDecimal

data class ConversionRateRepoModel(
    val symbol: String,
    val rateInUsd: BigDecimal,
    val currencySymbol: String,
    val id: String,
    val type: String
)