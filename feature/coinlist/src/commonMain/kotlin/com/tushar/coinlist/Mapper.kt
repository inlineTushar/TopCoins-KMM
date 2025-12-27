package com.tushar.coinlist

import com.tushar.coinlist.formatter.PercentageFormatter
import com.tushar.core.formatter.CurrencyFormatter
import com.tushar.domain.model.CoinDomainModel

fun List<CoinDomainModel>.asUiModel(
    currencyFormatter: CurrencyFormatter,
    percentageFormatter: PercentageFormatter,
) = map { it.asUiModel(currencyFormatter, percentageFormatter) }

private fun CoinDomainModel.asUiModel(
    currencyFormatter: CurrencyFormatter,
    percentageFormatter: PercentageFormatter,
) = CoinUIModel(
    id = id,
    name = name,
    symbol = symbol,
    currencyCode = coinCurrency.code,
    changePercent24Hr = percentageFormatter.format(changePercent24Hr),
    price = currencyFormatter.format(price, code = coinCurrency.code),
)
