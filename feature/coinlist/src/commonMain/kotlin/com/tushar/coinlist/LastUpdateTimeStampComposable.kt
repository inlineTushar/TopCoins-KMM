package com.tushar.coinlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.tushar.feature.coinlist.generated.resources.Res
import com.tushar.feature.coinlist.generated.resources.feature_coinlist_updated_at
import com.tushar.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LastUpdateTimeStampComposable(
    updatedAtFormated: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(Res.string.feature_coinlist_updated_at, updatedAtFormated),
        color = MaterialTheme.colorScheme.inversePrimary,
        fontSize = 14.sp,
        textAlign = TextAlign.End,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
@Preview(showBackground = true)
fun LastUpdateTimeStampComposablePreview() {
    AppTheme {
        LastUpdateTimeStampComposable(updatedAtFormated = "14:30:23")
    }
}