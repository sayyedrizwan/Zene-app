package com.rizwansayyed.zene.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun FeedView(viewModel: ZeneViewModel) {
    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {

            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.startGettingFeed()
    }
}