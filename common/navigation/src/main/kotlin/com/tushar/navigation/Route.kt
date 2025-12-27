package com.tushar.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val value: String) {
    @Serializable
    data object CoinList : Route("coin/list")
    @Serializable
    data object PriceLiveUpdate : Route("price/live")
}
