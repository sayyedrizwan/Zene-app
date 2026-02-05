package com.rizwansayyed.zene.ui.main.home.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.ItemCardViewDynamic
import com.rizwansayyed.zene.ui.view.PodcastViewItems
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBorder
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomePodcastView(homeViewModel: HomeViewModel) {
    val isPremium by isPremiumDB.collectAsState(true)
    var categoryTypeSheet by remember { mutableStateOf<ZeneMusicData?>(null) }

    LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homePodcast) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.latest_podcasts), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.podcast_for_you), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }
                }
            }

            is ResponseResult.Success -> {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Spacer(Modifier.height(30.dp))
                }

                items(v.data.history ?: emptyList(), span = { GridItemSpan(1) }) {
                    PodcastViewItems(it)
                }

                if (v.data.latest?.isNotEmpty() == true) item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.latest_podcasts), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.latest) { i, z ->
                                ItemCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }
                }

                if (v.data.trending?.isNotEmpty() == true) item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.trending_podcasts), 23)
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
                }

                if (v.data.categories?.isNotEmpty() == true) item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.categories), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        FlowRow(Modifier.fillMaxWidth()) {
                            v.data.categories.forEach {
                                TextViewBorder(it?.name ?: "") {
                                    categoryTypeSheet = it
                                }
                            }
                        }
                    }
                }

                if (v.data.podcastYouMayLike?.isNotEmpty() == true) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Column(Modifier.fillMaxWidth()) {
                            Spacer(Modifier.height(50.dp))
                            Box(Modifier.padding(horizontal = 6.dp)) {
                                TextViewBold(stringResource(R.string.podcast_for_you), 23)
                            }
                            Spacer(Modifier.height(12.dp))
                            LazyRow(Modifier.fillMaxWidth()) {
                                itemsIndexed(v.data.podcastYouMayLike) { i, z ->
                                    ItemCardView(z)

                                    if (!isPremium) {
                                        if (i == 1) NativeViewAdsCard(z?.id)
                                        if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                    }
                                }
                            }
                        }
                    }
                }

                if (v.data.explore?.isNotEmpty() == true) item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.explore_podcasts), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }


                items(v.data.explore ?: emptyList(), span = { GridItemSpan(1) }) {
                    GlideImage(
                        it?.thumbnail,
                        it?.name,
                        Modifier
                            .combinedClickable(
                                onLongClick = { NavigationUtils.triggerInfoSheet(it) },
                                onClick = { startMedia(it) })
                            .fillMaxWidth()
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.height(300.dp))
        }
    }

    if (categoryTypeSheet != null) CategoryPodcastSheetView(categoryTypeSheet!!) {
        categoryTypeSheet = null
    }

    LaunchedEffect(Unit) {
        homeViewModel.homePodcastData()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPodcastSheetView(data: ZeneMusicData, close: () -> Unit) {
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        val viewModel: HomeViewModel = hiltViewModel(key = data.id)

        LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillMaxWidth()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    TextViewSemiBold(data.name ?: "", 19)
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(10.dp)) }

            when (val v = viewModel.podcastCategories) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                    CircularLoadingView()
                }

                is ResponseResult.Success -> items(v.data) {
                    ItemCardViewDynamic(it, v.data)
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(70.dp)) }
        }

        LaunchedEffect(Unit) { viewModel.podcastCategories(data.name ?: "") }
    }
}