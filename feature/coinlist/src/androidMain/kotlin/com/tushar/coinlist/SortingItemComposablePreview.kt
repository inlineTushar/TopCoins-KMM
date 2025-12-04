package com.tushar.coinlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun SortingItemComposablePreview() {
    SortingItemComposable(
        sortType = SortType.WORST_PERFORM,
        onClickSort = {}
    )
}
