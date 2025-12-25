package com.tushar.data.repository

import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateApiResponse
import com.tushar.data.datasource.remote.api.realtime.model.PriceUpdateTick
import com.tushar.data.datasource.remote.api.realtime.model.SubscribeStatusResponse
import com.tushar.data.datasource.remote.api.realtime.model.SuccessItemApiResponse
import com.tushar.data.repository.model.PriceUpdateRepoModel
import com.tushar.data.repository.model.PriceUpdateTickRepoModel
import com.tushar.data.repository.model.SubscribeStatusRepoModel
import com.tushar.data.repository.model.SuccessItemRepoModel

fun PriceUpdateApiResponse.asRepoModel(): PriceUpdateRepoModel = when (this) {
    is SubscribeStatusResponse -> asRepoModel()
    is PriceUpdateTick -> asRepoModel()
}

private fun SubscribeStatusResponse.asRepoModel() =
    SubscribeStatusRepoModel(
        event = event,
        success = success.map { it.asRepoModel() },
        status = status
    )

private fun SuccessItemApiResponse.asRepoModel() =
    SuccessItemRepoModel(
        symbol = symbol,
        exchange = exchange,
        type = type
    )

private fun PriceUpdateTick.asRepoModel() =
    PriceUpdateTickRepoModel(
        event = event,
        symbol = symbol,
        exchange = exchange,
        type = type,
        currencyName = currencyName,
        price = price,
        currencyBase = currencyBase,
        timestamp = timestamp
    )
