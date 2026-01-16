package com.tushar.coinlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource

@Composable
fun SortingItemComposable(
    sortType: SortType,
    onClickSort: (SortType) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(
            items = SortType.entries,
            key = { it.ordinal },
        ) { type ->
            Button(onClick = { onClickSort(type) }) {
                Text(
                    text = stringResource(type.stringResource),
                    color = if (type == sortType) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = if (type == sortType) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortingItemComposablePreview() {
    SortingItemComposable(
        sortType = SortType.WORST_PERFORM,
        onClickSort = {}
    )
}
