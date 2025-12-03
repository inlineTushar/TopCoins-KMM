package com.tushar.domain

import com.tushar.domain.model.CoinCurrency
import com.tushar.domain.model.CoinDomainModel
import com.tushar.domain.model.CoinInUsdDomainModel
import com.tushar.domain.model.ConversionRateDomainModel
import java.math.RoundingMode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Converts a list of coins with USD prices to coins with adjusted prices based on conversion rate.
 * This is domain-level business logic for currency conversion.
 */
fun List<CoinInUsdDomainModel>.asPriceAdjusted(rate: ConversionRateDomainModel): List<CoinDomainModel> =
    map { it.asDomainModel(rate) }

private fun CoinInUsdDomainModel.asDomainModel(rate: ConversionRateDomainModel): CoinDomainModel =
    CoinDomainModel(
        id = id,
        name = name,
        symbol = symbol,
        price = priceInUsd.divide(rate.rateInUsd, 5, RoundingMode.HALF_UP),
        coinCurrency = CoinCurrency(
            id = rate.id,
            code = rate.symbol,
            symbol = rate.currencySymbol,
            type = rate.type,
            rateInUsd = rate.rateInUsd
        ),
        changePercent24Hr = changePercent24Hr
    )

fun Throwable.asDomainError(): Throwable = when (this) {
    is UnknownHostException,
    is ConnectException -> DomainError.NoConnectivity

    is SocketTimeoutException -> DomainError.NetworkTimeout
    else -> DomainError.UnknownError
}
