package com.rizwansayyed.zene.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarLoading
import com.rizwansayyed.zene.ui.home.view.HomeArtistsSimilarToView
import com.rizwansayyed.zene.ui.home.view.HomeHeaderView
import com.rizwansayyed.zene.ui.home.view.HorizontalArtistsView
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.HorizontalVideoView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.AdsBannerView
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.UpgradeToPremiumCard
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import java.util.UUID


@Composable
fun HomeView(homeViewModel: HomeViewModel) {
    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HomeHeaderView()
            }
        }
        item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalSongView(
                    homeViewModel.recommendedPlaylists,
                    Pair(TextSize.BIG, R.string.recommended_playlists),
                    StyleSize.HIDE_AUTHOR, showGrid = false
                )
            }
        }
        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
            }
        }
        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalSongView(
                    homeViewModel.recommendedAlbums,
                    Pair(TextSize.SMALL, R.string.albums_picked_for_you),
                    StyleSize.SHOW_AUTHOR, showGrid = false
                )
            }
        }
        item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
            }
        }
        item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalVideoView(homeViewModel.recommendedVideo, R.string.videos_you_may_like)
            }
        }
        item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
            }
        }
        item(8, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalSongView(
                    homeViewModel.songsYouMayLike,
                    Pair(TextSize.SMALL, R.string.songs_you_may_like),
                    StyleSize.SHOW_AUTHOR,
                    showGrid = true
                )
            }
        }

        item(9, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
                AdsBannerView()
                Spacer(Modifier.height(60.dp))
            }
        }

        item(10, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                UpgradeToPremiumCard()
                HorizontalSongView(
                    homeViewModel.moodList, Pair(TextSize.SMALL, R.string.pick_your_mood),
                    StyleSize.ONLY_TEXT, showGrid = true
                )
            }
        }
        item(11, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
            }
        }
        item(12, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalSongView(
                    homeViewModel.latestReleases, Pair(TextSize.MEDIUM, R.string.latest_release),
                    StyleSize.SHOW_AUTHOR, showGrid = true
                )
            }
        }
        item(13, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
            }
        }
        item(14, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                HorizontalArtistsView(
                    homeViewModel.topMostListeningArtists,
                    Pair(TextSize.SMALL, R.string.global_top_artists), showGrid = true
                )
            }
        }
        item(15, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
                AdsBannerView()
                Spacer(Modifier.height(60.dp))
            }
        }

        when (val v = homeViewModel.favArtistsLists) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> items(
                5,
                key = { 16 },
                span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                HomeArtistsSimilarLoading()
            }

            is APIResponse.Success -> items(
                v.data, { it.artists.id ?: UUID.randomUUID() },
                span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                HomeArtistsSimilarToView(it)
            }
        }

        item(17, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                Spacer(Modifier.height(60.dp))
                AdsBannerView()
                Spacer(Modifier.height(60.dp))
            }
        }

        when (val v = homeViewModel.suggestedSongsForYou) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                item(19, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                        TextPoppinsSemiBold(stringResource(R.string.songs_for_you), size = 15)

                    }
                }

                items(1, key = { UUID.randomUUID() }, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView()
                }
            }

            is APIResponse.Success -> {
                item(19, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                        TextPoppinsSemiBold(stringResource(R.string.songs_for_you), size = 15)

                    }
                }

                items(v.data,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it, v.data)
                }
            }
        }

        item(key = 1000) {
            Spacer(Modifier.height(100.dp))
        }

    }

    LaunchedEffect(Unit) {
        homeViewModel.init()
    }
}