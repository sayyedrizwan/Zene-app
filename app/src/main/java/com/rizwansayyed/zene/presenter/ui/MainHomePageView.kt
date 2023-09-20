package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.offline.TopBannerSuggestions
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayList
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun MainHomePageView() {
    val nav: HomeNavViewModel = hiltViewModel()
    val recentPlay = remember { mutableStateOf<List<RecentPlayedEntity>?>(null) }

    val columnModifier = Modifier
        .fillMaxSize()
        .background(DarkBlack)


    LazyVerticalGrid(columns = GridCells.Fixed(2), columnModifier) {
        item(span = { GridItemSpan(2) }) {
            HomepageTopView()
        }

        item(span = { GridItemSpan(2) }) {
//            if (nav.isOnline.value)
//                Column {}
//            else
            TopBannerSuggestions()
        }

        item(span = { GridItemSpan(2) }) {
            RecentPlayList(recentPlay)
        }

        items(recentPlay.value ?: emptyList()) {
            RecentPlayList(recentPlay)
        }

//        when (val v = offlineViewModel.allSongs.value) {
//            SongDataResponse.Empty -> {}
//            is SongDataResponse.Error -> {}
//            SongDataResponse.Loading -> {}
//            is SongDataResponse.Success -> items(v.item) {
//                Column {
//                    AsyncImage(
//                        model = it.art,
//                        contentDescription = null,
//                        modifier = Modifier.size(70.dp),
//                    )
//                    Text(text = it.title)
//                    Text(text = it.artist)
//                    Text(text = it.art.toString())
//                }
//            }
//        }
    }

//    LaunchedEffect(Unit) {
//        offlineViewModel.songsList()
//        offlineViewModel.songAddedThisWeek()
//    }
}