package com.rizwansayyed.zene.presenter.ui.home.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextMedium
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.AlbumViewModel
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun AlbumView() {
    val albumViewModel: AlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor), listState
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {

            TextMedium(homeNav.selectedAlbum)
        }
    }



    LaunchedEffect(homeNav.selectedAlbum) {
        if (homeNav.selectedAlbum.isNotEmpty()) {
            albumViewModel.init(homeNav.selectedAlbum)
            listState.scrollToItem(0)
        }
    }
}