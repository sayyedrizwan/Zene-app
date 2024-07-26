package com.rizwansayyed.zene.ui.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun MyMusicView(viewModel: ZeneViewModel) {
    LazyColumn(
        Modifier.fillMaxSize().background(DarkCharcoal)
    ) {
        item {

        }
    }
}