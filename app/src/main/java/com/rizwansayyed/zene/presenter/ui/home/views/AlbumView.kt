package com.rizwansayyed.zene.presenter.ui.home.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumNameWithPlayAllButton
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumsShortcutButton
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumsSongsItems
import com.rizwansayyed.zene.presenter.ui.home.albums.AlbumsTopThumbnail
import com.rizwansayyed.zene.presenter.ui.home.albums.SimilarAlbums
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel

@Composable
fun AlbumView() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val listState = rememberLazyGridState()

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
                Column(Modifier.fillMaxWidth()) {
                    AlbumsTopThumbnail()
                    AlbumNameWithPlayAllButton()
                }
            }

            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                Column(Modifier.fillMaxWidth()) {
                    AlbumsShortcutButton()
                    SimilarAlbums()
                    Spacer(Modifier.height(80.dp))
                }
            }

            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                GlobalNativeFullAds()
            }

            itemsIndexed(
                playlistAlbum.playlistSongsItem,
                span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, item ->
                AlbumsSongsItems(item, {
                    homeNav.setSongDetailsDialog(item)
                }, {
                    addAllPlayer(playlistAlbum.playlistSongsItem.toTypedArray(), i)
                })

            }

            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                Spacer(Modifier.height(200.dp))
            }
        }
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