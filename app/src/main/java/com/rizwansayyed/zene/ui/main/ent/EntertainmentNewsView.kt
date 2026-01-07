package com.rizwansayyed.zene.ui.main.ent

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebDatingView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebStoriesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntDiscoverTopView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLatestTrailerView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLifestyleView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingTopicsView
import com.rizwansayyed.zene.ui.main.ent.discoverview.NewsRow
import com.rizwansayyed.zene.ui.main.ent.discoverview.ViewAllButton
import com.rizwansayyed.zene.ui.main.ent.discoverview.sampleNews
import com.rizwansayyed.zene.ui.main.ent.view.EntertainmentScreenTopView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun EntertainmentNewsView(viewModel: NavigationViewModel) {
    val entViewModel: EntertainmentViewModel = hiltViewModel()
    val context = LocalActivity.current

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
            when (val v = entViewModel.discover) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentPadding = PaddingValues(bottom = 250.dp)
                ) {
                    item {
                        ShimmerEffect(
                            Modifier
                                .fillMaxWidth()
                                .height(550.dp)
                        )
                    }
                }

                is ResponseResult.Success -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal),
                    contentPadding = PaddingValues(bottom = 250.dp)
                ) {
                    item(key = "top") {
                        v.data.news?.forEachIndexed { i, v ->
                            if (i == 0) EntDiscoverTopView(v, entViewModel)
                        }
                    }

                    item(key = "stories") { EntCelebStoriesView() }

                    item(key = "trending") { EntTrendingTopicsView(v.data) }

                    item(key = "dating") { EntCelebDatingView() }

                    item(key = "movies") { EntTrendingMoviesView() }

                    item(key = "trailer") { EntLatestTrailerView() }

                    item(key = "lifestyle") { EntLifestyleView() }

                    items(sampleNews, key = { it.title }) { item ->
                        NewsRow(item)
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    item(key = "view_all_buzz") {
                        ViewAllButton {

                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        entViewModel.entDiscoverNews {
            CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(5.seconds)
                ProcessPhoenix.triggerRebirth(context)
            }
        }
        context?.let { InterstitialAdsUtils(it) }
    }

    DisposableEffect(Unit) {
        entViewModel.startReadersCounter()
        onDispose {
            entViewModel.stopReadersCounter()
        }
    }
}