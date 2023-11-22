package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.GreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.formatExoplayerDuration
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils.onClickOn3SecDelay
import com.rizwansayyed.zene.service.player.listener.PlayServiceListener
import com.rizwansayyed.zene.service.player.listener.PlayerServiceInterface
import com.rizwansayyed.zene.service.player.utils.Utils.seekToTimestamp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerSliders(player: ExoPlayer) {
    Spacer(Modifier.height(5.dp))
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val coroutine = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    var totalTime by remember { mutableStateOf("0:00") }
    var currentTime by remember { mutableStateOf("0:00") }
    var valueEndRange by remember { mutableFloatStateOf(1f) }

    Row(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.height(3.dp))
        TextRegular(currentTime, Modifier.padding(start = 4.dp), size = 15)

        Slider(
            sliderPosition, {
                sliderPosition = it
                currentTime = formatExoplayerDuration(it.toLong())
                seekToTimestamp(it.toLong())
            }, Modifier.weight(1f),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.White,
                inactiveTrackColor = GreyColor
            ),
            thumb = {},
            valueRange = 0f..valueEndRange
        )
        if (totalTime.contains("-"))
            TextRegular("0:00", Modifier.padding(end = 4.dp), size = 15)
        else
            TextRegular(totalTime, Modifier.padding(end = 4.dp), size = 15)

        Spacer(Modifier.height(3.dp))
    }

    DisposableEffect(Unit) {
        totalTime = formatExoplayerDuration(player.duration)
        job = coroutine.launch {
            while (true) {
                currentTime = formatExoplayerDuration(player.currentPosition)
                totalTime = formatExoplayerDuration(player.duration)
                valueEndRange = if (player.duration.toString().contains("-")) 1f
                else player.duration.toFloat()
                sliderPosition = player.currentPosition.toFloat()

                delay(1.seconds)
            }
        }
        onDispose {
            job?.cancel()
        }
    }
}

@Composable
fun MusicPlayerButtons(player: ExoPlayer) {
    var isLoading by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    Row(
        Modifier
            .padding(top = 11.dp)
            .padding(horizontal = 30.dp)
            .fillMaxSize(),
        Arrangement.SpaceBetween, Alignment.CenterVertically
    ) {

        SmallIcons(R.drawable.ic_song_previous, 25) {
            onClickOn3SecDelay {
                player.seekToPrevious()
            }
        }

        if (isLoading)
            CircularProgressIndicator(
                modifier = Modifier.width(30.dp),
                color = Color.White,
                trackColor = MainColor,
            )
        else
            if (isPlaying)
                SmallIcons(R.drawable.ic_pause, 38) {
                    player.pause()
                }
            else
                SmallIcons(R.drawable.ic_play, 38) {
                    player.play()
                }

        SmallIcons(R.drawable.ic_song_next, 25) {
            onClickOn3SecDelay {
                player.seekToNext()
            }
        }
    }

    DisposableEffect(Unit) {
        val playerListener = object : PlayerServiceInterface {
            override fun songBuffering(b: Boolean) {
                isLoading = b
            }

            override fun mediaItemUpdate(mediaItem: MediaItem) {
                isLoading = !mediaItem.requestMetadata.mediaUri.toString().contains("https://")
            }

            override fun playingStateChange() {
                super.playingStateChange()
                isPlaying = player.isPlaying
            }

        }
        PlayServiceListener.getInstance().addListener(playerListener)
        isPlaying = player.isPlaying

        onDispose {
            PlayServiceListener.getInstance().rmListener(playerListener)
        }
    }
}