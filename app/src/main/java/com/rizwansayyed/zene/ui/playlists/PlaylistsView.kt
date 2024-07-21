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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.playlists.view.LoadingAlbumTopView
import com.rizwansayyed.zene.ui.playlists.view.PlaylistAlbumTopView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun PlaylistsView(homeViewModel: HomeViewModel, id: String?, close: () -> Unit) {
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