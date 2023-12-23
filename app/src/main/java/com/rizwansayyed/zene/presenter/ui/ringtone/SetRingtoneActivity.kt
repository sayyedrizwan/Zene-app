package com.rizwansayyed.zene.presenter.ui.ringtone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.ringtone.view.RingtoneEditView
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.utils.FileDownloaderInChunks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SetRingtoneActivity : ComponentActivity() {

    @Inject
    lateinit var songDownload: SongDownloaderInterface

    @Inject
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)
        setContent {
            ZeneTheme {
                val p = runBlocking(Dispatchers.IO) { musicPlayerData.first() }
                var isDownloaded by remember { mutableStateOf(false) }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkGreyColor)
                ) {
                    if (isDownloaded) RingtoneEditView(p)
                    else LaunchedEffect(Unit) {
                        finishActivity()
                    }
                }

                LaunchedEffect(Unit) {
                    try {
                        val link = songDownload.download(p?.v?.songID ?: "").first()
                        if (link == null) {
                            finishActivity()
                            return@LaunchedEffect
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            FileDownloaderInChunks(link) { progress, status ->
                                if (status == false) finishActivity()

                                isDownloaded = progress == 100
                            }.startDownloadingRingtone()
                        }

                    } catch (e: Exception) {
                        finishActivity()
                    }
                }
            }
        }
    }

    private fun finishActivity() {
        finish()
        resources.getString(R.string.no_ringtone_found_try_again_later).toast()
    }

    override fun onStart() {
        super.onStart()

        if (player.isPlaying) {
            player.pause()
            resources.getString(R.string.playing_song_is_paused_for_ringtone).toast()
        }
    }
}
