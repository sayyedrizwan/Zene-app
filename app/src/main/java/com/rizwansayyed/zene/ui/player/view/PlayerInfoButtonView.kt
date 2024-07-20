package com.rizwansayyed.zene.ui.player.view

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.musicAutoplaySettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicLoopSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.data.db.model.MusicSpeed.*
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAYBACK_RATE
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.view.BorderButtons
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongSliderData(playerInfo: MusicPlayerData?) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.width(6.dp))
        TextPoppins(playerInfo?.formatCurrentDuration() ?: "0:00", false, size = 16)
        Spacer(Modifier.width(6.dp))

        Slider(value = sliderPosition,
            onValueChange = { sliderPosition = it },
            Modifier.weight(1f),
            valueRange = 0f..(playerInfo?.totalDuration ?: 0).toFloat(),
            colors = SliderColors(
                activeTickColor = Color.White,
                activeTrackColor = Color.White,
                disabledActiveTickColor = Color.DarkGray,
                disabledInactiveTickColor = Color.DarkGray,
                disabledInactiveTrackColor = Color.DarkGray,
                disabledThumbColor = Color.DarkGray,
                inactiveTickColor = Color.DarkGray,
                inactiveTrackColor = Color.DarkGray,
                thumbColor = Color.White,
                disabledActiveTrackColor = Color.White,
            ),
            thumb = {
                Spacer(Modifier.padding(5.dp))
            },
            onValueChangeFinished = {
                sendWebViewCommand(SEEK_DURATION_VIDEO, sliderPosition.toInt())
            })

        Spacer(Modifier.width(6.dp))
        TextPoppins(playerInfo?.formatTotalDuration() ?: "0:00", false, size = 16)
        Spacer(Modifier.width(6.dp))
    }

    LaunchedEffect(playerInfo?.currentDuration) {
        sliderPosition = (playerInfo?.currentDuration ?: 0).toFloat()
    }
}

@Composable
fun ButtonsView(playerInfo: MusicPlayerData?) {
    Spacer(Modifier.height(15.dp))

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
        Box(Modifier.rotate(180f)) {
            ImageIcon(R.drawable.ic_next) {
                sendWebViewCommand(PREVIOUS_SONG)
            }
        }

        if (playerInfo?.isBuffering == true) LoadingView(Modifier.size(32.dp))
        else ImageIcon(if (playerInfo?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play) {
            if (playerInfo?.isPlaying == true) sendWebViewCommand(PAUSE_VIDEO)
            else sendWebViewCommand(PLAY_VIDEO)
        }

        ImageIcon(R.drawable.ic_next) {
            sendWebViewCommand(NEXT_SONG)
        }
    }
}


@Composable
fun ExtraButtonsData(musicPlayerViewModel: MusicPlayerViewModel) {
    Spacer(Modifier.height(30.dp))

    val tryAgain = stringResource(id = R.string.try_after_a_seconds_fetching_video_details)

    var expanded by remember { mutableStateOf(false) }
    val musicSpeed by musicSpeedSettings.collectAsState(initial = `1`)
    val musicLoop by musicLoopSettings.collectAsState(initial = false)
    val musicAutoplay by musicAutoplaySettings.collectAsState(initial = false)

    Row {
        BorderButtons(
            Modifier
                .weight(1f)
                .clickable {
                    when (val v = musicPlayerViewModel.videoDetails) {
                        is APIResponse.Success -> {
                            openVideoPlayer(v.data.officialVideoID)
                        }
                        else -> tryAgain.toast()
                    }
                }, R.drawable.ic_flim_slate, R.string.song_video
        )
        BorderButtons(
            Modifier
                .weight(1f)
                .clickable {
                    when (val v = musicPlayerViewModel.videoDetails) {
                        is APIResponse.Success -> {
                            openVideoPlayer(v.data.lyricsVideoID)
                        }
                        else -> tryAgain.toast()
                    }
                }, R.drawable.ic_closed_caption, R.string.lyrics_video
        )
    }

    Spacer(Modifier.height(30.dp))

    LazyRow(
        Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            val loopEnabled = stringResource(R.string.looping_song_enabled)
            val loopDisabled = stringResource(R.string.looping_song_disabled)

            Box {
                ImgButton(R.drawable.ic_repeat) {
                    if (musicLoop) loopDisabled.toast()
                    else loopEnabled.toast()
                    musicLoopSettings = flowOf(!musicLoop)
                }

                AnimatedVisibility(
                    musicLoop, Modifier
                        .padding(top = 27.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(
                        Modifier
                            .size(4.dp)
                            .clip(RoundedCornerShape(100))
                            .background(Color.White)
                    )
                }
            }
        }

        item {
            val autoplayEnabled = stringResource(R.string.autoplay_song_enabled)
            val autoplayDisabled = stringResource(R.string.autoplay_song_disabled)

            Box {
                ImgButton(R.drawable.ic_go_forward) {
                    if (musicAutoplay) autoplayDisabled.toast()
                    else autoplayEnabled.toast()
                    musicAutoplaySettings = flowOf(!musicAutoplay)
                }

                AnimatedVisibility(
                    musicAutoplay, Modifier
                        .padding(top = 27.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(
                        Modifier
                            .size(4.dp)
                            .clip(RoundedCornerShape(100))
                            .background(Color.White)
                    )
                }
            }
        }

        item {
            ImgButton(R.drawable.ic_add_playlist) {

            }
        }

        item {
            Box {
                TextButton(musicSpeed.data) {
                    expanded = true
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    MusicSpeed.entries.forEach {
                        DropdownMenuItem(text = { TextPoppins(it.data, true, size = 16) },
                            onClick = {
                                musicSpeedSettings = flowOf(it)
                                expanded = false
                                CoroutineScope(Dispatchers.IO).launch {
                                    delay(1.seconds)
                                    sendWebViewCommand(PLAYBACK_RATE)
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
private fun ImgButton(i: Int, onclick: () -> Unit) {
    Box(
        Modifier
            .padding(horizontal = 20.dp)
            .clickable { onclick() }) {
        ImageIcon(i, 21)
    }
}

@Composable
private fun TextButton(i: String, onclick: () -> Unit) {
    Box(
        Modifier
            .padding(horizontal = 20.dp)
            .clickable { onclick() }) {
        TextPoppins(v = i, size = 18)
    }
}