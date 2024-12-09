package com.rizwansayyed.zene.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.MusicType.ALBUMS
import com.rizwansayyed.zene.data.api.model.MusicType.SONGS
import com.rizwansayyed.zene.data.api.model.MusicType.VIDEO
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpMusicDataDB
import com.rizwansayyed.zene.ui.mymusic.GlobalRemovedCacheSongsProvider
import com.rizwansayyed.zene.ui.mymusic.playlists.AddPlaylistDialog
import com.rizwansayyed.zene.ui.player.offlinedownload.OfflineDownload
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.LIKED_SONGS_ON_ZENE_PLAYLISTS
import com.rizwansayyed.zene.utils.Utils.addSongToLast
import com.rizwansayyed.zene.utils.Utils.addSongToNext
import com.rizwansayyed.zene.utils.Utils.loadBitmap
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun TopInfoItemSheet(m: ZeneMusicDataItems) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .padding(end = 9.dp)
        ) {
            TextPoppins(m.name ?: "", false, size = 24, lineHeight = 30)

            if (m.type() == SONGS || m.type() == ALBUMS || m.type() == VIDEO) {
                Spacer(Modifier.height(3.dp))

                TextPoppinsThin(m.artists ?: "", false, size = 15)
            }
        }

        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(13.dp)),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
