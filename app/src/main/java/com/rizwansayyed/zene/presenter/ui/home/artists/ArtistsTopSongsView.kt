package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeItems
import com.rizwansayyed.zene.presenter.ui.home.online.SongsYouMayLikeLoading
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun ArtistsTopSongs() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    when (val v = artistsViewModel.artistsTopSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.top_songs, null) {}

            SongsYouMayLikeLoading(screenWidth)
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.top_songs, null) {}

            LazyHorizontalGrid(
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .height((screenWidth / 1.9 * 2).dp)
            ) {
                itemsIndexed(v.item) { i, item ->
                    SongsYouMayLikeItems(item, screenWidth, homeNav) {
                        addAllPlayer(v.item.toTypedArray(), i)
                    }
                }
            }
        }
    }
}