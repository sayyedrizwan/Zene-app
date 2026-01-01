package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.ent.view.EntertainmentScreenTopView
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun EntertainmentNewsView(viewModel: NavigationViewModel) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current.applicationContext

    Column(Modifier.fillMaxSize()) {
        Box(
            Modifier
                .weight(1.2f)
                .fillMaxWidth()
        ) {
            EntertainmentScreenTopView(viewModel)
        }

        Box(
            Modifier
                .weight(8.8f)
                .fillMaxWidth()
        ) {
            EntertainmentDiscoverView()
        }
    }

    LaunchedEffect(Unit) {
//        homeViewModel.homeRecentData {
//            CoroutineScope(Dispatchers.IO).safeLaunch {
//                delay(5.seconds)
//                ProcessPhoenix.triggerRebirth(context)
//            }
//        }
//        homeViewModel.homePodcastData()
    }
}

//@Composable
//fun EntertainmentNewsView() {
//    val navViewModel : NavigationViewModel = hiltViewModel()
//    val homeViewModel: HomeViewModel = hiltViewModel()
//    val ip by ipDB.collectAsState(initial = null)
//    val activity = LocalActivity.current
//    val top10MovieString = stringResource(R.string.top_10_movies_show_in)
//
//    val sponsorAds by sponsorAdsDB.collectAsState(null)
//    val isPremium by isPremiumDB.collectAsState(true)
//
//    var showHollywoodNewsAll by remember { mutableStateOf(false) }
//    var showBollywoodNewsAll by remember { mutableStateOf(false) }
//    var showMusicNewsAll by remember { mutableStateOf(false) }
//
//    LazyColumn(Modifier.fillMaxSize()) {
//        item {
//            Spacer(Modifier.height(70.dp))
//            Box(Modifier.padding(horizontal = 9.dp)) {
//                TextViewBold(stringResource(R.string.entertainment_), 35)
//            }
//        }
//
//        when (val v = homeViewModel.entertainmentData) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> {
//                item {
//                    ShimmerEffect(
//                        Modifier
//                            .padding(top = 10.dp)
//                            .padding(horizontal = 10.dp)
//                            .fillMaxWidth()
//                            .height(240.dp), durationMillis = 1000
//                    )
//                }
//
//                item {
//                    Spacer(Modifier.height(70.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.latest_news_videos), 23)
//                    }
//                    Spacer(Modifier.height(12.dp))
//                    HorizontalShimmerVideoLoadingCard()
//                }
//
//                item {
//                    Spacer(Modifier.height(70.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.latest_movie_trailers), 23)
//                    }
//                    Spacer(Modifier.height(12.dp))
//                    HorizontalShimmerVideoLoadingCard()
//                }
//            }
//
//            is ResponseResult.Success -> {
//                item {
//                    Spacer(Modifier.height(50.dp))
//                    TopSliderVideoNewsView(v.data.topNews)
//                }
//
//                item {
//                    Spacer(Modifier.height(70.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.latest_news_videos), 23)
//                    }
//                    Spacer(Modifier.height(12.dp))
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        itemsIndexed(v.data.latestNews ?: emptyList()) { i, z ->
//                            ItemYoutubeCardView(z)
//                            if (!isPremium) {
//                                if (i == 1) NativeViewAdsCard(z?.id)
//                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                            }
//                        }
//                    }
//                }
//
//                if ((sponsorAds?.bottom?.title?.trim()?.length ?: 0) > 2 ||
//                    sponsorAds?.bottom?.media?.isNotEmpty() == true
//                ) item {
//                    Spacer(Modifier.height(70.dp))
//                    HomeSponsorAdsView(sponsorAds?.entertainment, navViewModel)
//                    Spacer(Modifier.height(12.dp))
//                }
//
//                item {
//                    Spacer(Modifier.height(70.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.latest_movie_trailers), 23)
//                    }
//                    Spacer(Modifier.height(12.dp))
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        itemsIndexed(v.data.trailers ?: emptyList()) { i, z ->
//                            ItemYoutubeCardView(z)
//
//                            if (!isPremium) {
//                                if (i == 1) NativeViewAdsCard(z?.id)
//                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                            }
//                        }
//                    }
//                }
//
//                when (val m = homeViewModel.entertainmentMoviesData) {
//                    is ResponseResult.Success -> {
//                        if (m.data.trendingMovies?.isNotEmpty() == true) item {
//                            Spacer(Modifier.height(70.dp))
//                            Box(Modifier.padding(horizontal = 6.dp)) {
//                                TextViewBold(stringResource(R.string.movies_trending_today), 23)
//                            }
//                            Spacer(Modifier.height(12.dp))
//                            LazyRow(Modifier.fillMaxWidth()) {
//                                itemsIndexed(m.data.trendingMovies) { i, z ->
//                                    MoviesImageCard(z)
//
//                                    if (!isPremium) {
//                                        if (i == 1) NativeViewAdsCard(z?.id)
//                                        if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                    }
//                                }
//                            }
//                        }
//
//                        if (m.data.topMovies?.isNotEmpty() == true) item {
//                            Spacer(Modifier.height(70.dp))
//                            Box(Modifier.padding(horizontal = 6.dp)) {
//                                TextViewBold("$top10MovieString ${ip?.country}", 23)
//                            }
//                            Spacer(Modifier.height(12.dp))
//                            LazyRow(Modifier.fillMaxWidth()) {
//                                itemsIndexed(m.data.topMovies) { i, m ->
//                                    MoviesImageCard(m, i)
//
//                                    if (!isPremium) {
//                                        if (i == 1) NativeViewAdsCard(m?.id)
//                                        if ((i + 1) % 6 == 0) NativeViewAdsCard(m?.id)
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    else -> {}
//                }
//
//                item {
//                    Spacer(Modifier.height(70.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.entertainment_news), 23)
//                    }
//                    Spacer(Modifier.height(12.dp))
//                }
//
//                item {
//                    Spacer(Modifier.height(20.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewNormal(stringResource(R.string.hollywood_news), 19)
//                    }
//                    Spacer(Modifier.height(4.dp))
//                }
//
//                itemsIndexed(
//                    clearArrayIt(v.data.newsArticles?.hollywood, showHollywoodNewsAll)
//                ) { i, z ->
//                    NewsItemCard(z)
//
//                    if (!isPremium) Box(Modifier.fillMaxWidth(), Alignment.Center) {
//                        Row(Modifier.width(250.dp)) {
//                            if (i == 1) NativeViewAdsCard(z?.id)
//                            if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                        }
//                    }
//                }
//
//                item {
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .rotate(if (showHollywoodNewsAll) 180f else 0f)
//                            .clickable { showHollywoodNewsAll = !showHollywoodNewsAll }
//                            .padding(vertical = 5.dp),
//                        Arrangement.Center,
//                        Alignment.CenterVertically
//                    ) {
//                        ImageIcon(R.drawable.ic_arrow_down, 28)
//                    }
//                }
//
//                item {
//                    Spacer(Modifier.height(50.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewNormal(stringResource(R.string.bollywood_news), 19)
//                    }
//                    Spacer(Modifier.height(4.dp))
//                }
//
//                itemsIndexed(
//                    clearArrayIt(v.data.newsArticles?.bollywood, showBollywoodNewsAll)
//                ) { i, z ->
//                    NewsItemCard(z)
//
//                    if (!isPremium) Box(Modifier.fillMaxWidth(), Alignment.Center) {
//                        Row(Modifier.width(250.dp)) {
//                            if (i == 1) NativeViewAdsCard(z?.id)
//                            if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                        }
//                    }
//                }
//
//                item {
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .rotate(if (showBollywoodNewsAll) 180f else 0f)
//                            .clickable { showBollywoodNewsAll = !showBollywoodNewsAll }
//                            .padding(vertical = 5.dp),
//                        Arrangement.Center,
//                        Alignment.CenterVertically
//                    ) {
//                        ImageIcon(R.drawable.ic_arrow_down, 28)
//                    }
//                }
//
//                item {
//                    Spacer(Modifier.height(50.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewNormal(stringResource(R.string.music_news), 19)
//                    }
//                    Spacer(Modifier.height(4.dp))
//                }
//
//                itemsIndexed(
//                    clearArrayIt(v.data.newsArticles?.music, showMusicNewsAll)
//                ) {  i, z ->
//                    NewsItemCard(z)
//
//
//                    if (!isPremium) Box(Modifier.fillMaxWidth(), Alignment.Center) {
//                        Row(Modifier.width(250.dp)) {
//                            if (i == 1) NativeViewAdsCard(z?.id)
//                            if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                        }
//                    }
//                }
//
//                item {
//                    Row(
//                        Modifier
//                            .fillMaxWidth()
//                            .rotate(if (showMusicNewsAll) 180f else 0f)
//                            .clickable { showMusicNewsAll = !showMusicNewsAll }
//                            .padding(vertical = 5.dp),
//                        Arrangement.Center,
//                        Alignment.CenterVertically
//                    ) {
//                        ImageIcon(R.drawable.ic_arrow_down, 28)
//                    }
//                }
//
//            }
//        }
//
//        item {
//            Spacer(Modifier.height(300.dp))
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        homeViewModel.entertainmentNewsData()
//        homeViewModel.entertainmentMovies()
//        activity?.let { InterstitialAdsUtils(it) }
//    }
//}

private fun clearArrayIt(list: List<ZeneMusicData?>?, showAll: Boolean): List<ZeneMusicData?> {
    return (if (showAll) list else list?.take(6)) ?: emptyList()
}