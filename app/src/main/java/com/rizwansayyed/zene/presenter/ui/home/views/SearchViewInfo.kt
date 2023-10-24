package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID


@Composable
fun SearchViewInfo(s: String) {
    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.padding(start = 12.dp)) {
                Spacer(Modifier.height(30.dp))

                Row {
                    TextBold(s, size = 35)
                }

                Spacer(Modifier.height(30.dp))
            }
        }
    }
}