package com.rizwansayyed.zene.presenter.ui.home.mood

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MoodViewModel

@Composable
fun MoodMusic() {
    val moodViewModel: MoodViewModel = hiltViewModel()
    val navViewModel: HomeNavViewModel = hiltViewModel()

    LazyVerticalGrid(
        GridCells.Fixed(UiUtils.GridSpan.TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(key = 1, span = { GridItemSpan(UiUtils.GridSpan.TOTAL_ITEMS_GRID) }) {
            MoodGifView(moodViewModel)
        }
    }

    LaunchedEffect(Unit) {
        moodViewModel.init(navViewModel.selectedMood)
    }
}