package com.tushar.data.repository

import com.tushar.data.datasource.remote.model.CoinsApiResponse
import com.tushar.data.datasource.remote.model.ConversionRateApiResponse
import com.tushar.data.repository.model.CoinRepoModel
import com.tushar.data.repository.model.CoinsRepoModel
import com.tushar.data.repository.model.ConversionRateRepoModel
import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.CoinsInUsdDomainModel
import com.tushar.domain.model.ConversionRateDomainModel

// API to Repo Model mappings
fun CoinsApiResponse.asRepoModel(): CoinsRepoModel =
    CoinsRepoModel(
        timestamp = timestamp,
        coins = coins.map {
            CoinRepoModel(
                id = it.id,
                name = it.name,
                symbol = it.symbol,
                priceInUsd = it.priceInUsd,
                changePercent24Hr = it.changePercent24Hr,
            )
        }
    )

fun ConversionRateApiResponse.asRepoModel(): ConversionRateRepoModel =
    ConversionRateRepoModel(
        id = conversion.id,
        symbol = conversion.symbol,
        rateInUsd = conversion.rateInUsd,
        currencySymbol = conversion.currencySymbol,
        type = conversion.type
    )

// Repo to Domain Model mappings
fun CoinsRepoModel.asDomainModel(): CoinsInUsdDomainModel =
    CoinsInUsdDomainModel(
        timestamp = timestamp,
        coins = coins.map { it.asDomainModel() }
    )

fun CoinRepoModel.asDomainModel(): CoinInUsdDomainModel =
    CoinInUsdDomainModel(
        id = id,
        name = name,
        symbol = symbol,
        priceInUsd = priceInUsd,
        changePercent24Hr = changePercent24Hr
    )

fun ConversionRateRepoModel.asDomainModel(): ConversionRateDomainModel =
    ConversionRateDomainModel(
        id = id,
        symbol = symbol,
        rateInUsd = rateInUsd,
        currencySymbol = currencySymbol,
        type = type
    )
