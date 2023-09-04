package com.rizwansayyed.zene.ui.musicplay

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.ui.dashedBorder
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.musicplay.video.FullVideoPlayerActivity
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerViewStatus
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.EXTRA.SONG_NAME_EXTRA
import com.rizwansayyed.zene.utils.Utils.showToast

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MusicPlayerButtonsView(
    music: MusicPlayerDetails, nav: HomeNavViewModel, songs: SongsViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext

    var showBookmarkDialog by remember { mutableStateOf(false) }
    var showRmBookmarkDialog by remember { mutableStateOf(false) }

    val offlineSongAvailable = stringResource(id = R.string.song_is_offline_available)

    val offlineStatus by songs.musicOfflineSongs.value!!.collectAsState(initial = null)

    Spacer(modifier = Modifier.height(60.dp))

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {

        Spacer(modifier = Modifier.height(8.dp))

        IconsForMusicShortcut(R.drawable.ic_video_replay) {
            nav.doPlayer(true)
            val searchName = "${music.songName?.lowercase()?.replace("official video", "")} - ${
                music.artists?.substringBefore(",")?.substringBefore("&")
            }".lowercase()
            Intent(context, FullVideoPlayerActivity::class.java).apply {
                putExtra(SONG_NAME_EXTRA, searchName)
                flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }

        IconsForMusicShortcut(R.drawable.ic_closed_caption) {
            nav.musicViewType(VideoPlayerViewStatus.LYRICS)
        }

        if (offlineStatus == null)
            IconsForMusicShortcut(R.drawable.ic_download_icon) {
                songs.insertOfflineSongs(music)
            }
        else if (offlineStatus?.isEmpty() == true)
            IconsForMusicShortcut(R.drawable.ic_download_icon) {
                songs.insertOfflineSongs(music)
            }
        else if (offlineStatus?.first()?.status == OfflineStatusTypes.DOWNLOADING)
            CircularProgressIndicator(
                Modifier
                    .padding(18.dp)
                    .size(24.dp), Color.White
            )
        else if (offlineStatus?.first()?.status == OfflineStatusTypes.SUCCESS)
            IconsForMusicShortcut(R.drawable.ic_download_success) {
                offlineSongAvailable.showToast()
            }
        else
            IconsForMusicShortcut(R.drawable.ic_download_icon) {
                songs.insertOfflineSongs(music)
            }

        if (songs.isSongInPlaylist == 0)
            IconsForMusicShortcut(R.drawable.ic_bookmark) {
                showBookmarkDialog = true
            }
        else
            IconsForMusicShortcut(R.drawable.ic_bookmark_check) {
                showRmBookmarkDialog = true
            }

        IconsForMusicShortcut(R.drawable.ic_information_circle) {
            nav.musicViewType(VideoPlayerViewStatus.INFO)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    LaunchedEffect(Unit) {
        songs.isSongInPlaylist(music.pId ?: "")
    }

    LaunchedEffect(nav.showMusicPlayerView.value) {
        if (nav.showMusicPlayerView.value)
            songs.offlineSongsData(music.pId ?: "")
    }

    if (showBookmarkDialog) AddSongsToPlayList(music, songs) {
        showBookmarkDialog = false
    }

    if (showRmBookmarkDialog) RemoveFromPlaylist {
        showRmBookmarkDialog = false
        if (it){
            songs.removeSongPlaylist(music.pId ?: "")
        }
    }
}

@Composable
fun RemoveFromPlaylist(close: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { close(false) },
        text = {
            QuickSandSemiBold(
                stringResource(id = R.string.remove_song_playlist),
                Modifier.padding(5.dp),
                size = 15,
            )
        },
        confirmButton = {
            TextButton(onClick = { close(true) }) {
                QuickSandBold(
                    stringResource(id = R.string.remove).uppercase(),
                    Modifier.padding(5.dp),
                    size = 16,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { close(false) }) {
                QuickSandBold(
                    stringResource(id = R.string.cancel).uppercase(),
                    Modifier.padding(5.dp),
                    size = 16,
                )
            }
        },
    )
}

@Composable
fun AddSongsToPlayList(
    music: MusicPlayerDetails,
    songs: SongsViewModel = hiltViewModel(),
    close: () -> Unit
) {

    val playlists by songs.playlists.collectAsState(initial = emptyList())

    var addPlaylistView by remember { mutableStateOf(false) }

    val playlistAvailable = stringResource(id = R.string.playlists_same_name)
    val playlistValidName = stringResource(id = R.string.enter_a_valid_playlist_name)

    Dialog(close) {
        Card(
            Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenWidthDp.dp)
                .padding(8.dp),
            RoundedCornerShape(10.dp),
            CardDefaults.cardColors(Color.Black),
            CardDefaults.cardElevation(8.dp)
        ) {
            LazyColumn(Modifier.fillMaxWidth()) {
                item {
                    QuickSandSemiBold(
                        stringResource(id = R.string.playlists),
                        size = 17, modifier = Modifier.padding(15.dp)
                    )
                }

                item {
                    if (addPlaylistView) {
                        AddPlaylistView {
                            if (it.trim().length < 4) {
                                playlistValidName.showToast()
                                return@AddPlaylistView
                            }
                            songs.insertPlaylist(it.trim()) { status ->
                                if (!status) playlistAvailable.showToast()
                            }
                        }
                    } else
                        AddPlaylistBtn {
                            addPlaylistView = true
                        }
                }

                item {
                    if (playlists.isEmpty())
                        Row(
                            Modifier.fillMaxWidth(),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            QuickSandLight(
                                stringResource(id = R.string.no_playlist_found),
                                size = 13, modifier = Modifier.padding(18.dp)
                            )
                        }
                }

                items(playlists) {
                    PlaylistItemView(it) {
                        close()
                        songs.insertSongToPlaylist(music, it)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItemView(playlist: PlaylistEntity, add: () -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 9.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable { add() },
        Arrangement.Start,
        Alignment.CenterVertically
    ) {
        if (playlist.image1.isNotEmpty())
            AsyncImage(
                playlist.image1,
                "",
                Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
        else
            Image(
                painterResource(id = R.drawable.ic_playlist),
                "",
                Modifier.size(30.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )

        QuickSandLight(
            playlist.name, size = 16, modifier = Modifier.padding(start = 8.dp), maxLine = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaylistView(addPlaylist: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    val iconView = @Composable {
        IconButton(
            onClick = {
                addPlaylist(text)
                text = ""
            },
        ) {
            Icon(
                painterResource(id = R.drawable.plus_sign_square),
                contentDescription = "",
                tint = Color.Black
            )
        }
    }

    OutlinedTextField(
        text, {
            if (text.length > 29) return@OutlinedTextField
            text = it
        }, Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .fillMaxWidth()
            .background(Color.LightGray),
        textStyle = TextStyle(Color.Black, 13.sp), singleLine = true,
        trailingIcon = iconView
    )
}

@Composable
fun AddPlaylistBtn(click: () -> Unit) {
    Box(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .dashedBorder(1.dp, Color.Gray, 8.dp)
            .clickable {
                click()
            },
        contentAlignment = Alignment.Center
    ) {
        QuickSandRegular(
            stringResource(id = R.string.new_playlists).lowercase(),
            size = 14,
            modifier = Modifier.padding(14.dp)
        )
    }
}


@Composable
fun IconsForMusicShortcut(ic: Int, click: () -> Unit) {
    Image(
        painter = painterResource(id = ic),
        contentDescription = "",
        modifier = Modifier
            .size(24.dp)
            .clickable {
                click()
            },
        colorFilter = ColorFilter.tint(Color.White)
    )
}