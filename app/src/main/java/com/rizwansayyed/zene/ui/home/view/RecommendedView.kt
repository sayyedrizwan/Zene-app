package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.SimpleCardsView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.VideoCardsViewWithSong
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun RecommendedPlaylistView(homeViewModel: HomeViewModel) {
    when (val v = homeViewModel.recommendedPlaylists) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppins(stringResource(R.string.recommended_playlists), size = 30)
            }
            LazyRow {
                items(9) {
                    LoadingCardView()
                }
            }
        }

        is APIResponse.Success -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppins(stringResource(R.string.recommended_playlists), size = 30)
            }

            LazyRow {
                items(v.data) {
                    SimpleCardsView(it) {

                    }
                }
            }
        }
    }
}

@Composable
fun RecommendedAlbumsView(homeViewModel: HomeViewModel) {
    when (val v = homeViewModel.recommendedAlbums) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}

        APIResponse.Loading -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppinsSemiBold(stringResource(R.string.albums_picked_for_you), size = 15)
            }
            LazyRow {
                items(9) {
                    LoadingCardView()
                }
            }
        }

        is APIResponse.Success -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppinsSemiBold(stringResource(R.string.albums_picked_for_you), size = 15)
            }

            LazyRow {
                items(v.data) {
                    CardsViewDesc(it) {

                    }
                }
            }
        }
    }
}

@Composable
fun RecommendedVideoView(homeViewModel: HomeViewModel) {
    when (val v = homeViewModel.recommendedVideo) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}

        APIResponse.Loading -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppinsSemiBold(stringResource(R.string.video_you_may_like), size = 15)
            }
            LazyRow {
                items(9) {
                    LoadingCardView()
                }
            }
        }

        is APIResponse.Success -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppinsSemiBold(stringResource(R.string.video_you_may_like), size = 15)
            }

            LazyRow {
                items(v.data) {
                    VideoCardsViewWithSong(it) {

                    }
                }
            }
        }
    }
}