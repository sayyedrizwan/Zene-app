package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumTopInfoDetails
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumsSongsList
import com.rizwansayyed.zene.presenter.ui.home.albums.ArtistsDesc
import com.rizwansayyed.zene.presenter.ui.home.albums.SimilarArtistsAlbums
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItemsShort
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel

@Composable
fun AlbumView() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val listState = rememberLazyGridState()

    val height = LocalConfiguration.current.screenHeightDp.dp / 2

    Box(
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        LazyVerticalGrid(
            GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier.fillMaxSize(), listState
        ) {

            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                Spacer(Modifier.height(80.dp))
            }

            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                when (val v = playlistAlbum.playlistAlbum) {
                    DataResponse.Empty -> {}
                    is DataResponse.Error -> {}
                    DataResponse.Loading -> {}
                    is DataResponse.Success -> Column(Modifier.fillMaxWidth()) {
                        AsyncImage(
                            v.item.thumbnail, "",
                            Modifier
                                .fillMaxWidth()
                                .height(height)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Row(
                            Modifier
                                .padding(top = 20.dp)
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth(),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                TextSemiBold(v.item.name ?: "", size = 25)
                                Spacer(Modifier.height(4.dp))
                                TextThin(v.item.artistsName ?: "")
                            }

                            Image(
                                painterResource(R.drawable.ic_play),
                                "",
                                Modifier
                                    .padding(5.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(MainColor)
                                    .padding(vertical = 15.dp)
                                    .padding(start = 18.dp, end = 15.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }

                        Row(
                            Modifier
                                .padding(top = 20.dp)
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                        ) {

                            Row(
                                Modifier
                                    .padding(5.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(100))
                                    .padding(vertical = 9.dp, horizontal = 14.dp)
                            ) {
                                TextRegular("Share")
                            }

                            Spacer(Modifier.height(4.dp))

                            Row(
                                Modifier
                                    .padding(5.dp)
                                    .border(1.dp, Color.Gray, RoundedCornerShape(100))
                                    .padding(vertical = 9.dp, horizontal = 14.dp)
                            ) {
                                TextRegular("Save Playlist")
                            }
                        }
                    }
                }
            }
        }
//            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
//                Column {
//                    AlbumTopInfoDetails()
//                }
//            }
//
//            itemsIndexed(
//                playlistAlbumViewModel.playlistSongsItem,
//                span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, item ->
//                AlbumsSongsList(item, {
//                    homeNav.setSongDetailsDialog(item)
//                }, {
//                    addAllPlayer(playlistAlbumViewModel.playlistSongsItem.toTypedArray(), i)
//                })
//            }
//
//            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
//                Column {
//                    Spacer(Modifier.height(50.dp))
//                    ArtistsDesc()
//                    Spacer(Modifier.height(50.dp))
//                }
//            }
//
//            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
//                SimilarArtistsAlbums()
//            }
//
//            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
//                Spacer(Modifier.height(150.dp))
//            }
    }

    Column {
        Spacer(Modifier.height(19.dp))

        SmallIcons(icon = R.drawable.ic_arrow_left, 28, 10) {
            homeNav.setAlbum("")
        }
    }

    LaunchedEffect(homeNav.selectedAlbum) {
        if (homeNav.selectedAlbum.isNotEmpty()) {
            playlistAlbum.playlistAlbum(homeNav.selectedAlbum)
            listState.scrollToItem(0)
        }
    }
}