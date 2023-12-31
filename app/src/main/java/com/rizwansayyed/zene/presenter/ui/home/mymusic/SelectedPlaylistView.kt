package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.toMusicData
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.toMusicDataList
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.home.HomepageTopView
import com.rizwansayyed.zene.presenter.ui.home.online.GlobalTrendingPagerItems
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel

@Composable
fun SelectedPlaylistView() {
    val playlist: PlaylistAlbumViewModel = hiltViewModel()
    val homeViewModel: HomeNavViewModel = hiltViewModel()

    val width = LocalConfiguration.current.screenWidthDp / 1.6

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Row(Modifier.padding(top = 50.dp)) {
                SmallIcons(R.drawable.ic_arrow_left) {
                    homeViewModel.setSelectMyMusicPlaylists(null)
                }

                Spacer(Modifier.weight(1f))
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(30.dp))
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                homeViewModel.selectMyMusicPlaylists.value?.thumbnail?.let {
                    AsyncImage(
                        it, homeViewModel.selectMyMusicPlaylists.value?.name,
                        Modifier.size(width.dp),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    Image(
                        painterResource(id = R.mipmap.logo), "", Modifier.size(width.dp)
                    )
                }
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(20.dp))
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            TextSemiBold(
                homeViewModel.selectMyMusicPlaylists.value?.name ?: "",
                Modifier.fillMaxWidth(),
                doCenter = true
            )
        }


        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(40.dp))
        }

        itemsIndexed(
            playlist.myMusicPlaylistSongsItem,
            span = { _, _ -> GridItemSpan(TOTAL_ITEMS_GRID) }) { i, s ->
            GlobalTrendingPagerItems(s.toMusicData(), false) {
                addAllPlayer(playlist.myMusicPlaylistSongsItem.toMusicDataList().toTypedArray(), i)
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(200.dp))
        }
    }

    LaunchedEffect(Unit) {
        playlist.getAllPlaylistSongs(homeViewModel.selectMyMusicPlaylists.value?.id)
    }
}
