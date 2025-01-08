package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.ui.main.ent.view.TopSliderVideoNewsView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemYoutubeCardView
import com.rizwansayyed.zene.ui.view.MoviesImageCard
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EntertainmentNewsView() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val ip by ipDB.collectAsState(initial = null)
    val top10MovieString = stringResource(R.string.top_10_movies_show_in)

    LazyColumn(Modifier.fillMaxSize()) {
        when (val v = homeViewModel.entertainmentData) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                Spacer(Modifier.height(50.dp))
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                item {
                    Spacer(Modifier.height(70.dp))
                    Box(Modifier.padding(horizontal = 9.dp)) {
                        TextViewBold(stringResource(R.string.entertainment_), 35)
                    }
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    TopSliderVideoNewsView(v.data.topNews)
                }


                item {
                    Spacer(Modifier.height(70.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.latest_news_videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.latestNews ?: emptyList()) {
                            ItemYoutubeCardView(it)
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(70.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.latest_movie_trailers), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.trailers ?: emptyList()) {
                            ItemYoutubeCardView(it)
                        }
                    }
                }

                when (val m = homeViewModel.entertainmentMoviesData) {
                    is ResponseResult.Success -> {
                        item {
                            Spacer(Modifier.height(70.dp))
                            Box(Modifier.padding(horizontal = 6.dp)) {
                                TextViewBold(stringResource(R.string.movies_trending_today), 23)
                            }
                            Spacer(Modifier.height(12.dp))
                            LazyRow(Modifier.fillMaxWidth()) {
                                items(m.data.trendingMovies ?: emptyList()) {
                                    MoviesImageCard(it)
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(70.dp))
                            Box(Modifier.padding(horizontal = 6.dp)) {
                                TextViewBold("$top10MovieString ${ip?.country}", 23)
                            }
                            Spacer(Modifier.height(12.dp))
                            LazyRow(Modifier.fillMaxWidth()) {
                                itemsIndexed(m.data.topMovies ?: emptyList()) { i, m ->
                                    MoviesImageCard(m, i)
                                }
                            }
                        }
                    }

                    else -> {}
                }

                item {
                    Spacer(Modifier.height(70.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.entertainment_news), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                }

                items(v.data.newsArticles ?: emptyList()) {
                    Row(
                        Modifier
                            .padding(vertical = 20.dp)
                            .fillMaxWidth(),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        GlideImage(
                            it?.thumbnail,
                            it?.name,
                            Modifier
                                .padding(horizontal = 10.dp)
                                .size(100.dp)
                                .clip(RoundedCornerShape(13.dp)),
                            contentScale = ContentScale.Crop
                        )

                        TextViewNormal(it?.name ?: "", 15)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.entertainmentNewsData()
        homeViewModel.entertainmentMovies()
    }
}