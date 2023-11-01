package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItems
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItemsShort
import com.rizwansayyed.zene.presenter.ui.home.online.ArtistsFanItems
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.ui.home.online.SimilarArtistsItems
import com.rizwansayyed.zene.presenter.ui.home.online.SongsExploreItems
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TWO_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel


@Composable
fun SearchViewInfo(s: String, close: () -> Unit) {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()
    val errorLoading = stringResource(id = R.string.error_loading_data_try_again)

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.padding(start = 12.dp)) {
                Spacer(Modifier.height(45.dp))

                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.height(6.dp))

                    SmallIcons(R.drawable.ic_arrow_left, 30) {
                        close()
                    }

                    Spacer(Modifier.height(12.dp))

                    TextBold(s, size = 38)
                }

                Spacer(Modifier.height(30.dp))
            }
        }

        when (val v = homeApiViewModel.searchData) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {
                close()
                errorLoading.toast()
            }

            DataResponse.Loading -> items(20, span = { GridItemSpan(TWO_ITEMS_GRID) }) {
                LoadingAlbumsCards()
            }

            is DataResponse.Success -> {
                if (v.item?.artists?.isNotEmpty() == true)
                    items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Column(Modifier.fillMaxWidth()) {

                            TopInfoWithSeeMore(stringResource(id = R.string.artists), null) {}

                            LazyRow(Modifier.fillMaxWidth()) {
                                items(v.item.artists) { artists ->
                                    SimilarArtistsItems(artists)
                                }
                            }
                        }
                    }

                if (v.item?.albums?.isNotEmpty() == true)
                    items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Column(Modifier.fillMaxWidth()) {
                            TopInfoWithSeeMore(stringResource(id = R.string.albums), null) {}

                            LazyRow(Modifier.fillMaxWidth()) {
                                items(v.item.albums) { album ->
                                    AlbumsItemsShort(album)
                                }
                            }
                        }
                    }

                if (v.item?.songs?.isNotEmpty() == true)
                    items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                        Column(Modifier.fillMaxWidth()) {
                            TopInfoWithSeeMore(stringResource(id = R.string.songs), null) {}
                        }
                    }

                itemsIndexed(
                    v.item?.songs ?: emptyList(),
                    span = { _, _ -> GridItemSpan(TWO_ITEMS_GRID) }) { i, m ->
                    SongsExploreItems(m) {
                        addAllPlayer((v.item?.songs ?: emptyList()).toTypedArray(), i)
                    }
                }

                items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Spacer(Modifier.height(160.dp))
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        homeApiViewModel.searchData(s)
    }
}