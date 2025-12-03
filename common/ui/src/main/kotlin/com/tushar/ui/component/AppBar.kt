package com.tushar.ui.component

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.tushar.ui.R
import com.tushar.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    isBackVisible: Boolean,
    onBack: () -> Unit = {},
    label: String = stringResource(R.string.common_ui_title_appbar),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    if (isBackVisible) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.common_ui_accessibility_back_button)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor,
                navigationIconContentColor = contentColor
            ),
            modifier = Modifier.semantics { testTag = label }
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor,
                titleContentColor = contentColor
            ),
            modifier = Modifier.semantics { testTag = label }
        )
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark Mode"
)
@Composable
fun PreviewNucleusAppBar() {
    AppTheme {
        AppBar(
            title = "Preview Title",
            isBackVisible = true,
            onBack = {}
        )
    }
}