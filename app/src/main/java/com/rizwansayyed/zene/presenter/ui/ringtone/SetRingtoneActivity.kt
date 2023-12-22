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
import androidx.compose.ui.res.stringResource
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
                var url by remember { mutableStateOf("") }

                val noRingtoneFound = stringResource(R.string.no_ringtone_found_try_again_later)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkGreyColor)
                ) {

                }

                LaunchedEffect(Unit) {
                    fun finishActivity() {
                        finish()
                        noRingtoneFound.toast()
                    }

                    songDownload.download(p?.v?.songID ?: "").catch {
                        finishActivity()
                    }.collectLatest {
                        if (it == null) finishActivity()
                        else url = it
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (player.isPlaying) {
            player.pause()
            resources.getString(R.string.playing_song_is_paused_for_ringtone).toast()
        }
    }
}
