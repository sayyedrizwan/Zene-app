package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.offline.TopBannerSuggestions
import com.rizwansayyed.zene.presenter.ui.home.online.CityRadioViewList
import com.rizwansayyed.zene.presenter.ui.home.online.LocalSongsTop
import com.rizwansayyed.zene.presenter.ui.home.online.PlaylistList
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsList
import com.rizwansayyed.zene.presenter.ui.home.online.TopGlobalSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.TrendingSongsCountryList
import com.rizwansayyed.zene.presenter.ui.home.view.OfflineDownloadHeader
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

    LazyVerticalGrid(columns = GridCells.Fixed(3), columnModifier) {
        item(span = { GridItemSpan(3) }) {
            HomepageTopView()
        }

        item(span = { GridItemSpan(3) }) {
            if (nav.isOnline.value)
                Column {}
            else
                TopBannerSuggestions()
        }

        item(span = { GridItemSpan(3) }) {
            RecentPlayList(recentPlayList)
        }

        items(recentPlayList ?: emptyList()) {
            RecentPlayItemsShort(it)
        }

        item(span = { GridItemSpan(3) }) {
            if (nav.isOnline.value) LocalSongsTop()
        }

        item(span = { GridItemSpan(3) }) {
            OfflineDownloadHeader()
        }

        item(span = { GridItemSpan(3) }) {
            if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                PlaylistList()
                CityRadioViewList()
                TopArtistsList()
                TopGlobalSongsList()
            }
        }

        item(span = { GridItemSpan(3) }) {
            if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                TrendingSongsCountryList()
            }
        }




        item {
            Column {
                Spacer(Modifier.height(120.dp))
            }
        }
    }
}