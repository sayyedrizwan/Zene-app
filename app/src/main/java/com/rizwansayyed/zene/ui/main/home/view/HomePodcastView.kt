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
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.PodcastViewItems
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBorder
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomePodcastView(homeViewModel: HomeViewModel) {
    LazyVerticalGrid(GridCells.Fixed(3), Modifier.fillMaxSize()) {
        when (val v = homeViewModel.homePodcast) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
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
                            items(v.data.latest) {
                                ItemCardView(it)
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

                                }
                            }
                        }
                    }
                }

                if (v.data.podcastYouMayLike?.isNotEmpty() == true) item(span = {
                    GridItemSpan(
                        maxLineSpan
                    )
                }) {
                    Column(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(50.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.podcast_for_you), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data.podcastYouMayLike) {
                                ItemCardView(it)
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
                            .combinedClickable(onLongClick = { NavigationUtils.triggerInfoSheet(it) },
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
}