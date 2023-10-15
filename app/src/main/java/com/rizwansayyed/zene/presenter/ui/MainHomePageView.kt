package com.rizwansayyed.zene.presenter.ui

import android.graphics.drawable.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.offline.TopBannerSuggestions
import com.rizwansayyed.zene.presenter.ui.home.online.CityRadioViewList
import com.rizwansayyed.zene.presenter.ui.home.online.CurrentMostPlayingSong
import com.rizwansayyed.zene.presenter.ui.home.online.FreshAddedSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalSongsItemsFull
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalSongsViewItems
import com.rizwansayyed.zene.presenter.ui.home.online.LocalSongsTop
import com.rizwansayyed.zene.presenter.ui.home.online.PlaylistList
import com.rizwansayyed.zene.presenter.ui.home.online.SelectFavArtists
import com.rizwansayyed.zene.presenter.ui.home.online.SongYouMayTitle
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsCountryList
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsList
import com.rizwansayyed.zene.presenter.ui.home.online.TopGlobalSongsList
import com.rizwansayyed.zene.presenter.ui.home.online.TrendingSongsCountryList
import com.rizwansayyed.zene.presenter.ui.home.view.OfflineDownloadHeader
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayItemsShort
import com.rizwansayyed.zene.presenter.ui.home.view.RecentPlayList
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.FlowPreview


@OptIn(FlowPreview::class)
@Composable
fun MainHomePageView(
    nav: HomeNavViewModel, room: RoomDbViewModel, home: HomeApiViewModel, jsoup: JsoupScrapViewModel
) {
    val recentPlayList by room.recentSongPlayed.collectAsState(initial = emptyList())
    val selectedFavouriteArtists by selectedFavouriteArtistsSongs.collectAsState(initial = null)
    val noMore = stringResource(id = R.string.can_just_select_six_artists_song_suggestions)

    val columnModifier = Modifier
        .fillMaxSize()
        .background(DarkBlack)

    LazyVerticalGrid(columns = GridCells.Fixed(2), columnModifier) {
        item(span = { GridItemSpan(2) }) {
            HomepageTopView()
        }

        item(span = { GridItemSpan(2) }) {
            if (nav.isOnline.value)
                Column {
                    CurrentMostPlayingSong()
                    RecentPlayList(recentPlayList)
                }
            else
                TopBannerSuggestions()
        }

        items(recentPlayList) {
            RecentPlayItemsShort(it)
        }

        item(span = { GridItemSpan(2) }) {
            if (nav.isOnline.value) LocalSongsTop()
        }

        item(span = { GridItemSpan(2) }) {
            OfflineDownloadHeader()
        }

        item(span = { GridItemSpan(2) }) {
            if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                PlaylistList()
                CityRadioViewList()
                TopArtistsList()
                TopGlobalSongsList()
            }
        }

        item(span = { GridItemSpan(2) }) {
            if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                TrendingSongsCountryList()
                FreshAddedSongsList()
                TopArtistsCountryList()
                SongYouMayTitle(recentPlayList, selectedFavouriteArtists)
            }
        }

        if (recentPlayList.isEmpty() && selectedFavouriteArtists?.isEmpty() == true) {

        } else
            when (val v = room.songsYouMayLike) {
                DataResponse.Empty -> {}
                is DataResponse.Error -> {}
                DataResponse.Loading -> item(span = { GridItemSpan(2) }) { LoadingStateBar() }
                is DataResponse.Success -> {
                    items(v.item) {
                        GlobalSongsViewItems(it)
                    }
                }
            }

        when (val v = room.albumsYouMayLike) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading -> item(span = { GridItemSpan(2) }) {
                if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                    TopInfoWithSeeMore(R.string.albums_you_may_like, null) {}

                    LoadingStateBar()
                }
            }

            is DataResponse.Success -> {
                item(span = { GridItemSpan(2) }) {
                    if (nav.isOnline.value) Column(Modifier.fillMaxWidth()) {
                        TopInfoWithSeeMore(R.string.albums_you_may_like, null) {}
                    }
                }

                items(v.item) {
                    AlbumsViewItems(it)
                }
            }
        }
        item {
            Column {
                Spacer(Modifier.height(320.dp))
            }
        }
    }
}


@Composable
fun AlbumsViewItems(items: MusicData?) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .padding(horizontal = 4.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Box {
            AsyncImage(
                items?.thumbnail, "",
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(17.dp)), contentScale = ContentScale.Crop
            )


            Image(
                painterResource(id = R.drawable.ic_vanillamusic),
                "",
                Modifier
                    .padding(9.dp)
                    .align(Alignment.BottomEnd)
                    .size(30.dp),
                colorFilter = ColorFilter.tint(Color.Blue)
            )
        }

        SongsTitleAndArtistsSmall(
            items?.name ?: "",
            items?.artists ?: "",
            Modifier
                .width(170.dp)
                .padding(3.dp),
            true
        )

    }

}
