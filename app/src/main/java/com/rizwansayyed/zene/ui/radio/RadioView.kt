package com.rizwansayyed.zene.ui.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.home.view.HorizontalSongView
import com.rizwansayyed.zene.ui.home.view.StyleSize
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.radio.view.RadioCountriesView
import com.rizwansayyed.zene.ui.radio.view.RadioLanguagesView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SongDynamicCards
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.Utils.THREE_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TOTAL_GRID_SIZE
import com.rizwansayyed.zene.utils.Utils.TWO_GRID_SIZE
import com.rizwansayyed.zene.viewmodel.RadioViewModel
import java.util.UUID

@Composable
fun RadioView() {
    val radioViewModel: RadioViewModel = hiltViewModel()
    val isThreeGrid = isScreenBig()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_GRID_SIZE),
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item(1, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column(Modifier.padding(horizontal = 12.dp)) {
                Spacer(Modifier.height(80.dp))
                TextPoppinsSemiBold(stringResource(R.string.zene_fm), size = 35)
                Spacer(Modifier.height(20.dp))
            }
        }

        when (val v = radioViewModel.topRadios) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> item(2, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                Column(Modifier.padding(top = 50.dp)) {
                    HorizontalSongView(
                        APIResponse.Loading,
                        Pair(TextSize.SMALL, "Trending Now"),
                        StyleSize.SHOW_AUTHOR,
                        showGrid = false
                    )
                }
            }

            is APIResponse.Success -> items(v.data, span = { GridItemSpan(TOTAL_GRID_SIZE) }) {
                if (it.list.isNotEmpty()) {
                    Column(Modifier.padding(top = 50.dp)) {
                        HorizontalSongView(
                            APIResponse.Success(it.list),
                            Pair(TextSize.SMALL, it.name ?: ""),
                            StyleSize.SHOW_AUTHOR,
                            showGrid = false
                        )
                    }
                }
            }
        }

        item(3, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                RadioLanguagesView(radioViewModel)
            }
        }

        item(4, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Column {
                RadioCountriesView(radioViewModel)
            }
        }

        item(5, { GridItemSpan(TOTAL_GRID_SIZE) }) {
            Spacer(Modifier.height(40.dp))
        }

        when (val v = radioViewModel.radiosYouMayLike) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                item(6, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                        TextPoppinsSemiBold(stringResource(R.string.radios_you_may_like), size = 15)
                    }
                }

                item(7, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    LoadingView(Modifier.size(32.dp))
                }
            }

            is APIResponse.Success -> if (v.data.isNotEmpty()) {
                item(8, { GridItemSpan(TOTAL_GRID_SIZE) }) {
                    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                        TextPoppinsSemiBold(
                            stringResource(R.string.songs_for_you), size = 15
                        )
                    }
                }

                items(v.data,
                    span = { GridItemSpan(if (isThreeGrid) THREE_GRID_SIZE else TWO_GRID_SIZE) }) {
                    SongDynamicCards(it, v.data)
                }
            }

        }

        item {
            Spacer(Modifier.height(260.dp))
        }
    }

    LaunchedEffect(Unit) {
        radioViewModel.init()
    }
}