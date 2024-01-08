package com.rizwansayyed.zene.presenter.ui.home.mood


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.home.mood.view.MoodGifView
import com.rizwansayyed.zene.presenter.ui.home.mood.view.MoodPlaylistsSongs
import com.rizwansayyed.zene.presenter.ui.home.mood.view.MoodTopSongs
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MoodViewModel
import java.util.UUID

@Composable
fun MoodMusic() {
    val moodViewModel: MoodViewModel = hiltViewModel()
    val navViewModel: HomeNavViewModel = hiltViewModel()


    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(key = 1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MoodGifView(moodViewModel)
        }

        item(key = 2, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            TextBold(
                navViewModel.selectedMood?.name ?: "",
                Modifier
                    .padding(horizontal = 10.dp, vertical = 30.dp)
                    .fillMaxWidth(),
                true,
                size = 30
            )
        }


        item(key = 3, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            MoodPlaylistsSongs(moodViewModel)
        }

        item(key = 4, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.fillMaxWidth()) {
                MoodTopSongs(moodViewModel)
            }
        }

        item(key = 5, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(30.dp))
        }

        item(key = 100) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        navViewModel.selectedMood?.txt?.lowercase()?.let { moodViewModel.init(it) }
    }
}