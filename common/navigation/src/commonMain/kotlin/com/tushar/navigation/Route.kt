package com.tushar.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val value: String) : NavKey {
    @Serializable
    data object CoinList : Route("coin/list")
    @Serializable
    data object PriceLiveUpdate : Route("price/live")
}
