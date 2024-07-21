package com.rizwansayyed.zene.ui.playlists

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.playlists.view.LoadingAlbumTopView
import com.rizwansayyed.zene.ui.playlists.view.PlaylistAlbumTopView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun PlaylistsView(homeViewModel: HomeViewModel, id: String?, close: () -> Unit) {
    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Row(Modifier.padding(top = 50.dp, start = 8.dp, bottom = 25.dp)) {
                ImageIcon(R.drawable.ic_arrow_left, close)

                Spacer(Modifier.weight(1f))
            }
        }
        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            when (val v = homeViewModel.albumPlaylistData) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> LoadingAlbumTopView()
                is APIResponse.Success -> PlaylistAlbumTopView(v.data.info)
            }
        }

        when (val v = homeViewModel.albumPlaylistData) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {}
            is APIResponse.Success -> {
                item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp, top = 30.dp)) {
                        TextPoppinsSemiBold(stringResource(R.string.songs), size = 15)

                    }
                }

                items(v.data.songs,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it, v.data.songs)
                }
            }
        }

        item(key = 1000, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(100.dp))
        }
    }
    LaunchedEffect(Unit) {
        if (id == null) close()
        else homeViewModel.playlistsData(id)
    }

    BackHandler {
        close()
    }
}