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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tushar.ui.resources.Res
import com.tushar.ui.resources.common_ui_accessibility_error_occurred
import com.tushar.ui.resources.common_ui_retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorComposable(
    modifier: Modifier = Modifier,
    errorText: String,
    onRetry: () -> Unit,
    tag: String = stringResource(Res.string.common_ui_accessibility_error_occurred)
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .semantics { testTag = tag },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = errorText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = onRetry) {
                Text(text = stringResource(Res.string.common_ui_retry))
            }
        }
    }
}
