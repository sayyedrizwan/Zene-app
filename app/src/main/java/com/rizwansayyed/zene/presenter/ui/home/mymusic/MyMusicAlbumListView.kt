package com.rizwansayyed.zene.presenter.ui.home.mymusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.asMusicData
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItemsShort
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlaylistDialog
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun MyMusicAlbumList(myMusic: MyMusicViewModel) {
    val homeNav: HomeNavViewModel = hiltViewModel()
    val albums by myMusic.saveAlbums.collectAsState(emptyList())
    var page by remember { mutableIntStateOf(0) }

    if (albums.isNotEmpty()) Column(Modifier, Arrangement.Center) {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.albums, null, 50) {}
        }

        LazyRow(Modifier.fillMaxSize()) {
            items(albums) {
                AlbumsItemsShort(it.asMusicData()) {
                    homeNav.setAlbum(it.playlistId ?: "")
                }
            }

            items(myMusic.saveAlbumsLoadList) {
                AlbumsItemsShort(it.asMusicData()) {
                    homeNav.setAlbum(it.playlistId ?: "")
                }
            }

            if (albums.size >= OFFSET_LIMIT && myMusic.saveAlbumsLoadMore) item {
                LoadMoreCircleButtonForHistory {
                    page += 1
                    myMusic.savedAlbumsLoadList(page * 50)
                }
            }
        }
    }
}