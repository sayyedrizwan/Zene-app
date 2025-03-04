package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemArtistsCardView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun HomeMusicView(homeViewModel: HomeViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeRecent) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                if (v.data.topSongs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.topSongs) {
                            ItemCardView(it, v.data.topSongs)
                        }
                    }
                }

                if (v.data.topPlaylists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.your_mixes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.topPlaylists) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.playlists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_playlists), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.playlists) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.albums?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.albums_for_your_vibe), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.albums) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.songsYouMayLike?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.songs_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.songsYouMayLike) {
                            ItemCardView(it)
                        }
                    }
                }

                if (v.data.favouriteArtists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.your_favourite_artists), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.favouriteArtists) {
                            ItemArtistsCardView(it)
                        }
                    }
                }

                if (v.data.songsToExplore?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.explore_tunes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.songsToExplore?.chunked(10) ?: emptyList()) {
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(it) {
                            ItemCardView(it)
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }
        }

        item { Spacer(Modifier.height(60.dp)) }
        item { HomeLoveTextView() }
        item { Spacer(Modifier.height(300.dp)) }
    }
}