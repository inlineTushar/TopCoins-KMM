package com.tushar.data.repository.model

import com.tushar.domain.model.BigDecimal

data class ConversionRateRepoModel(
    val symbol: String,
    val rateInUsd: BigDecimal,
    val currencySymbol: String,
    val id: String,
    val type: String
)