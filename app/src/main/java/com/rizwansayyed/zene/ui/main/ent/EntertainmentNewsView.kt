package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.ui.main.ent.view.TopSliderVideoNewsView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ItemYoutubeCardView
import com.rizwansayyed.zene.ui.view.MoviesImageCard
import com.rizwansayyed.zene.ui.view.NewsItemCard
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun EntertainmentNewsView() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val ip by ipDB.collectAsState(initial = null)
    val top10MovieString = stringResource(R.string.top_10_movies_show_in)

    var showHollywoodNewsAll by remember { mutableStateOf(false) }
    var showBollywoodNewsAll by remember { mutableStateOf(false) }
    var showMusicNewsAll by remember { mutableStateOf(false) }

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

                item {
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewNormal(stringResource(R.string.hollywood_news), 19)
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(clearArrayIt(v.data.newsArticles?.hollywood, showHollywoodNewsAll)) {
                    NewsItemCard(it)
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .rotate(if (showHollywoodNewsAll) 180f else 0f)
                            .clickable { showHollywoodNewsAll = !showHollywoodNewsAll }
                            .padding(vertical = 5.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        ImageIcon(R.drawable.ic_arrow_down, 28)
                    }
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewNormal(stringResource(R.string.bollywood_news), 19)
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(clearArrayIt(v.data.newsArticles?.bollywood, showBollywoodNewsAll)) {
                    NewsItemCard(it)
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .rotate(if (showBollywoodNewsAll) 180f else 0f)
                            .clickable { showBollywoodNewsAll = !showBollywoodNewsAll }
                            .padding(vertical = 5.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        ImageIcon(R.drawable.ic_arrow_down, 28)
                    }
                }

                item {
                    Spacer(Modifier.height(50.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewNormal(stringResource(R.string.music_news), 19)
                    }
                    Spacer(Modifier.height(4.dp))
                }

                items(clearArrayIt(v.data.newsArticles?.music, showMusicNewsAll)) {
                    NewsItemCard(it)
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .rotate(if (showMusicNewsAll) 180f else 0f)
                            .clickable { showMusicNewsAll = !showMusicNewsAll }
                            .padding(vertical = 5.dp),
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        ImageIcon(R.drawable.ic_arrow_down, 28)
                    }
                }

            }
        }

        item {
            Spacer(Modifier.height(300.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.entertainmentNewsData()
        homeViewModel.entertainmentMovies()
    }
}

private fun clearArrayIt(list: List<ZeneMusicData?>?, showAll: Boolean): List<ZeneMusicData?> {
    return (if (showAll) list else list?.take(6)) ?: emptyList()
}