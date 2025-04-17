package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.ui.view.FullVideoCardView
import com.rizwansayyed.zene.ui.view.HorizontalShimmerVideoLoadingCard
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.VideoCardView
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun HomeVideoView(homeViewModel: HomeViewModel) {
    val isPremium by isPremiumDB.collectAsState(true)

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homeVideos) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerVideoLoadingCard()
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_music_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerVideoLoadingCard()
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_films_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerVideoLoadingCard()
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_gaming_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerVideoLoadingCard()
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.videos_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerVideoLoadingCard()
                }
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
                        itemsIndexed(v.data.recommended) { i, z ->
                            Row {
                                VideoCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
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
                        itemsIndexed(v.data.trendingMusic) { i, z ->
                            Row {
                                VideoCardView(z)
                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
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
                        itemsIndexed(it.videos ?: emptyList()) { i, z ->
                            Row {
                                VideoCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
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

                itemsIndexed(v.data.forYou ?: emptyList()) { i, z ->
                    FullVideoCardView(z)

                    if (!isPremium) Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        Row(Modifier.width(250.dp)) {
                            if (i == 1) NativeViewAdsCard(z?.id)
                            if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                        }
                    }
                }
            }
        }
        item {
            Spacer(Modifier.height(300.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeVideosData()
    }
}