package com.rizwansayyed.zene.ui.main.search

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun SearchView(homeViewModel: HomeViewModel) {

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchViewzz(homeViewModel: HomeViewModel) {
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

    // Scroll to top when search keywords change
    LaunchedEffect(homeViewModel.searchKeywords) {
        state.animateScrollToItem(0)
    }

    LazyColumn(Modifier.fillMaxWidth(), state) {
        // Search header and bar
        SearchHeaderSection(
            search = search,
            showSearch = showSearch,
            onSearchChange = { showSearch = it },
            coroutine = coroutine,
            job = job,
            onJobUpdate = { job = it },
            homeViewModel = homeViewModel
        )

        // Trending keywords section
        TrendingKeywordsSection(homeViewModel.searchTrending, search, onSearchUpdate = { showSearch = it })

        item { Spacer(Modifier.height(20.dp)) }

        // Search history section
        SearchHistorySection(
            searchHistory = searchHistory,
            showSearch = showSearch,
            onSearchSelect = { showSearch = it },
            searchHistoryRemoved = searchHistoryRemoved
        )

        // Search suggestions section
        SearchSuggestionsSection(homeViewModel.searchKeywords, search, onSearchUpdate = { showSearch = it })

        // Main search results or trending content
        if (showSearch.trim().length > 3) {
            SearchResultsSection(homeViewModel.searchData, isPremium)
        } else {
            TrendingContentSection(homeViewModel.searchTrending, inInfo, isPremium)
        }

        item { Spacer(Modifier.height(320.dp)) }
    }

    // Search effects
    SearchEffects(
        showSearch = showSearch,
        focusManager = focusManager,
        homeViewModel = homeViewModel,
        activity = activity
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyListScope.SearchHeaderSection(
    search: MutableState<String>,
    showSearch: String,
    onSearchChange: (String) -> Unit,
    coroutine: CoroutineScope,
    job: Job?,
    onJobUpdate: (Job?) -> Unit,
    homeViewModel: HomeViewModel
) {
    item {
        SearchTopView {
            search.value = it
            onSearchChange(it)
        }
    }

    stickyHeader {
        SearchBarView(search, showSearch) {
            onSearchChange(it)
        }

        LaunchedEffect(search.value) {
            onJobUpdate(job?.apply { cancel() })
            onJobUpdate(coroutine.launch {
                delay(500)
                if (showSearch != search.value) {
                    homeViewModel.searchKeywordsData(search.value)
                }
            })
        }
    }
}

@Composable
private fun LazyListScope.TrendingKeywordsSection(
    searchTrending: ResponseResult<*>,
    search: MutableState<String>,
    onSearchUpdate: (String) -> Unit
) {
    when (searchTrending) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {
            item {
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
        }
        is ResponseResult.Success -> {
            item {
                LazyRow(Modifier.fillMaxWidth()) {
                    items(searchTrending.data.keywords ?: emptyList()) { txt ->
                        TrendingItemView(txt?.name, R.drawable.ic_chart_line) {
                            val name = txt?.name ?: ""
                            search.value = name
                            onSearchUpdate(name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListScope.SearchHistorySection(
    searchHistory: Array<String>?,
    showSearch: String,
    onSearchSelect: (String) -> Unit,
    searchHistoryRemoved: String
) {
    if (searchHistory?.isNotEmpty() == true && showSearch.trim().length < 3) {
        item {
            LazyRow(Modifier.fillMaxWidth()) {
                items(searchHistory) { txt ->
                    TrendingItemView(txt, R.drawable.ic_go_backward) { isSelect ->
                        if (isSelect) {
                            onSearchSelect(txt)
                        } else {
                            addRemoveSearchHistory(txt, false)
                            SnackBarManager.showMessage(searchHistoryRemoved)
                        }
                    }
                }
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
    }
}

@Composable
private fun LazyListScope.SearchSuggestionsSection(
    searchKeywords: ResponseResult<*>,
    search: MutableState<String>,
    onSearchUpdate: (String) -> Unit
) {
    when (searchKeywords) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {
            item { CircularLoadingView() }
        }
        is ResponseResult.Success -> {
            items(searchKeywords.data) { txt ->
                SearchKeywordsItemView(txt) { isDirectSearch ->
                    if (isDirectSearch) {
                        search.value = txt
                        onSearchUpdate(txt)
                    } else {
                        search.value = txt
                    }
                }
            }
            if (searchKeywords.data.isNotEmpty()) {
                item { Spacer(Modifier.height(50.dp)) }
            }
        }
    }
}

@Composable
private fun LazyListScope.SearchResultsSection(
    searchData: ResponseResult<*>,
    isPremium: Boolean
) {
    when (searchData) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> {
            SearchResultsLoadingSection()
        }
        is ResponseResult.Success -> {
            SearchResultsSuccessSection(searchData.data, isPremium)
        }
    }
}

@Composable
private fun LazyListScope.SearchResultsLoadingSection() {
    val sections = listOf(
        R.string.songs to { HorizontalShimmerLoadingCard() },
        R.string.artists to { HorizontalShimmerLoadingCard() },
        R.string.videos to { HorizontalShimmerVideoLoadingCard() },
        R.string.albums to { HorizontalShimmerLoadingCard() }
    )

    sections.forEach { (titleRes, shimmerContent) ->
        item {
            Spacer(Modifier.height(30.dp))
            Box(Modifier.padding(horizontal = 6.dp)) {
                TextViewBold(stringResource(titleRes), 23)
            }
            Spacer(Modifier.height(12.dp))
            shimmerContent()
        }
    }
}

@Composable
private fun LazyListScope.SearchResultsSuccessSection(
    data: Any, // Replace with your actual data type
    isPremium: Boolean
) {
    // Songs section
    if (data.songs?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.songs),
            items = data.songs,
            isPremium = isPremium
        ) { index, item ->
            ItemCardView(item)
        }
    }

    // Artists section
    if (data.artists?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.artists),
            items = data.artists,
            isPremium = isPremium
        ) { index, item ->
            ItemArtistsCardView(item)
        }
    }

    // Videos section
    if (data.videos?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.videos),
            items = data.videos,
            isPremium = isPremium
        ) { index, item ->
            VideoCardView(item)
        }
    }

    // Albums section
    if (data.albums?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.albums),
            items = data.albums,
            isPremium = isPremium
        ) { index, item ->
            ItemCardView(item)
        }
    }

    // Playlists section
    if (data.playlists?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.playlists),
            items = data.playlists,
            isPremium = isPremium
        ) { index, item ->
            ItemCardView(item)
        }
    }

    // AI Songs section
    if (data.aiSongs?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.ai_music),
            items = data.aiSongs,
            isPremium = isPremium
        ) { index, item ->
            ItemCardView(item)
        }
    }

    // News section
    if (data.news?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.news),
            items = data.news,
            isPremium = isPremium
        ) { index, item ->
            VideoCardView(item)
        }
    }

    // Podcast section
    if (data.podcast?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.podcasts),
            items = data.podcast,
            isPremium = isPremium
        ) { index, item ->
            ItemCardView(item)
        }
    }

    // Movies section
    if (data.movies?.isNotEmpty() == true) {
        SearchResultCategorySection(
            title = stringResource(R.string.movies),
            items = data.movies,
            isPremium = isPremium
        ) { index, item ->
            MoviesImageCard(item)
        }
    }
}

