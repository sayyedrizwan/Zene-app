package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItems
import com.rizwansayyed.zene.presenter.ui.home.online.ArtistsLoadingCards
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsItems
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ArtistsAlbumsList() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.searchData) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.albums, null) {}
            FlowRow {
                repeat(8) {
                    Box(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2)) {
                        LoadingAlbumsCards()
                    }
                }
            }
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.albums, null) {}
            FlowRow {
                v.item?.albums?.forEach {
                    Box(Modifier.width(LocalConfiguration.current.screenWidthDp.dp / 2)) {
                        AlbumsItems(it)
                    }
                }
            }
        }
    }
}


@Composable
fun ArtistsLatestSongs() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.searchData) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.latest_songs, null) {}

            repeat(6) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 7.dp)
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shimmerBrush())
                        .padding(5.dp)
                )
            }
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.latest_songs, null) {}
            v.item?.songs?.forEachIndexed { i, music ->
                GlobalTrendingPagerItems(music, false) {
                    Utils.addAllPlayer(v.item.songs.toTypedArray(), i)
                }
            }
        }
    }
}

@Composable
fun ArtistsSimilarList() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp


    when (val v = artistsViewModel.searchData) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.similar_artists, null) {}

            ArtistsLoadingCards(screenWidth)
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.similar_artists, null) {}

            LazyHorizontalGrid(
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .height((screenWidth / 1.8 * 2).dp)
            ) {
                items(v.item?.artists ?: emptyList()) {
                    TopArtistsItems(it, screenWidth) {
                        if (homeNav.selectedArtists.lowercase().trim() !=
                            it.name?.lowercase()?.trim()
                        ) homeNav.setArtists(it.name ?: "")
                    }
                }
            }
        }
    }
}