package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntBuzzNewsViewItem
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebDatingView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntCelebStoriesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntDiscoverTopView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLatestTrailerView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLifestyleLoadingView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntLifestyleView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingMoviesView
import com.rizwansayyed.zene.ui.main.ent.discoverview.EntTrendingTopicsView
import com.rizwansayyed.zene.ui.main.ent.discoverview.ViewAllButton
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@Composable
fun EntertainmentDiscoverView(
    entViewModel: EntertainmentViewModel, viewModel: NavigationViewModel
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
            if (v.data.news?.isNotEmpty() == true) item(key = "top") {
                v.data.news.forEachIndexed { i, v ->
                    if (i == 0) EntDiscoverTopView(v, entViewModel)
                }
            }

            item(key = "stories") { EntCelebStoriesView() }

            if (v.data.trends?.isNotEmpty() == true) item(key = "trending") {
                EntTrendingTopicsView(v.data)
            }

            if (v.data.dated?.isNotEmpty() == true) item(key = "dating") {
                EntCelebDatingView(v.data)
            }

            if (v.data.movies?.isNotEmpty() == true) {
                item(key = "movies_") { Spacer(Modifier.height(30.dp)) }
                item(key = "movies") { EntTrendingMoviesView(v.data) }
            }
            item(key = "trailer") { EntLatestTrailerView(v.data) }

            when (val lifestyle = entViewModel.discoverLifeStyle) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item(key = "lifestyle_loading") { EntLifestyleLoadingView() }
                is ResponseResult.Success -> if (lifestyle.data.isNotEmpty())
                    item(key = "lifestyle") { EntLifestyleView(lifestyle.data) }
            }


            item(key = "lifestyle_space") {
                Spacer(Modifier.height(50.dp))
            }


            if (v.data.news?.isNotEmpty() == true) {
                items(
                    v.data.news.drop(1), key = { "news_${it.name}" }) { item ->
                    EntBuzzNewsViewItem(item)
                }

                item(key = "view_all_buzz") {
                    ViewAllButton {
                        viewModel.setEntNavigation(EntSectionSelector.BUZZ)
                    }
                }
            }
        }
    }
}