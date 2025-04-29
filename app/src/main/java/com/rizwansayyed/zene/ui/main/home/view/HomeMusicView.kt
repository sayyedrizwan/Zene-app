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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.datastore.DataStorageManager.sponsorAdsDB
import com.rizwansayyed.zene.ui.main.home.HomeSponsorAdsView
import com.rizwansayyed.zene.ui.main.home.HomeTopHeaderView
import com.rizwansayyed.zene.ui.main.home.topHeaderAlert
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemArtistsCardView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeMusicView(homeViewModel: HomeViewModel) {
    val navViewModel: NavigationViewModel = hiltViewModel()
    var headerText by remember { mutableStateOf("") }
    val coroutine = rememberCoroutineScope()
    val isPremium by isPremiumDB.collectAsState(true)
    val sponsorAds by sponsorAdsDB.collectAsState(null)

    val state = rememberLazyListState()

    LazyColumn(Modifier.fillMaxSize(), state) {
        item { HomeTopHeaderView(headerText) }

        when (val v = homeViewModel.homeRecent) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item {
                    Spacer(Modifier.height(30.dp))
                    HorizontalShimmerLoadingCard()
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.your_mixes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.recommended_playlists), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.albums_for_your_vibe), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.songs_for_you), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()
                }
            }

            is ResponseResult.Success -> {
                if (v.data.topSongs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(v.data.topSongs) { i, z ->
                            ItemCardView(z, v.data.topSongs)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
                        }
                    }
                }


                if ((sponsorAds?.top?.title?.trim()?.length
                        ?: 0) > 2 || sponsorAds?.top?.media?.isNotEmpty() == true
                ) item {
                    Spacer(Modifier.height(50.dp))
                    HomeSponsorAdsView(sponsorAds?.top, navViewModel)
                    Spacer(Modifier.height(12.dp))
                }

                if (v.data.topPlaylists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.your_mixes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(v.data.topPlaylists) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
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
                        itemsIndexed(v.data.playlists) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
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
                        itemsIndexed(v.data.albums) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
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
                        itemsIndexed(v.data.songsYouMayLike) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
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
                        itemsIndexed(v.data.favouriteArtists) { i, z ->
                            if (!isPremium && i == 0) NativeViewAdsCard(z?.id)
                            ItemArtistsCardView(z)
                        }
                    }
                }

                if ((sponsorAds?.bottom?.title?.trim()?.length
                        ?: 0) > 2 || sponsorAds?.bottom?.media?.isNotEmpty() == true
                ) item {
                    Spacer(Modifier.height(50.dp))
                    HomeSponsorAdsView(sponsorAds?.bottom, navViewModel)
                    Spacer(Modifier.height(12.dp))
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
                        itemsIndexed(it) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 5 == 0) NativeViewAdsCard(z?.id)
                            }
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


    LaunchedEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            headerText = topHeaderAlert()
            if (headerText.length > 3) coroutine.launch(Dispatchers.Main) {
                state.animateScrollToItem(0)
            }
        }
    }
}