fun SongInfoItemSheet(m: ZeneMusicDataItems, close: () -> Unit) {
    val viewModel: ZeneViewModel = hiltViewModel()
    val roomViewModel: RoomDBViewModel = hiltViewModel()
    var addToPlaylistSongs by remember { mutableStateOf(false) }
    var isLiked by remember { mutableStateOf(false) }
    var offlineCache by remember { mutableStateOf(false) }

    SheetDialogSheet(R.drawable.ic_play, R.string.play) {
        openSpecificIntent(m, listOf(m))
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_next, R.string.play_next) {
        addSongToNext(m)
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(
        if (roomViewModel.isSaved) R.drawable.ic_tick else R.drawable.ic_folder_music,
        if (roomViewModel.isSaved) R.string.saved_offline_cache else R.string.offline_cache_song
    ) {
        offlineCache = true
    }

    Spacer(Modifier.height(20.dp))

    when (val v = viewModel.isSongLiked) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> LoadingText()
        is APIResponse.Success -> {
            AnimatedVisibility(isLiked) {
                SheetDialogSheet(R.drawable.ic_liked, R.string.liked) {
                    isLiked = !isLiked
                    viewModel.addRemoveSongFromPlaylists(
                        LIKED_SONGS_ON_ZENE_PLAYLISTS, m.id ?: "", false
                    )
                }
            }

            AnimatedVisibility(!isLiked) {
                SheetDialogSheet(R.drawable.ic_non_liked, R.string.like_the_song) {
                    isLiked = !isLiked
                    viewModel.addRemoveSongFromPlaylists(
                        LIKED_SONGS_ON_ZENE_PLAYLISTS, m.id ?: "", true
                    )
                }
            }
            LaunchedEffect(Unit) {
                isLiked = v.data
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_alarm_clock, R.string.add_to_wake_up_timer) {
        wakeUpMusicDataDB = flowOf(m)
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_play_queue, R.string.add_in_queue) {
        addSongToLast(m)
        close()
    }

    Spacer(Modifier.height(20.dp))

    SheetDialogSheet(R.drawable.ic_add_playlist, R.string.add_to_playlist) {
        addToPlaylistSongs = true
    }

    if (addToPlaylistSongs) AddSongToPlaylist(m) {
        addToPlaylistSongs = false
    }

    if (offlineCache) {
        if (roomViewModel.isSaved) AlertDialogView(
            R.string.remove_song_cache, R.string.remove_cache_from_offline, R.string.remove
        ) {
            if (it) {
                roomViewModel.removeOfflineSong(m)
                roomViewModel.isSongSaved(m)
                GlobalRemovedCacheSongsProvider.sendEvent(m.id ?: "")
            }
            offlineCache = false
        }
        else OfflineDownload(m) {
            roomViewModel.isSongSaved(m)
            offlineCache = false
        }

    }

    LaunchedEffect(Unit) {
        roomViewModel.isSongSaved(m)
        m.id?.let { viewModel.isSongLiked(it) }
    }
}

@Composable
fun AddSongToPlaylist(m: ZeneMusicDataItems, close: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Dialog(
        close, properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val zeneViewModel: ZeneViewModel = hiltViewModel()

        var addPlaylistItems by remember { mutableStateOf(false) }
        var page by remember { mutableIntStateOf(0) }

        val playlistsAddStatus = remember { mutableStateMapOf<String, Boolean>() }

        LazyColumn(
            modifier = Modifier
                .requiredSize(screenWidth - 10.dp, (screenHeight.value / 1.3).dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            item {
                Row(Modifier.padding(top = 20.dp, start = 10.dp, end = 10.dp)) {
                    Row(Modifier.weight(1f)) {
                        TextPoppins(stringResource(R.string.add_to_playlist))
                    }
                    ImageIcon(id = R.drawable.ic_add) {
                        addPlaylistItems = true
                    }
                }
            }
            item {
                Spacer(Modifier.height(30.dp))
            }

            items(zeneViewModel.saveSongPlaylists) {
                PlaylistAddComponents(it, zeneViewModel, m.id, playlistsAddStatus)
            }

            if (zeneViewModel.dataIsLoading) item {
                Row(
                    Modifier
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    LoadingView(Modifier.size(32.dp))
                }
            }

            if (!zeneViewModel.dataIsLoading && zeneViewModel.doShowMoreLoading) item {
                SmallButtonBorderText(R.string.load_more) {
                    page += 1
                    m.id?.let { zeneViewModel.checkIfSongPresentInPlaylists(it, page) }
                }
            }
        }

        if (addPlaylistItems) AddPlaylistDialog(zeneViewModel) {
            page = 0
            addPlaylistItems = false
            m.id?.let { zeneViewModel.checkIfSongPresentInPlaylists(it, page) }
        }

        LaunchedEffect(Unit) {
            logEvents(FirebaseLogEvents.FirebaseEvents.ADDING_TO_PLAYLISTS)
            m.id?.let { zeneViewModel.checkIfSongPresentInPlaylists(it, page) }
        }

        LaunchedEffect(playlistsAddStatus.values) {
            logEvents(FirebaseLogEvents.FirebaseEvents.ADDING_TO_PLAYLISTS)
            m.id?.let { zeneViewModel.checkIfSongPresentInPlaylists(it, page) }
        }
    }
}

@Composable
fun AlbumPlaylistInfoItemSheet(m: ZeneMusicDataItems, close: () -> Unit) {
    val zeneViewModel: ZeneViewModel = hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()

    var removeDialog by remember { mutableStateOf(false) }

    when (val v = homeViewModel.albumPlaylistData) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> LoadingText()
        is APIResponse.Success -> {
            val i = v.data
            if (i.isAdded == true) SheetDialogSheet(R.drawable.ic_tick, R.string.added_to_library) {
                removeDialog = true
            } else SheetDialogSheet(R.drawable.ic_folder_library, R.string.add_to_library) {
                CoroutineScope(Dispatchers.IO).launch {
                    val image = loadBitmap(i.info?.thumbnail.toString())
                    zeneViewModel.createNewPlaylist(i.info?.name ?: "", image, i.info?.id)
                    close()
                }
            }

            if (removeDialog) AlertDialogView(
                R.string.are_you_sure_want_to_remove,
                R.string.are_you_sure_want_to_remove_desc,
                R.string.remove
            ) {
                if (it) {
                    i.info?.id?.let { it1 -> zeneViewModel.deletePlaylists(it1) }
                }
                removeDialog = false
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.playlistsData(m.id ?: "")
    }
}


@Composable
fun PlaylistAddComponents(
    items: ZeneMusicDataItems,
    viewModel: ZeneViewModel,
    songID: String?,
    playlistsAddStatus: SnapshotStateMap<String, Boolean>
) {
    Row(
        Modifier
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        AsyncImage(
            items.thumbnail,
            items.name,
            Modifier
                .padding(start = 10.dp)
                .size(70.dp)
                .clip(RoundedCornerShape(13.dp)),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f)
        ) {
            TextPoppins(items.name ?: "", size = 16, limit = 2)
        }

        if (playlistsAddStatus[items.id] == true) ImageIcon(R.drawable.ic_tick, 25) {
            playlistsAddStatus[items.id ?: ""] = false
            viewModel.addRemoveSongFromPlaylists(items.id ?: "", songID ?: "", false)
        }
        else ImageIcon(R.drawable.ic_add, 25) {
            playlistsAddStatus[items.id ?: ""] = true
            viewModel.addRemoveSongFromPlaylists(items.id ?: "", songID ?: "", true)
        }

        LaunchedEffect(Unit) {
            items.id?.let { playlistsAddStatus[it] = items.extra == "present" }
        }
    }
}