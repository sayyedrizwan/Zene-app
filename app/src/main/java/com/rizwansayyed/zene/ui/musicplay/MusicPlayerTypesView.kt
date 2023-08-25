package com.rizwansayyed.zene.ui.musicplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerResponse
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerStatus
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerStatus.*
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerViewStatus
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

@Composable
fun MusicPlayerLyrics(nav: HomeNavViewModel, song: SongsViewModel = hiltViewModel()) {

    val musicPlayer by BaseApplication.dataStoreManager.musicPlayerData
        .collectAsState(initial = runBlocking(Dispatchers.IO) { BaseApplication.dataStoreManager.musicPlayerData.first() })

    val noLyricsFound = stringResource(id = R.string.no_lyrics_found)

    Spacer(modifier = Modifier.padding(top = 30.dp))

    QuickSandBold(
        "${musicPlayer?.songName} ${stringResource(id = R.string.lyrics)}",
        Modifier
            .padding(10.dp)
            .fillMaxWidth(), size = 25
    )

    when (song.videoLyricsDetails.status) {
        LOADING ->
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                CircularProgressIndicator(Modifier.size(30.dp), Color.White)
            }

        ERROR -> {
            Spacer(modifier = Modifier.height(50.dp))
            QuickSandRegular(noLyricsFound, Modifier.fillMaxWidth())
        }

        SUCCESS -> {
            if (song.videoLyricsDetails.data?.trim()?.isEmpty() == true) {
                noLyricsFound.showToast()
                nav.musicViewType(VideoPlayerViewStatus.MUSIC)
            } else {
                QuickSandRegular(song.videoLyricsDetails.data ?: "", Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 2))
            }

        }
    }

    LaunchedEffect(Unit) {
        val songs = "${musicPlayer?.songName} - ${musicPlayer?.artists}"
        song.readLyrics(songs)
        delay(1.seconds)
        song.readLyrics(songs)
    }
}