package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.rizwansayyed.zene.ui.main.ent.view.EntertainmentNearByEventsView
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EntertainmentDiscoverView(
    entViewModel: EntertainmentViewModel, viewModel: NavigationViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(entViewModel.discover) {
        if (entViewModel.discover is ResponseResult.Success) coroutineScope.launch {
            delay(500)
            listState.scrollToItem(index = 0)
        }
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal), listState,
        contentPadding = PaddingValues(bottom = 250.dp)
    ) {
        when (val v = entViewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item {
                    ShimmerEffect(
                        Modifier
                            .fillMaxWidth()
                            .height(550.dp)
                    )
                }

                item { Spacer(Modifier.height(30.dp)) }

                items(6) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ShimmerEffect(
                            Modifier
                                .padding(vertical = 5.dp)
                                .weight(1f)
                                .height(100.dp)
                        )

                        ShimmerEffect(
                            Modifier
                                .padding(vertical = 5.dp)
                                .weight(1f)
                                .height(100.dp)
                        )
                    }
                }
            }

            is ResponseResult.Success -> {
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
                    EntCelebDatingView(v.data, viewModel)
                }

                if (v.data.movies?.isNotEmpty() == true) {
                    item(key = "movies_") { Spacer(Modifier.height(30.dp)) }
                    item(key = "movies") { EntTrendingMoviesView(v.data, viewModel) }
                }
                item(key = "trailer") { EntLatestTrailerView(v.data, viewModel) }

                if (v.data.events?.thisWeek?.isNotEmpty() == true) {
                    item(key = "events_") { Spacer(Modifier.height(30.dp)) }
                    item(key = "events") { EntertainmentNearByEventsView(v.data.events, viewModel) }
                }
            }
        }

        when (val lifestyle = entViewModel.discoverLifeStyle) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item(key = "lifestyle_loading") { EntLifestyleLoadingView() }
            is ResponseResult.Success -> if (lifestyle.data.isNotEmpty()) item(key = "lifestyle") {
                EntLifestyleView(lifestyle.data, viewModel)
            }
        }

        when (val v = entViewModel.discover) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
                item(key = "lifestyle_space") {
                    Spacer(Modifier.height(50.dp))
                }
                if (v.data.news?.isNotEmpty() == true) {
                    items(v.data.news.drop(1), key = { "news_${it.name}" }) { item ->
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
}