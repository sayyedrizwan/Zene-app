package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.autoplaySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.loopSettings
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.startOfflineDownloadWorkManager
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlSongShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun MusicActionButtons(p: MusicPlayerData?) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val coroutine = rememberCoroutineScope()

    var playlistDialog by remember { mutableStateOf<MusicPlayerList?>(null) }
    val offlineDownload by playerViewModel.offlineSongStatus.collectAsState(initial = null)

    val loop by loopSettings.collectAsState(initial = false)
    val autoplay by autoplaySettings.collectAsState(initial = true)

    fun updateToTemp() {
        coroutine.launch(Dispatchers.IO) {
            val v = musicPlayerData.first()?.apply { temp = (111..999).random() }
            musicPlayerData = flowOf(v)
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()), Arrangement.Center, Alignment.CenterVertically
    ) {
        if (playerViewModel.showMusicType == Utils.MusicViewType.VIDEO)
            MusicActionButton(R.drawable.ic_headphones, R.string.switch_to_music) {
                updateToTemp()
                playerViewModel.setMusicType(Utils.MusicViewType.MUSIC)
            }
        else
            MusicActionButton(R.drawable.ic_flim_video, R.string.switch_to_video) {
                updateToTemp()
                playerViewModel.setMusicType(Utils.MusicViewType.VIDEO)
            }
        if (playerViewModel.showMusicType == Utils.MusicViewType.LYRICS)
            MusicActionButton(R.drawable.ic_headphones, R.string.switch_to_music) {
                updateToTemp()
                playerViewModel.setMusicType(Utils.MusicViewType.MUSIC)
            }
        else
            MusicActionButton(R.drawable.ic_closed_caption, R.string.switch_to_lyrics_video) {
                updateToTemp()
                playerViewModel.setMusicType(Utils.MusicViewType.LYRICS)
            }

        MusicActionButton(
            R.drawable.ic_repeat, if (loop) R.string.playing_in_loop else R.string.play_in_loop
        ) {
            loopSettings = flowOf(!loop)
        }

        MusicActionButton(
            R.drawable.ic_autoplay, if (autoplay) R.string.autoplay_on else R.string.autoplay_is_on
        ) {
            autoplaySettings = flowOf(!autoplay)
        }

        MusicActionButton(R.drawable.ic_share, R.string.share) {
            p?.songID?.let { shareTxt(appUrlSongShare(it)) }
        }
        MusicActionButton(R.drawable.ic_playlist, R.string.add_to_playlist) {
            playlistDialog = p?.v
        }

        OfflineSongDownloadButton(offlineDownload, p, playerViewModel)
    }

    playlistDialog?.let { MusicPlaylistDialog(it) { playlistDialog = null } }
}

@Composable
fun OfflineSongDownloadButton(
    offlineDownload: OfflineDownloadedEntity?, p: MusicPlayerData?, playerViewModel: PlayerViewModel
) {
    val downloading = stringResource(R.string.downloading__)
    var rmDialog by remember { mutableStateOf(false) }


    fun startDownloading() {
        p?.v?.let {
            playerViewModel.addOfflineSong(it)
            startOfflineDownloadWorkManager(it.songID)
        }
    }

    if (offlineDownload == null)
        MusicActionButton(R.drawable.ic_download, R.string.offline_download) {
            startDownloading()
        }
    else if (offlineDownload.progress < 100)
        MusicActionButton(R.drawable.ic_download, "$downloading (${offlineDownload.progress}%)") {
            startDownloading()
        }
    else if (offlineDownload.progress == 100)
        MusicActionButton(R.drawable.ic_tick, R.string.offline_downloaded) {
            rmDialog = true
        }

    if (rmDialog) DeleteOfflineDialog({
        playerViewModel.rmDownloadSongs(p?.songID ?: "")
        rmDialog = false
    }, {
        rmDialog = false
    })

}


@Composable
fun DeleteOfflineDialog(
    onConfirmation: () -> Unit, onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            TextRegular(stringResource(R.string.are_you_sure_want_to_rm_downloaded_song))
        },
        text = {},
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                TextSemiBold(stringResource(R.string.remove))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                TextSemiBold(stringResource(R.string.cancel))
            }
        },
        containerColor = MainColor
    )
}

@Composable
fun MusicActionButton(drawable: Int, txt: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                click()
            }
            .padding(horizontal = 6.dp, vertical = 3.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {

        SmallIcons(icon = drawable, size = 17)
        TextRegular(v = stringResource(txt))

        Spacer(Modifier.width(6.dp))
    }
}

@Composable
fun MusicActionButton(drawable: Int, txt: String, click: () -> Unit) {
    Row(
        Modifier
            .padding(6.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
            .clickable {
                click()
            }
            .padding(horizontal = 6.dp, vertical = 3.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {

        SmallIcons(icon = drawable, size = 17)
        TextRegular(v = txt)

        Spacer(Modifier.width(6.dp))
    }
}