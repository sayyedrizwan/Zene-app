package com.rizwansayyed.zene.presenter.ui.home.albums


import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.dialog.SimpleTextDialog
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.startOfflineDownloadWorkManager
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlArtistsShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun AlbumsShortcutButton() {
    val coroutines = rememberCoroutineScope()
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val isAlbumPresent by playlistAlbum.isAlbumPresent.collectAsState(initial = 0)
    var offlineDownloadDialog by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    val title = stringResource(id = R.string.offline_download)
    val desc = stringResource(id = R.string.all_songs_album_download_desc)
    val btn = stringResource(id = R.string.download)

    when (val v = playlistAlbum.playlistAlbum) {
        DataResponse.Loading -> {}
        is DataResponse.Success -> Row(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            RoundBorderButtonsView(stringResource(id = R.string.share)) {
                shareTxt(appUrlArtistsShare(homeNav.selectedAlbum))
            }
            RoundBorderButtonsView(stringResource(id = if (isAlbumPresent > 0) R.string.remove_from_saved_List else R.string.save_album)) {
                playlistAlbum.saveAlbumsLocally(v.item, homeNav.selectedAlbum, isAlbumPresent > 0)
            }
            RoundBorderButtonsView(stringResource(R.string.offline_download)) {
                offlineDownloadDialog = true
            }
        }

        else -> {}
    }

    fun startDownload() {
        playlistAlbum.playlistSongsItem.forEach {
            startOfflineDownloadWorkManager(it.pId)
        }
        job?.cancel()
    }

    if (offlineDownloadDialog) SimpleTextDialog(title, desc, btn, {
        offlineDownloadDialog = false

        if (playlistAlbum.playlistSongsItem.size == playlistAlbum.playlistSongsListSize) {
            startDownload()
        } else job = coroutines.launch(Dispatchers.IO) {
            while (true) {
                if (playlistAlbum.playlistSongsItem.size == playlistAlbum.playlistSongsListSize)
                    startDownload()

                delay(2.seconds)
            }
        }
    }, {
        offlineDownloadDialog = false
    })

    DisposableEffect(Unit) {
        onDispose {
            job?.cancel()
        }
    }
}