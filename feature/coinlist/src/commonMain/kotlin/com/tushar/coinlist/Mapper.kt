package com.tushar.coinlist

import com.tushar.coinlist.formatter.PercentageFormatterContract
import com.tushar.core.formatter.CurrencyFormatterContract
import com.tushar.domain.model.CoinDomainModel

fun List<CoinDomainModel>.asUiModel(
    currencyFormatter: CurrencyFormatterContract,
    percentageFormatter: PercentageFormatterContract,
) = map { it.asUiModel(currencyFormatter, percentageFormatter) }

private fun CoinDomainModel.asUiModel(
    currencyFormatter: CurrencyFormatterContract,
    percentageFormatter: PercentageFormatterContract,
) = CoinUIModel(
    id = id,
    name = name,
    symbol = symbol,
    currencyCode = coinCurrency.code,
    changePercent24Hr = percentageFormatter.format(changePercent24Hr),
    price = currencyFormatter.format(price, code = coinCurrency.code),
)
