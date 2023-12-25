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
import com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs.DEFAULT_PLAYLIST_ITEMS
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.MusicPlaylistDialog
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.MyMusicViewModel

@Composable
fun MyMusicPlaylistsList(myMusic: MyMusicViewModel, homeNavViewModel: HomeNavViewModel) {
    val playlists by myMusic.savePlaylists.collectAsState(emptyList())
    val defaultPlaylistSongsCount = myMusic.defaultPlaylistSongs
    var page by remember { mutableIntStateOf(0) }

    val appNamePlaylists = stringResource(id = R.string.zene_playlist)

    Column(Modifier, Arrangement.Center) {
        Column(Modifier.padding(horizontal = 9.dp)) {
            TopInfoWithSeeMore(R.string.playlists, null, 50) {}
        }

        LazyRow(Modifier.fillMaxSize()) {
            item {
                AddNewPlaylist()
            }
            item {
                DefaultPlaylistsItem(defaultPlaylistSongsCount) {
                    val p = SavedPlaylistEntity(
                        -0, appNamePlaylists, items = defaultPlaylistSongsCount,
                        playlistId = DEFAULT_PLAYLIST_ITEMS.replace(",", "")
                    )
                    homeNavViewModel.setSelectMyMusicPlaylists(p)
                }
            }

            items(playlists) {
                MyMusicPlaylistsItems(it) {
                    homeNavViewModel.setSelectMyMusicPlaylists(it)
                }
            }

            items(myMusic.savePlaylistsLoadList) {
                MyMusicPlaylistsItems(it) {
                    homeNavViewModel.setSelectMyMusicPlaylists(it)
                }
            }

            if (playlists.size >= OFFSET_LIMIT && myMusic.savePlaylistsLoadMore) item {
                LoadMoreCircleButtonForHistory {
                    page += 1
                    myMusic.savedAlbumsLoadList(page * 50)
                }
            }
        }
    }
}

@Composable
fun MyMusicPlaylistsItems(playlists: SavedPlaylistEntity, click: () -> Unit) {
    val mod = Modifier
        .fillMaxWidth()
        .height(75.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(Color.White)
        .clickable {
            click()
        }

    Column(
        Modifier
            .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
            .width(140.dp)
    ) {
        if (playlists.thumbnail == null)
            Column(mod, Arrangement.Center, Alignment.CenterHorizontally) {
                Image(
                    painterResource(R.drawable.ic_playlist), "",
                    Modifier.size(25.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
        else
            AsyncImage(playlists.thumbnail, playlists.name, mod, contentScale = ContentScale.Crop)

        Spacer(Modifier.height(5.dp))
        TextThin(playlists.name, Modifier.padding(horizontal = 4.dp), singleLine = true, size = 14)
        Spacer(Modifier.height(2.dp))
        TextThin(
            "${playlists.items} ${stringResource(R.string.tracks).lowercase()}",
            Modifier.padding(horizontal = 4.dp),
            singleLine = true,
            size = 13
        )
    }
}

@Composable
fun DefaultPlaylistsItem(count: Int, click: () -> Unit) {
    Column(
        Modifier
            .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
            .width(140.dp)
            .clickable { click() }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Image(painterResource(R.mipmap.logo), "", Modifier.size(50.dp))
        }

        Spacer(Modifier.height(5.dp))
        TextThin(
            stringResource(R.string.playlists),
            Modifier.padding(horizontal = 4.dp),
            singleLine = true,
            size = 14
        )
        Spacer(Modifier.height(2.dp))
        TextThin(
            "$count ${stringResource(R.string.tracks).lowercase()}",
            Modifier.padding(horizontal = 4.dp),
            singleLine = true,
            size = 13
        )
    }
}


@Composable
fun AddNewPlaylist() {
    var playlistAddDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
            .clickable { playlistAddDialog = true }
            .width(140.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.ic_plus_sign_square), "",
                Modifier.size(25.dp), colorFilter = ColorFilter.tint(Color.Black)
            )
        }

        Spacer(Modifier.height(5.dp))
        TextThin(
            stringResource(R.string.new_playlist), Modifier.fillMaxWidth(), true,
            singleLine = true, size = 14
        )
    }


    if (playlistAddDialog) MusicPlaylistDialog(null) {
        playlistAddDialog = false
    }
}