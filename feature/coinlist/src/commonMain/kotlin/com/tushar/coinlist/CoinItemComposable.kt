package com.tushar.coinlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CoinItemComposable(
    coinName: String,
    coinSymbol: String,
    coinPrice: String,
    coinChange: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics {
                contentDescription =
                    "Coin item: $coinName ($coinSymbol), price $coinPrice, change in last 24 hours $coinChange"
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = coinName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.semantics { contentDescription = "Coin name: $coinName" }
            )
            Text(
                text = coinSymbol,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .semantics { contentDescription = "Symbol: $coinSymbol" }
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = coinChange,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { contentDescription = "Change in last 24 hours: $coinChange" }
            )
            Text(
                text = coinPrice,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .semantics { contentDescription = "Price: $coinPrice" }
            )
        }
    }
}

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
