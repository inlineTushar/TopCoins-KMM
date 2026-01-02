package com.tushar.priceupdate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PriceLiveUpdateScreenPreview(modifier: Modifier = Modifier) {
    PriceUpdateComposable(
        state = PriceLiveUpdateUiState.Tick(
            symbol = "BTC",
            price = "$87,528.32",
            currencyName = "BTC/USD",
            isHiked = true
        ),
        modifier = modifier,
        onBack = {}
    )
}