@Composable
private fun LazyListScope.SearchResultCategorySection(
    title: String,
    items: List<Any>, // Replace with your actual item type
    isPremium: Boolean,
    itemContent: @Composable (Int, Any) -> Unit
) {
    item {
        Spacer(Modifier.height(30.dp))
        Box(Modifier.padding(horizontal = 6.dp)) {
            TextViewBold(title, 23)
        }
        Spacer(Modifier.height(12.dp))
        LazyRow(Modifier.fillMaxWidth()) {
            itemsIndexed(items) { index, item ->
                itemContent(index, item)

                // Add native ads for non-premium users with proper key generation
                if (!isPremium) {
                    val adKey = "${title}_${item?.id}_$index" // Create unique key
                    if (index == 1) {
                        key(adKey + "_first") {
                            NativeViewAdsCard(item?.id)
                        }
                    }
                    if ((index + 1) % 6 == 0) {
                        key(adKey + "_recurring") {
                            NativeViewAdsCard(item?.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LazyListScope.TrendingContentSection(
    searchTrending: ResponseResult<*>,
    inInfo: Any?, // Replace with your actual IP info type
    isPremium: Boolean
) {
    when (searchTrending) {
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
            TrendingContentSuccessSection(searchTrending.data, inInfo, isPremium)
        }
    }
}

@Composable
private fun LazyListScope.TrendingContentSuccessSection(
    data: Any, // Replace with your actual data type
    inInfo: Any?, // Replace with your actual IP info type
    isPremium: Boolean
) {
    // Global trending songs
    if (data.globalSongs?.isNotEmpty() == true) {
        item {
            Spacer(Modifier.height(30.dp))
            Box(Modifier.padding(horizontal = 6.dp)) {
                TextViewBold(stringResource(R.string.global_trending_songs), 19)
            }
        }

        items(data.globalSongs.chunked(25)) { chunk ->
            Spacer(Modifier.height(15.dp))
            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(chunk) { index, item ->
                    ItemCardView(item)

                    if (!isPremium) {
                        val adKey = "global_songs_${item?.id}_$index"
                        if (index == 1) {
                            key(adKey + "_first") {
                                NativeViewAdsCard(item?.id)
                            }
                        }
                        if ((index + 1) % 6 == 0) {
                            key(adKey + "_recurring") {
                                NativeViewAdsCard(item?.id)
                            }
                        }
                    }
                }
            }
        }
    }

    item { Spacer(Modifier.height(20.dp)) }

    // Local trending songs
    if (data.songs?.isNotEmpty() == true) {
        item {
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

        items(data.songs.chunked(25)) { chunk ->
            Spacer(Modifier.height(15.dp))
            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(chunk) { index, item ->
                    ItemCardView(item)

                    if (!isPremium) {
                        val adKey = "local_songs_${item?.id}_$index"
                        if (index == 1) {
                            key(adKey + "_first") {
                                NativeViewAdsCard(item?.id)
                            }
                        }
                        if ((index + 1) % 6 == 0) {
                            key(adKey + "_recurring") {
                                NativeViewAdsCard(item?.id)
                            }
                        }
                    }
                }
            }
        }
    }

    item { Spacer(Modifier.height(20.dp)) }

    // Trending artists
    if (data.artists?.isNotEmpty() == true) {
        item {
            Spacer(Modifier.height(30.dp))
            Box(Modifier.padding(horizontal = 6.dp)) {
                TextViewBold(stringResource(R.string.trending_artists), 19)
            }
        }

        items(data.artists.chunked(25)) { chunk ->
            Spacer(Modifier.height(15.dp))
            LazyRow(Modifier.fillMaxWidth()) {
                itemsIndexed(chunk) { index, item ->
                    if (index == 0 && !isPremium) {
                        key("artists_ad_${item?.id}") {
                            NativeViewAdsCard(item?.id)
                        }
                    }
                    ItemArtistsCardView(item)
                }
            }
        }
    }
}

@Composable
private fun SearchEffects(
    showSearch: String,
    focusManager: FocusManager,
    homeViewModel: HomeViewModel,
    activity: Activity?
) {
    LaunchedEffect(showSearch) {
        focusManager.clearFocus()
        homeViewModel.clearSearchKeywordsSuggestions()
        homeViewModel.searchZene(showSearch)

        if (showSearch.trim().length > 3) {
            activity?.let { InterstitialAdsUtils(it) }
            addRemoveSearchHistory(showSearch)

            // Clear native ads maps to prevent view parent conflicts
            try {
                nativeAdsMap.clear()
                nativeAdsAndroidViewMap.clear()
            } catch (e: Exception) {
                // Handle any clearing exceptions silently
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.searchTrendingData()
    }
}



//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun SearchView(homeViewModel: HomeViewModel) {
//    val inInfo by DataStorageManager.ipDB.collectAsState(null)
//    val activity = LocalActivity.current
//    val searchHistory by DataStorageManager.searchHistoryDB.collectAsState(emptyArray())
//    var showSearch by remember { mutableStateOf("") }
//    val search = remember { mutableStateOf("") }
//    var job by remember { mutableStateOf<Job?>(null) }
//    val coroutine = rememberCoroutineScope()
//    val focusManager = LocalFocusManager.current
//    val searchHistoryRemoved = stringResource(R.string.removed_search_history)
//    val isPremium by isPremiumDB.collectAsState(true)
//
//    val state = rememberLazyListState()
//
//
//    LaunchedEffect(homeViewModel.searchKeywords) {
//        state.animateScrollToItem(0)
//    }
//
//    LazyColumn(Modifier.fillMaxWidth(), state) {
//        item {
//            SearchTopView {
//                search.value = it
//                showSearch = it
//            }
//        }
//
//        stickyHeader {
//            SearchBarView(search, showSearch) {
//                showSearch = it
//            }
//
//            LaunchedEffect(search.value) {
//                job?.cancel()
//                job = coroutine.launch {
//                    delay(500)
//                    if (showSearch != search.value) homeViewModel.searchKeywordsData(search.value)
//                }
//            }
//        }
//
//        when (val v = homeViewModel.searchTrending) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> item {
//                LazyRow(Modifier.fillMaxWidth()) {
//                    items(9) {
//                        ShimmerEffect(
//                            Modifier
//                                .padding(horizontal = 3.dp)
//                                .clip(RoundedCornerShape(14.dp))
//                                .background(Color.Black)
//                                .size(100.dp, 50.dp)
//                                .padding(horizontal = 8.dp, vertical = 6.dp)
//                                .clip(RoundedCornerShape(14.dp))
//                        )
//                    }
//                }
//            }
//
//            is ResponseResult.Success -> {
//                item {
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        items(v.data.keywords ?: emptyList()) { txt ->
//                            TrendingItemView(txt?.name, R.drawable.ic_chart_line) {
//                                search.value = txt?.name ?: ""
//                                showSearch = txt?.name ?: ""
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        item { Spacer(Modifier.height(20.dp)) }
//
//        if (searchHistory?.isNotEmpty() == true && showSearch.trim().length < 3) {
//            item {
//                LazyRow(Modifier.fillMaxWidth()) {
//                    items(searchHistory!!) { txt ->
//                        TrendingItemView(txt, R.drawable.ic_go_backward) {
//                            if (it) showSearch = txt
//                            else {
//                                addRemoveSearchHistory(txt, false)
//                                SnackBarManager.showMessage(searchHistoryRemoved)
//                            }
//                        }
//                    }
//                }
//            }
//
//            item { Spacer(Modifier.height(20.dp)) }
//        }
//
//        when (val v = homeViewModel.searchKeywords) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> item {
//                CircularLoadingView()
//            }
//
//            is ResponseResult.Success -> {
//                items(v.data) { txt ->
//                    SearchKeywordsItemView(txt) {
//                        if (it) {
//                            search.value = txt
//                            showSearch = txt
//                        } else search.value = txt
//                    }
//                }
//
//                if (v.data.isNotEmpty()) item { Spacer(Modifier.height(50.dp)) }
//            }
//        }
//
//        if (showSearch.trim().length > 3) {
//            when (val v = homeViewModel.searchData) {
//                ResponseResult.Empty -> {}
//                is ResponseResult.Error -> {}
//                ResponseResult.Loading -> {
//                    item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.songs), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        HorizontalShimmerLoadingCard()
//                    }
//
//                    item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.artists), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        HorizontalShimmerLoadingCard()
//                    }
//
//                    item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.videos), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        HorizontalShimmerVideoLoadingCard()
//                    }
//
//                    item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.albums), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        HorizontalShimmerLoadingCard()
//                    }
//                }
//
//                is ResponseResult.Success -> {
//                    if (v.data.songs?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.songs), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.songs) { i, z ->
//                                ItemCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//                    if (v.data.artists?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.artists), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.artists) { i, z ->
//                                ItemArtistsCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (v.data.videos?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.videos), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.videos) { i, z ->
//                                VideoCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (v.data.albums?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.albums), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.albums) { i, z ->
//                                ItemCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (v.data.playlists?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.playlists), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.playlists) { i, z ->
//                                ItemCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//
//                    if (v.data.aiSongs?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.ai_music), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.aiSongs) { i, z ->
//                                ItemCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//                    if (v.data.news?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.news), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.news) { i, z ->
//                                VideoCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//                    if (v.data.podcast?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.podcasts), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.podcast) { i, z ->
//                                ItemCardView(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//                    if (v.data.movies?.isNotEmpty() == true) item {
//                        Spacer(Modifier.height(30.dp))
//                        Box(Modifier.padding(horizontal = 6.dp)) {
//                            TextViewBold(stringResource(R.string.movies), 23)
//                        }
//                        Spacer(Modifier.height(12.dp))
//                        LazyRow(Modifier.fillMaxWidth()) {
//                            itemsIndexed(v.data.movies) { i, z ->
//                                MoviesImageCard(z)
//
//                                if (!isPremium) {
//                                    if (i == 1) NativeViewAdsCard(z?.id)
//                                    if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                                }
//                            }
//                        }
//                    }
//
//                }
//            }
//        } else when (val v = homeViewModel.searchTrending) {
//            ResponseResult.Empty -> {}
//            is ResponseResult.Error -> {}
//            ResponseResult.Loading -> {
//                item {
//                    Spacer(Modifier.height(30.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.global_trending_songs), 19)
//                    }
//
//                    HorizontalShimmerLoadingCard()
//                }
//            }
//
//            is ResponseResult.Success -> {
//                if (v.data.globalSongs?.isNotEmpty() == true) item {
//                    Spacer(Modifier.height(30.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.global_trending_songs), 19)
//                    }
//                }
//
//                items(v.data.globalSongs?.chunked(25) ?: emptyList()) {
//                    Spacer(Modifier.height(15.dp))
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        itemsIndexed(it) { i, z ->
//                            ItemCardView(z)
//
//                            if (!isPremium) {
//                                if (i == 1) NativeViewAdsCard(z?.id)
//                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                            }
//                        }
//                    }
//                }
//
//                item { Spacer(Modifier.height(20.dp)) }
//
//                if (v.data.songs?.isNotEmpty() == true) item {
//                    Spacer(Modifier.height(30.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//
//                        TextViewBold(
//                            String.format(
//                                Locale.getDefault(),
//                                stringResource(R.string.trending_songs),
//                                inInfo?.country
//                            ),
//                            19
//                        )
//                    }
//                }
//
//                items(v.data.songs?.chunked(25) ?: emptyList()) {
//                    Spacer(Modifier.height(15.dp))
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        itemsIndexed(it) { i, z ->
//                            ItemCardView(z)
//
//                            if (!isPremium) {
//                                if (i == 1) NativeViewAdsCard(z?.id)
//                                if ((i + 1) % 6 == 0) NativeViewAdsCard(z?.id)
//                            }
//                        }
//                    }
//                }
//
//                item { Spacer(Modifier.height(20.dp)) }
//
//                if (v.data.artists?.isNotEmpty() == true) item {
//                    Spacer(Modifier.height(30.dp))
//                    Box(Modifier.padding(horizontal = 6.dp)) {
//                        TextViewBold(stringResource(R.string.trending_artists), 19)
//                    }
//                }
//
//                items(v.data.artists?.chunked(25) ?: emptyList()) {
//                    Spacer(Modifier.height(15.dp))
//                    LazyRow(Modifier.fillMaxWidth()) {
//                        itemsIndexed(it) { i, z ->
//                            if (i == 0 && !isPremium) NativeViewAdsCard(z?.id)
//                            ItemArtistsCardView(z)
//                        }
//                    }
//                }
//            }
//        }
//
//        item { Spacer(Modifier.height(320.dp)) }
//    }
//
//    LaunchedEffect(showSearch) {
//        focusManager.clearFocus()
//        homeViewModel.clearSearchKeywordsSuggestions()
//        homeViewModel.searchZene(showSearch)
//        if (showSearch.trim().length > 3) {
//            activity?.let { InterstitialAdsUtils(it) }
//            addRemoveSearchHistory(showSearch)
//            nativeAdsMap.clear()
//            nativeAdsAndroidViewMap.clear()
//        }
//    }
//    LaunchedEffect(Unit) {
//        homeViewModel.searchTrendingData()
//    }
//}