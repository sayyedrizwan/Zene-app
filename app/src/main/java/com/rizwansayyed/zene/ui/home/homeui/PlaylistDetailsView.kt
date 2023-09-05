package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.roomdb.collections.items.PlaylistSongsEntity
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.musicplay.RemoveFromPlaylist
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlaylistDetailsView(
    songsViewModel: SongsViewModel = hiltViewModel(),
    nav: HomeNavViewModel
) {

    val listState = rememberLazyGridState()

    LazyVerticalGrid(columns = GridCells.Fixed(2), Modifier.fillMaxSize(), state = listState) {
        item(span = { GridItemSpan(2) }) {
            TopPlaylistInfo(songsViewModel.playlistsData)
        }

        items(songsViewModel.playlistsSongs, span = { GridItemSpan(2) }) { song ->
            PlaylistSongs(song) { doPlay ->
                if (doPlay) {
                    nav.showMusicPlayer()
                    songsViewModel.songsPlayingDetails(song.thumbnail, song.name, song.artists)
                } else {
                    songsViewModel.removeSongPlaylist(song.pID)
                    songsViewModel.playlistsData?.let { songsViewModel.playlistSongs(it) }
                }
            }
        }

        if (songsViewModel.playlistsSuggestedSongs.isNotEmpty()) {
            item(span = { GridItemSpan(2) }) {
                QuickSandSemiBold(
                    stringResource(id = R.string.suggested_playlist_songs),
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp).padding(top = 80.dp, bottom = 10.dp),
                    align = TextAlign.Start
                )
            }

            items(songsViewModel.playlistsSuggestedSongs) {
                TrendingSongsViewShortText(it) { thumbnail, name, artists ->
                    nav.showMusicPlayer()
                    songsViewModel.songsPlayingDetails(thumbnail, name, artists)
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column {
                Spacer(Modifier.height(210.dp))
            }
        }
    }

    LaunchedEffect(Unit) {
//        delay(1.seconds)
        songsViewModel.playlistsData?.id?.let { songsViewModel.suggestSongsOnPlaylists(it) }
    }
}

@Composable
fun TopPlaylistInfo(playlist: PlaylistEntity?) {

    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        AsyncImage(
            playlist?.image1,
            "",
            Modifier
                .padding(top = 100.dp)
                .width(LocalConfiguration.current.screenWidthDp.dp - 90.dp)
                .height(LocalConfiguration.current.screenWidthDp.dp)
        )

        QuickSandBold(
            playlist?.name ?: "",
            Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp)
                .padding(horizontal = 5.dp),
            align = TextAlign.Center
        )

        Spacer(Modifier.height(30.dp))
    }
}

@Composable
fun PlaylistSongs(playlistSongs: PlaylistSongsEntity, play: (Boolean) -> Unit) {

    var rmDialog by remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                play(true)
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        AsyncImage(
            playlistSongs.thumbnail, "",
            Modifier
                .padding(13.dp)
                .size(60.dp)
        )
        Column(Modifier.weight(1f)) {
            QuickSandSemiBold(
                playlistSongs.name,
                Modifier.fillMaxWidth(),
                align = TextAlign.Start,
                size = 16,
                maxLine = 1
            )
            QuickSandRegular(
                playlistSongs.artists,
                Modifier.fillMaxWidth(),
                align = TextAlign.Start,
                size = 14,
                maxLine = 1,
                color = Color.Gray
            )
        }
        Image(
            painterResource(id = R.drawable.ic_delete),
            "",
            Modifier
                .padding(5.dp)
                .size(25.dp)
                .clickable {
                    rmDialog = true
                },
            colorFilter = ColorFilter.tint(Color.White)
        )
    }

    if (rmDialog) RemoveFromPlaylist {
        rmDialog = false
        if (it) {
            play(false)
        }
    }
}