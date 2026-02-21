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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeAIView(homeViewModel: HomeViewModel) {
    val context = LocalContext.current.applicationContext
    val isPremium by isPremiumDB.collectAsState(true)

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.aiMusicList) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item { Spacer(Modifier.height(30.dp)) }

                item {
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()

                    Spacer(Modifier.height(30.dp))
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.new_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalShimmerLoadingCard()

                    Spacer(Modifier.height(30.dp))
                }

            }

            is ResponseResult.Success -> {
                item { Spacer(Modifier.height(30.dp)) }
                if (v.data.trending?.isNotEmpty() == true) item {
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(v.data.trending) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
                        }
                    }
                }


                if (v.data.new?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.new_ai_musics), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.new?.chunked(10) ?: emptyList()) {
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { i, z ->
                            ItemCardView(z)
                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }

                if (v.data.list?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.explore_tunes), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.list?.chunked(10) ?: emptyList()) {
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { i, z ->
                            ItemCardView(z)
                            if (i == 1) NativeViewAdsCard(z?.id)
                        }
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }
        }

        item { Spacer(Modifier.height(300.dp)) }
    }

    LaunchedEffect(Unit) {
        registerEvents(FirebaseEventsParams.AI_MUSIC_PAGE_VIEW)

        homeViewModel.trendingAIMusic {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
    }
}