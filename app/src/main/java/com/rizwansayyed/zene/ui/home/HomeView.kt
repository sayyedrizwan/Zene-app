package com.rizwansayyed.zene.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarLoading
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarToView
import com.rizwansayyed.zene.ui.home.view.HomeHeaderView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.viewmodel.HomeViewModel


@Composable
fun HomeView() {
    val homeViewModel: HomeViewModel = viewModel()


    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(key = 1) {
            HomeHeaderView()
        }
//        item(key = 2) {
//            HorizontalSongView(
//                homeViewModel.recommendedPlaylists,
//                Pair(TextSize.BIG, R.string.recommended_playlists),
//                StyleSize.HIDE_AUTHOR, showGrid = false
//            )
//        }
//        item(key = 3) {
//            Spacer(Modifier.height(60.dp))
//        }
//        item(key = 4) {
//            HorizontalSongView(
//                homeViewModel.recommendedAlbums,
//                Pair(TextSize.SMALL, R.string.albums_picked_for_you),
//                StyleSize.SHOW_AUTHOR, showGrid = false
//            )
//        }
//        item(key = 5) {
//            Spacer(Modifier.height(60.dp))
//        }
//        item(key = 6) {
//            HorizontalVideoView(homeViewModel.recommendedVideo, R.string.videos_you_may_like)
//        }
//        item(key = 7) {
//            Spacer(Modifier.height(60.dp))
//        }
//        item(key = 8) {
//            HorizontalSongView(
//                homeViewModel.songsYouMayLike,
//                Pair(TextSize.SMALL, R.string.songs_you_may_like),
//                StyleSize.SHOW_AUTHOR,
//                showGrid = true
//            )
//        }
//        item(key = 9) {
//            Spacer(Modifier.height(60.dp))
//        }
//
//
//        item(key = 10) {
//            HorizontalSongView(
//                homeViewModel.moodList, Pair(TextSize.SMALL, R.string.pick_your_mood),
//                StyleSize.ONLY_TEXT, showGrid = true
//            )
//        }
//        item(key = 11) {
//            Spacer(Modifier.height(60.dp))
//        }
//        item(key = 12) {
//            HorizontalSongView(
//                homeViewModel.latestReleases, Pair(TextSize.MEDIUM, R.string.latest_release),
//                StyleSize.SHOW_AUTHOR, showGrid = true
//            )
//        }
//        item(key = 13) {
//            Spacer(Modifier.height(60.dp))
//        }
//        item(key = 14) {
//            HorizontalArtistsView(
//                homeViewModel.topMostListeningArtists,
//                Pair(TextSize.SMALL, R.string.global_top_artists), showGrid = true
//            )
//        }
//        item(key = 15) {
//            Spacer(Modifier.height(60.dp))
//        }

        when (val v = homeViewModel.favArtistsLists) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> items(5) {
                HomeArtistsSimilarLoading()
            }

            is APIResponse.Success -> items(v.data) {
                HomeArtistsSimilarToView(it)
            }
        }
//        item(key = 14) {
//            HomeArtistsSimilarToView(homeViewModel.favArtistsLists)
//        }
        item(key = 16) {
            Spacer(Modifier.height(60.dp))
        }

        item(key = 1000) {
            Spacer(Modifier.height(100.dp))
        }

    }

    LaunchedEffect(Unit) {
        homeViewModel.init()
    }
}