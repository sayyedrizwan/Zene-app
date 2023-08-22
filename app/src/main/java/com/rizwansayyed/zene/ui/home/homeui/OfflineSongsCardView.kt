package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsEntity
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.home.homeui.OfflineSongsStatus.*
import com.rizwansayyed.zene.ui.musicplay.IconsForMusicShortcut
import com.rizwansayyed.zene.ui.theme.PurpleGrey40
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.Utils.showToast
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineSongsCard(songs: OfflineSongsEntity, songsViewModel: SongsViewModel) {

    var showCustomDialog by remember { mutableStateOf(false) }

    val songDownloading = stringResource(id = R.string.downloading_the_song_info)
    val songErrorDownloading = stringResource(id = R.string.error_downloading_the_song_info)

    Card(
        onClick = {
            if (songs.status == OfflineStatusTypes.DOWNLOADING) {
                songDownloading.showToast()
                return@Card
            }

            if (!File(songs.songPath).exists()){
                startDownloadSongsWorkManager()
                songErrorDownloading.showToast()
                return@Card
            }
            showCustomDialog = true
        },
        colors = CardDefaults.cardColors(PurpleGrey40),
        modifier = Modifier
            .padding(5.dp)
            .height(250.dp)
            .width(180.dp),
        elevation = CardDefaults.cardElevation(20.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val songPath =
                    if (songs.img.contains("https")) songs.img else File(songs.img)
                AsyncImage(
                    songPath, contentDescription = "", modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(50))
                )

                Spacer(modifier = Modifier.height(7.dp))

                QuickSandRegular(songs.songName.trim(), size = 17, maxLine = 1)

                Spacer(modifier = Modifier.height(2.dp))

                QuickSandLight(songs.songArtists.trim(), size = 11, maxLine = 1)
            }

            if (songs.status == OfflineStatusTypes.DOWNLOADING) {
                CircularProgressIndicator(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(18.dp)
                        .size(24.dp), Color.White
                )
            } else {
                if (!File(songs.songPath).exists())
                    IconsForMusicShortcut(R.drawable.ic_warning) {}
            }
        }
    }

    if (showCustomDialog) OfflineOptionsDialog {
        when (it) {
            DISMISS -> showCustomDialog = false
            PLAY -> songsViewModel.songsPlayingOffline(songs)
            DELETE -> songsViewModel.songsDeleteOffline(songs)
        }
        showCustomDialog = false
    }
}

@Composable
fun OfflineOptionsDialog(callback: (OfflineSongsStatus) -> Unit) {
    AlertDialog(
        onDismissRequest = { callback(DISMISS) },
        text = {
            Column {
                Button(onClick = { callback(PLAY) }) {
                    Text(text = "play")
                }

                Button(onClick = { callback(DELETE) }) {
                    Text(text = "delete")
                }
            }
        },
        dismissButton = { Text(text = "") },
        confirmButton = { Text(text = "") }
    )
}

enum class OfflineSongsStatus(val v: Int) {
    DISMISS(0), PLAY(1), DELETE(2)
}