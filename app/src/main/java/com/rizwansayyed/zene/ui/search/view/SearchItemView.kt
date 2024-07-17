package com.rizwansayyed.zene.ui.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HorizontalArtistsView
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.HorizontalVideoView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.enterUniqueSearchHistory
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun SearchItemView(homeViewModel: HomeViewModel, search: String, close: () -> Unit) {
    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        when (val v = homeViewModel.searchQuery) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                item(key = 50, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TopSearchHeader(search, close)
                }
                items(7, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LazyRow(Modifier.padding(bottom = 10.dp)) {
                        items(9) {
                            LoadingCardView()
                        }
                    }
                }
            }

            is APIResponse.Success -> {
                item(key = 52, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    TopSearchHeader(search, close)
                }
                item(key = 53, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(30.dp))
                }

                item(key = 56, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Column {
                        HorizontalArtistsView(
                            APIResponse.Success(v.data.artists),
                            Pair(TextSize.SMALL, R.string.artists), showGrid = true
                        )
                    }
                }

                item(key = 57, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(70.dp))
                }


                item(key = 58, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Column {
                        HorizontalSongView(
                            APIResponse.Success(v.data.playlists),
                            Pair(TextSize.SMALL, R.string.playlists),
                            StyleSize.HIDE_AUTHOR, showGrid = false
                        )
                    }
                }

                item(key = 59, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(70.dp))
                }


                item(key = 60, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Column {
                        HorizontalSongView(
                            APIResponse.Success(v.data.albums),
                            Pair(TextSize.SMALL, R.string.albums),
                            StyleSize.SHOW_AUTHOR, showGrid = false
                        )
                    }
                }

                item(key = 61, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(70.dp))
                }


                item(key = 62, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Column {
                        HorizontalVideoView(APIResponse.Success(v.data.videos), R.string.videos)
                    }
                }

                item(key = 63, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Spacer(Modifier.height(70.dp))
                }


                item(65, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                        TextPoppinsSemiBold(stringResource(R.string.songs), size = 15)

                    }
                }

                items(v.data.songs,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it)
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        homeViewModel.search(search)
        enterUniqueSearchHistory(search)
    }
}

@Composable
fun TopSearchHeader(search: String, close: () -> Unit) {
    Column {
        Spacer(Modifier.height(70.dp))

        Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
            Box(Modifier.clickable { close() }) {
                ImageIcon(R.drawable.ic_arrow_left, 30, Color.White)
            }

            Box(
                Modifier
                    .padding(horizontal = 6.dp)
                    .weight(1f)
            ) {
                TextPoppins(search, size = 35)
            }
        }

        Spacer(Modifier.height(30.dp))
    }
}