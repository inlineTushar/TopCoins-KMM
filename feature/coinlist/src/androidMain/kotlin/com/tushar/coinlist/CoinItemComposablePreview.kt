package com.tushar.coinlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun CoinItemPreview() {
    CoinItemComposable(
        coinName = "Bitcoin",
        coinSymbol = "BTC",
        coinPrice = "6459.34",
        coinChange = "+4.44%"
    )
}
