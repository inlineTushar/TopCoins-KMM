package com.tushar.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tushar.ui.R

@Composable
fun ErrorComposable(
    errorText: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    tag: String = stringResource(R.string.common_ui_accessibility_error_occurred)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .semantics { testTag = tag }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorText, textAlign = TextAlign.Center)
            if (onRetry != null) {
                Button(
                    onClick = onRetry,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = stringResource(R.string.common_ui_retry))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorComposablePreview() {
    ErrorComposable(
        errorText = "An error occurred while loading data",
        onRetry = { }
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorComposableWithoutRetryPreview() {
    ErrorComposable(
        errorText = "An error occurred while loading data"
    )
}