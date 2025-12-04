package com.tushar.coinlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tushar.ui.theme.AppTheme

@Composable
@Preview(showBackground = true)
fun LastUpdateTimeStampComposablePreview() {
    AppTheme {
        LastUpdateTimeStampComposable(updatedAtFormated = "14:30:23")
    }
}
