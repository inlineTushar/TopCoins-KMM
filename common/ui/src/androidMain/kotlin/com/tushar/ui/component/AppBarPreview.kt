package com.tushar.ui.component

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tushar.ui.theme.AppTheme

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PreviewAppBar() {
    AppTheme {
        AppBar(
            title = "Preview Title",
            isBackVisible = true,
            onBack = {}
        )
    }
}
