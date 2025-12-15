package com.rizwansayyed.zene.ui.main.search

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.ui.main.search.view.SearchBarView
import com.rizwansayyed.zene.ui.main.search.view.SearchKeywordsItemView
import com.rizwansayyed.zene.ui.main.search.view.SearchTopView
import com.rizwansayyed.zene.ui.main.search.view.TrendingItemView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.HorizontalShimmerVideoLoadingCard
import com.rizwansayyed.zene.ui.view.ItemArtistsCardView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.MoviesImageCard
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.VideoCardView
import com.rizwansayyed.zene.utils.MainUtils.addRemoveSearchHistory
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.ads.NativeViewAdsCard
import com.rizwansayyed.zene.utils.ads.nativeAdsAndroidViewMap
import com.rizwansayyed.zene.utils.ads.nativeAdsMap
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchView(homeViewModel: HomeViewModel) {
    val inInfo by DataStorageManager.ipDB.collectAsState(null)
    val activity = LocalActivity.current
    val searchHistory by DataStorageManager.searchHistoryDB.collectAsState(emptyArray())
    var showSearch by remember { mutableStateOf("") }
    val search = remember { mutableStateOf("") }
    var job by remember { mutableStateOf<Job?>(null) }
    val coroutine = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val searchHistoryRemoved = stringResource(R.string.removed_search_history)
    val isPremium by isPremiumDB.collectAsState(true)

    val state = rememberLazyListState()


    LaunchedEffect(homeViewModel.searchKeywords) {
        state.animateScrollToItem(0)
    }

    LazyColumn(Modifier.fillMaxWidth(), state) {
        item {
            SearchTopView {
                search.value = it
                showSearch = it
            }
        }

        stickyHeader {
            SearchBarView(search, showSearch) {
                showSearch = it
            }

            LaunchedEffect(search.value) {
                job?.cancel()
                job = coroutine.safeLaunch {
                    delay(500)
                    if (showSearch != search.value) homeViewModel.searchKeywordsData(search.value)
                }
            }
        }

        when (val v = homeViewModel.searchTrending) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                LazyRow(Modifier.fillMaxWidth()) {
                    items(9) {
                        ShimmerEffect(
                            Modifier
                                .padding(horizontal = 3.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.Black)
                                .size(100.dp, 50.dp)
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                    }
                }
            }

            is ResponseResult.Success -> {
                item {
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.keywords ?: emptyList()) { txt ->
                            TrendingItemView(txt?.name, R.drawable.ic_chart_line) {
                                search.value = txt?.name ?: ""
                                showSearch = txt?.name ?: ""
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(20.dp)) }

        if (searchHistory?.isNotEmpty() == true && showSearch.trim().length < 3) {
            item {
                LazyRow(Modifier.fillMaxWidth()) {
                    items(searchHistory!!) { txt ->
                        TrendingItemView(txt, R.drawable.ic_go_backward) {
                            if (it) showSearch = txt
                            else {
                                addRemoveSearchHistory(txt, false)
                                SnackBarManager.showMessage(searchHistoryRemoved)
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }
        }

        when (val v = homeViewModel.searchKeywords) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                items(v.data) { txt ->
                    SearchKeywordsItemView(txt) {
                        if (it) {
                            search.value = txt
                            showSearch = txt
                        } else search.value = txt
                    }
                }

                if (v.data.isNotEmpty()) item { Spacer(Modifier.height(50.dp)) }
            }
        }

        if (showSearch.trim().length > 3) {
            when (val v = homeViewModel.searchData) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> {
                    item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.songs), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.artists), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.videos), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerVideoLoadingCard()
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.albums), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }

                    item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.radios), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalShimmerLoadingCard()
                    }
                }

                is ResponseResult.Success -> {
                    if (v.data.songs?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.songs), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.songs) { i, z ->
                                ItemCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }

                    if (v.data.artists?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.artists), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.artists) { i, z ->
                                ItemArtistsCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }


                    if (v.data.videos?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.videos), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.videos) { i, z ->
                                VideoCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }


                    if (v.data.albums?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.albums), 23)
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


                    if (v.data.playlists?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.playlists), 23)
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

                    if (v.data.radio?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.radios), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.radio) { i, z ->
                                ItemCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }


                    if (v.data.aiSongs?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.ai_music), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.aiSongs) { i, z ->
                                ItemCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }

                    if (v.data.podcast?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.podcasts), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.podcast) { i, z ->
                                ItemCardView(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }

                    if (v.data.movies?.isNotEmpty() == true) item {
                        Spacer(Modifier.height(30.dp))
                        Box(Modifier.padding(horizontal = 6.dp)) {
                            TextViewBold(stringResource(R.string.movies), 23)
                        }
                        Spacer(Modifier.height(12.dp))
                        LazyRow(Modifier.fillMaxWidth()) {
                            itemsIndexed(v.data.movies) { i, z ->
                                MoviesImageCard(z)

                                if (!isPremium) {
                                    if (i == 1) NativeViewAdsCard(z?.id)
                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                                }
                            }
                        }
                    }

                }
            }
        }


        if (showSearch.trim().length <= 3) when (val v = homeViewModel.searchTrending) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {
                item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.global_trending_songs), 19)
                    }

                    HorizontalShimmerLoadingCard()
                }
            }

            is ResponseResult.Success -> {
                if (v.data.globalSongs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.global_trending_songs), 19)
                    }
                }

                items(v.data.globalSongs?.chunked(25) ?: emptyList()) {
                    Spacer(Modifier.height(15.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }

                if (v.data.songs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {

                        TextViewBold(
                            String.format(
                                Locale.getDefault(),
                                stringResource(R.string.trending_songs),
                                inInfo?.country
                            ),
                            19
                        )
                    }
                }

                items(v.data.songs?.chunked(25) ?: emptyList()) {
                    Spacer(Modifier.height(15.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { i, z ->
                            ItemCardView(z)

                            if (!isPremium) {
                                if (i == 1) NativeViewAdsCard(z?.id)
                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }

                if (v.data.artists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.trending_artists), 19)
                    }
                }

                items(v.data.artists?.chunked(25) ?: emptyList()) {
                    Spacer(Modifier.height(15.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        itemsIndexed(it) { i, z ->
                            if (i == 0 && !isPremium) NativeViewAdsCard(z?.id)
                            ItemArtistsCardView(z)
                        }
                    }
                }
            }
        }

        item { Spacer(Modifier.height(320.dp)) }
    }

    LaunchedEffect(showSearch) {
        focusManager.clearFocus()
        homeViewModel.clearSearchKeywordsSuggestions()
        homeViewModel.searchZene(showSearch)
        if (showSearch.trim().length > 3) {
            activity?.let { InterstitialAdsUtils(it) }
            addRemoveSearchHistory(showSearch)
            nativeAdsMap.clear()
            nativeAdsAndroidViewMap.clear()
        }
    }
    LaunchedEffect(Unit) {
        homeViewModel.searchTrendingData()
    }
}