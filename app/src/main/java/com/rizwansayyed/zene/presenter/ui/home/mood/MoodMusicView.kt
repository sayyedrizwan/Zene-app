package com.rizwansayyed.zene.presenter.ui.home.mood

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.home.mood.view.MoodGifView
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MoodViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MoodMusic() {
    val moodViewModel: MoodViewModel = hiltViewModel()
    val navViewModel: HomeNavViewModel = hiltViewModel()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(key = 1) {
            MoodGifView(moodViewModel)
        }

        stickyHeader(key = 2) {
            TextBold(
                navViewModel.selectedMood?.name ?: "",
                Modifier
                    .padding(horizontal = 10.dp, vertical = 30.dp)
                    .fillMaxWidth(),
                true,
                size = 30
            )
        }
    }

    LaunchedEffect(Unit) {
        navViewModel.selectedMood?.txt?.lowercase()?.let { moodViewModel.init(it) }
    }
}