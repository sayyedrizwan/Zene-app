package com.rizwansayyed.zene.ui.main.search

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.main.search.view.TrendingItemView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ItemArtistsCardView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchView(homeViewModel: HomeViewModel) {
    val inInfo by DataStorageManager.ipDB.collectAsState(null)
    var search by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LazyColumn(Modifier.fillMaxWidth()) {
        item {
            Spacer(Modifier.height(70.dp))

            Row(
                Modifier.padding(horizontal = 9.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                TextViewBold(stringResource(R.string.search), 24)
                Spacer(Modifier.weight(1f))
                ImageIcon(R.drawable.ic_voice_id, 25)
                Spacer(Modifier.width(9.dp))
                ImageIcon(R.drawable.ic_mic, 25)
            }
        }

        stickyHeader {
            TextField(
                search,
                { if (it.length <= 20) search = it },
                Modifier
                    .padding(top = 30.dp)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
//                    if (search.length > 3) viewModel.searchZene(search)
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_s_p_a_etc), 14)
                },
                trailingIcon = {
                    if (search.length > 3) {
                        IconButton({
                            focusManager.clearFocus()
//                            viewModel.searchZene(search)
                        }) {
                            ImageIcon(R.drawable.ic_search, 24)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MainColor,
                    unfocusedContainerColor = MainColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )
        }

        when (val v = homeViewModel.searchTrending) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item {
                CircularLoadingView()
            }

            is ResponseResult.Success -> {
                item {
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.keywords ?: emptyList()) {
                            TrendingItemView(it)
                        }
                    }
                }
                item { Spacer(Modifier.height(20.dp)) }

                if (v.data.globalSongs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.global_trending_songs), 19)
                    }
                }

                items(v.data.globalSongs?.chunked(25) ?: emptyList()) {
                    Spacer(Modifier.height(15.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(it) {
                            ItemCardView(it)
                        }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }

                if (v.data.songs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {

                        TextViewBold(
                            String.format(stringResource(R.string.trending_songs), inInfo?.country),
                            19
                        )
                    }
                }

                items(v.data.songs?.chunked(25) ?: emptyList()) {
                    Spacer(Modifier.height(15.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(it) {
                            ItemCardView(it)
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
                        items(it) {
                            ItemArtistsCardView(it)
                        }
                    }
                }
            }
        }


        item { Spacer(Modifier.height(250.dp)) }
    }


    LaunchedEffect(Unit) {
        homeViewModel.searchTrendingData()
    }
}