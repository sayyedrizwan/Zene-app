package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.offline.TopBannerSuggestions
import com.rizwansayyed.zene.presenter.ui.home.online.LocalSongsTop
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayItemsShort
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayList
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel


@Composable
fun MainHomePageView(nav: HomeNavViewModel, room: RoomDbViewModel) {
    val recentPlayList by room.recentSongPlayed.collectAsState(initial = null)

    val columnModifier = Modifier
        .fillMaxSize()
        .background(DarkBlack)


    LazyVerticalGrid(columns = GridCells.Fixed(2), columnModifier) {
        item(span = { GridItemSpan(2) }) {
            HomepageTopView()
        }

        item(span = { GridItemSpan(2) }) {
            if (!nav.isOnline.value)
                Column {}
            else
                TopBannerSuggestions()
        }

        item(span = { GridItemSpan(2) }) {
            RecentPlayList(recentPlayList)
        }

        items(recentPlayList ?: emptyList()) {
            RecentPlayItemsShort(it)
        }
        item(span = { GridItemSpan(2) }) {
            if (nav.isOnline.value) LocalSongsTop()
        }

        item {
            Column {
                Spacer(Modifier.height(120.dp))
            }
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