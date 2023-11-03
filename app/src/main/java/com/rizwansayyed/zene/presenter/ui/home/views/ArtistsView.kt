package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID

@Composable
fun ArtistsView() {
    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor),
    ) {

    }

    LaunchedEffect(Unit) {

    }
}