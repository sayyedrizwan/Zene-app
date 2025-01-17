package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.FullVideoCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.VideoCardView
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun HomeVideoView(homeViewModel: HomeViewModel) {
    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeVideos) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                if (v.data.recommended?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyHorizontalGrid(
                        GridCells.Fixed(2),
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 440.dp)
                    ) {
                        items(v.data.recommended) {
                            VideoCardView(it)
                        }
                    }
                }


                if (v.data.trendingMusic?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_music_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyHorizontalGrid(
                        GridCells.Fixed(2),
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 440.dp)
                    ) {
                        items(v.data.trendingMusic) {
                            VideoCardView(it)
                        }
                    }
                }


                if (v.data.trendingFilm?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_films_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyHorizontalGrid(
                        GridCells.Fixed(2),
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 440.dp)
                    ) {
                        items(v.data.trendingFilm) {
                            VideoCardView(it)
                        }
                    }
                }


                if (v.data.trendingGaming?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_gaming_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyHorizontalGrid(
                        GridCells.Fixed(2),
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 440.dp)
                    ) {
                        items(v.data.trendingGaming) {
                            VideoCardView(it)
                        }
                    }
                }

                if (v.data.suggestions?.isNotEmpty() == true) items(v.data.suggestions) {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(it.name ?: "", 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyHorizontalGrid(
                        GridCells.Fixed(2),
                        Modifier
                            .fillMaxWidth()
                            .heightIn(max = 440.dp)
                    ) {
                        items(it.videos ?: emptyList()) {
                            VideoCardView(it)
                        }
                    }
                }

                if (v.data.forYou?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.videos_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.forYou ?: emptyList()) {
                    FullVideoCardView(it)
                }
            }
        }
        item {
            Spacer(Modifier.height(150.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeVideosData()
    }
}