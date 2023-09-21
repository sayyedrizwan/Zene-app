package com.rizwansayyed.zene.presenter.ui

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.rizwansayyed.zene.data.SongDataResponse
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.offline.TopBannerSuggestions
import com.rizwansayyed.zene.presenter.ui.home.online.LocalSongsTop
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayItemsShort
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayList
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel


@Composable
fun MainHomePageView(nav: HomeNavViewModel, room: RoomDbViewModel) {
    val recentPlayList by room.recentSongPlayed.collectAsState(initial = null)
    val offlineSongsViewModel: OfflineSongsViewModel = hiltViewModel()


    val context = LocalContext.current.applicationContext

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
    }
}