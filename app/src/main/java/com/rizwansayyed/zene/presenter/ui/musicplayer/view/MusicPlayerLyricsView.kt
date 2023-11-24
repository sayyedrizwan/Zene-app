package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.formatExoplayerDuration
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun MusicPlayerLyrics(playerViewModel: PlayerViewModel, player: ExoPlayer) {
    val coroutine = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    var text by remember { mutableStateOf("") }


    if (playerViewModel.lyricsInfo?.lyrics?.isNotEmpty() == true) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(17.dp))
                .background(Color.Black)
                .padding(10.dp)
        ) {

            Spacer(Modifier.height(6.dp))
            TextSemiBold(stringResource(R.string.lyrics), size = 20)
            Spacer(Modifier.height(12.dp))
            TextRegular(v = text)
            Spacer(Modifier.height(6.dp))
        }
    }



    DisposableEffect(Unit) {
        job = coroutine.launch {
            while (true) {
                val v = playerViewModel.lyricsInfo

                if (v?.lyrics?.isNotEmpty() == true && v.subtitles) {
                    text = " "
                    val currentTime = formatExoplayerDuration(player.currentPosition)
                    if (!v.lyrics.substringAfter("[$currentTime").substringAfter("[")
                            .substringBefore("]").contains("00:00")
                    ) {
                        text = v.lyrics.substringAfter("[$currentTime").substringAfter("]")
                            .substringBefore("[")
                    }
                } else if (v?.lyrics?.isNotEmpty() == true){
                    text = v.lyrics.replace("[", "\n[")
                }

                delay(1.seconds)
            }
        }
        onDispose {
            job?.cancel()
        }
    }
}
