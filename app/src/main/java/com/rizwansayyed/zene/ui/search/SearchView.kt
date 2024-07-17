package com.rizwansayyed.zene.ui.search

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.searchHistoryDB
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingText
import com.rizwansayyed.zene.ui.view.SearchScreenBar
import com.rizwansayyed.zene.ui.view.SearchTexts
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.Utils.enterUniqueSearchHistory
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchView(homeViewModel: HomeViewModel, back: () -> Unit) {
    val context = LocalContext.current as Activity
    val searchHistory by searchHistoryDB.collectAsState(initial = emptyArray())
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        stickyHeader {
            Spacer(Modifier.height(20.dp))
            SearchScreenBar(searchQuery) {
                homeViewModel.searchSuggestions(it)
                searchQuery = it
            }
        }

        item {
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
                        if (b) enterUniqueSearchHistory(it)
                        else homeViewModel.searchSuggestions(it)
                    }
                }

                item {
                    Spacer(Modifier.height(60.dp))
                }
            }
        }

        item {
            Spacer(Modifier.height(20.dp))
            Row(Modifier.padding(horizontal = 9.dp)) {
                TextPoppins(stringResource(R.string.search_history), size = 24)
            }
        }


        if (searchHistory?.toList()?.isEmpty() == true) {
            item {
                Spacer(Modifier.height(80.dp))
                TextPoppinsThin(stringResource(R.string.no_search_history), true, size = 20)
                Spacer(Modifier.height(40.dp))
            }
        } else {
            items(searchHistory?.toList() ?: emptyList()) {
                SearchTexts(it, true) { b ->
                    searchQuery = it
                    if (b) searchQuery = it
                    else homeViewModel.searchSuggestions(it)
                }
            }
        }

        item {
            Spacer(Modifier.height(80.dp))
        }
    }

    BackHandler {
        if (searchQuery != "") {
            searchQuery = ""
            return@BackHandler
        }
        back()
    }
}
