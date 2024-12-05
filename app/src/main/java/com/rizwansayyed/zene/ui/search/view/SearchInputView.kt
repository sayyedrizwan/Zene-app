package com.rizwansayyed.zene.ui.search.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.searchHistoryDB
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.InputTypes
import com.rizwansayyed.zene.ui.view.LoadingText
import com.rizwansayyed.zene.ui.view.SearchScreenBar
import com.rizwansayyed.zene.ui.view.SearchTexts
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun SearchInputView(homeViewModel: HomeViewModel, search: (String) -> Unit) {
    val searchHistory by searchHistoryDB.collectAsState(initial = emptyArray())
    var searchQuery by remember { mutableStateOf("") }

    var searchJob by remember { mutableStateOf<Job?>(null) }
    val coroutine = rememberCoroutineScope()

    val context = LocalContext.current as Activity

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item {
            Spacer(Modifier.height(20.dp))

            SearchScreenBar(InputTypes.SEARCH, searchQuery, {
                searchQuery = it
                searchJob?.cancel()
                searchJob = coroutine.launch {
                    delay(600)
                    homeViewModel.searchSuggestions(it)
                }
            }) {
                search(it)
                searchQuery = it
            }
        }

        item(key = 1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(20.dp))
        }

        when (val v = homeViewModel.searchSuggestions) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> items(10) {
                LoadingText()
            }

            is APIResponse.Success -> {
                items(v.data) {
                    SearchTexts(it, false) { b ->
                        searchQuery = it
                        if (b) search(it)
                        else homeViewModel.searchSuggestions(it)
                    }
                }

                item(key = 2) {
                    Spacer(Modifier.height(60.dp))
                }
            }
        }

        item(key = 3) {
            Spacer(Modifier.height(20.dp))
            Row(Modifier.padding(horizontal = 9.dp)) {
                TextPoppins(stringResource(R.string.search_history), size = 24)
            }
        }


        if (searchHistory?.toList()?.isEmpty() == true) {
            item(key = 4) {
                Spacer(Modifier.height(80.dp))
                TextPoppinsThin(stringResource(R.string.no_search_history), true, size = 20)
                Spacer(Modifier.height(40.dp))
            }
        } else {
            items(searchHistory?.toList() ?: emptyList()) {
                SearchTexts(it, true) { b ->
                    searchQuery = it
                    if (b) search(it)
                    else homeViewModel.searchSuggestions(it)
                }
            }
        }

        item(key = 5) {
            Spacer(Modifier.height(180.dp))
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.searchSuggestions("", true)
        searchQuery = ""

        ShowAdsOnAppOpen(context).interstitialAds()
    }
}