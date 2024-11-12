package com.rizwansayyed.zene.ui.playlists

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.playlists.view.LoadingAlbumTopView
import com.rizwansayyed.zene.ui.playlists.view.PlaylistAlbumTopView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.DialogSheetInfos
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SmallButtonBorderText
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.bouncingClickable
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.openSpecificIntent
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun UserPlaylistsView(id: String?, close: () -> Unit) {
    val zeneViewModel: ZeneViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    val context = LocalContext.current as Activity

    var page by remember { mutableIntStateOf(0) }
    val isUserOwner = remember { mutableStateOf(false) }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Row(Modifier.padding(top = 50.dp, start = 8.dp, bottom = 25.dp)) {
                ImageIcon(R.drawable.ic_arrow_left) {
                    close()
                }

                Spacer(Modifier.weight(1f))
            }
        }

        item(2, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
            when (val v = homeViewModel.albumPlaylistData) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> LoadingAlbumTopView()
                is APIResponse.Success ->
                    PlaylistAlbumTopView(v.data.info, zeneViewModel, true, close, isUserOwner)
            }
        }

        items(homeViewModel.userPlaylistsSong, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
            PlaylistFullGridSongs(it, homeViewModel, zeneViewModel, id, isUserOwner)
        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(20.dp))

            if (!homeViewModel.isUserPlaylistLoading && homeViewModel.userPlaylistsSong.isEmpty())
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    TextPoppins(
                        stringResource(R.string.no_song_found_in_this_playlists), true, size = 16
                    )
                }

            if (homeViewModel.showMorePlaylistSongs && !homeViewModel.isUserPlaylistLoading) {
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    SmallButtonBorderText(R.string.load_more) {
                        page += 1
                        id?.let { homeViewModel.userPlaylistsSongs(it, page) }
                    }
                }
            }

            if (homeViewModel.isUserPlaylistLoading) LoadingView(Modifier.size(30.dp))
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(230.dp))
        }
    }

    LaunchedEffect(Unit) {
        if (id == null) close()
        else {
            logEvents(FirebaseLogEvents.FirebaseEvents.STARTED_CREATING_USER_PLAYLISTS_VIEW)
            page = 0
            ShowAdsOnAppOpen(context).interstitialAds()
            homeViewModel.userPlaylists(id)
            homeViewModel.userPlaylistsSongs(id, page)
        }
    }
}

@Composable
fun PlaylistFullGridSongs(
    m: ZeneMusicDataItems,
    homeViewModel: HomeViewModel,
    zeneViewModel: ZeneViewModel,
    playlistID: String?,
    isUserOwner: MutableState<Boolean>
) {
    var dialog by remember { mutableStateOf(false) }
    var removeSongDialog by remember { mutableStateOf(false) }

    Row(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth()
            .bouncingClickable {
                if (it) openSpecificIntent(m, homeViewModel.userPlaylistsSong)
                else dialog = true
            },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            TextPoppins(v = m.name ?: "", false, size = 16)
            Spacer(Modifier.height(4.dp))
            TextPoppins(v = m.artists ?: "", false, size = 13)
        }

        if (isUserOwner.value) ImageIcon(R.drawable.ic_remove_circle, 19) {
            removeSongDialog = true
        }
    }

    if (dialog) DialogSheetInfos(m) {
        dialog = false
    }

    if (removeSongDialog) AlertDialogView(
        R.string.are_you_sure_want_to_remove,
        R.string.are_you_sure_want_to_remove_song_desc,
        R.string.remove
    ) {
        removeSongDialog = false

        if (it) {
            homeViewModel.removePlaylistsSongs(m.id)
            if (playlistID != null && m.id != null)
                zeneViewModel.addRemoveSongFromPlaylists(playlistID, m.id, false)
        }
    }
}