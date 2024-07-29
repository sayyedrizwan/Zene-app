package com.rizwansayyed.zene.ui.artists

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.artists.view.ArtistsNews
import com.rizwansayyed.zene.ui.artists.view.ArtistsSocialButton
import com.rizwansayyed.zene.ui.artists.view.ArtistsTopView
import com.rizwansayyed.zene.ui.artists.view.ArtistsTopViewLoading
import com.rizwansayyed.zene.ui.artists.view.FollowArtists
import com.rizwansayyed.zene.ui.home.view.HorizontalArtistsView
import com.rizwansayyed.zene.ui.home.view.HorizontalNewsView
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.HorizontalVideoView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.CardRoundLoading
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun ArtistsView(viewModel: ZeneViewModel, id: String?, close: () -> Unit) {
    val context = LocalContext.current as Activity

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1) {
            Row(Modifier.padding(top = 50.dp, start = 8.dp, bottom = 25.dp)) {
                ImageIcon(R.drawable.ic_arrow_left) {
                    close()
                }

                Spacer(Modifier.weight(1f))
            }
        }

        when (val v = viewModel.artistsInfo) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                item(300) {
                    ArtistsTopViewLoading()
                }
                item(301) {
                    LazyRow(Modifier.padding(top = 20.dp)) {
                        items(9) {
                            LoadingCardView()
                        }
                    }
                }
            }

            is APIResponse.Success -> {
                item(3) {
                    ArtistsTopView(v.data)
                }
                item(4) {
                    FollowArtists(v.data, viewModel)
                }
                item(5) {
                    ArtistsSocialButton(v.data.socialMedia)
                }
                item(6) {
                    Spacer(Modifier.height(90.dp))

                    if (v.data.topSongs != null) HorizontalSongView(
                        APIResponse.Success(v.data.topSongs.filterNotNull()),
                        Pair(TextSize.SMALL, R.string.top_songs),
                        StyleSize.SONG_WITH_LISTENER, showGrid = true
                    )
                }
            }
        }

        when (val v = viewModel.artistsData) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {}
            is APIResponse.Success -> {
                item(7) {
                    Spacer(Modifier.height(90.dp))

                    if (v.data.news != null) HorizontalNewsView(
                        APIResponse.Success(v.data.news.filterNotNull()),
                        Pair(TextSize.SMALL, R.string.news), showGrid = false
                    )
                }

                item(8) {
                    Spacer(Modifier.height(90.dp))

                    HorizontalVideoView(
                        APIResponse.Success(v.data.videos), R.string.videos
                    )
                }

                item(9) {
                    Spacer(Modifier.height(90.dp))

                    HorizontalSongView(
                        APIResponse.Success(v.data.playlists),
                        Pair(TextSize.SMALL, R.string.playlists),
                        StyleSize.HIDE_AUTHOR, showGrid = true
                    )
                }

                item(10) {
                    Spacer(Modifier.height(90.dp))

                    HorizontalSongView(
                        APIResponse.Success(v.data.albums),
                        Pair(TextSize.SMALL, R.string.albums),
                        StyleSize.SHOW_AUTHOR, showGrid = true
                    )
                }

                item(11) {
                    Spacer(Modifier.height(90.dp))

                    HorizontalSongView(
                        APIResponse.Success(v.data.songs),
                        Pair(TextSize.SMALL, R.string.songs),
                        StyleSize.SHOW_AUTHOR, showGrid = true
                    )
                }

                item(12) {
                    Spacer(Modifier.height(90.dp))

                    HorizontalArtistsView(
                        APIResponse.Success(v.data.artists),
                        Pair(TextSize.SMALL, R.string.similar_artists), showGrid = true
                    )
                }

            }
        }

        item(1000) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        if (id == null) close()
        else {
            ShowAdsOnAppOpen(context).interstitialAds()
            viewModel.artistsInfo(id)
            viewModel.artistsData(id)
        }
    }

    BackHandler {
        close()
    }

}