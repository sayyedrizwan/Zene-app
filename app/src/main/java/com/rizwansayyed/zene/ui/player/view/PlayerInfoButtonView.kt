package com.rizwansayyed.zene.ui.player.view

import android.os.Build
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.musicLoopSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.data.db.model.MusicSpeed.*
import com.rizwansayyed.zene.data.db.model.formatTotalDuration
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAYBACK_RATE
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.view.AddSongToPlaylist
import com.rizwansayyed.zene.ui.view.BorderButtons
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingText
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SheetDialogSheet
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.shareUrl
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.RADIO_ARTISTS
import com.rizwansayyed.zene.utils.Utils.URLS.LIKED_SONGS_ON_ZENE_PLAYLISTS
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
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
    val interactionSource = remember { MutableInteractionSource() }

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.width(6.dp))
        TextPoppins(formatTotalDuration(playerInfo?.currentDuration), false, size = 16)
        Spacer(Modifier.width(6.dp))

        Slider(value = sliderPosition,
            onValueChange = { sliderPosition = it },
            Modifier.weight(1f),
            valueRange = 0f..(playerInfo?.totalDuration ?: 0).toFloat(),
            colors = SliderColors(
                activeTickColor = White,
                activeTrackColor = White,
                disabledActiveTickColor = Color.DarkGray,
                disabledInactiveTickColor = Color.DarkGray,
                disabledInactiveTrackColor = Color.DarkGray,
                disabledThumbColor = Color.DarkGray,
                inactiveTickColor = Color.DarkGray,
                inactiveTrackColor = Color.DarkGray,
                thumbColor = White,
                disabledActiveTrackColor = White,
            ),
            thumb = {
                Spacer(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(White)
                        .width(4.dp)
                        .height(18.dp)
                )
            },
            interactionSource = interactionSource,
            onValueChangeFinished = {
                sendWebViewCommand(SEEK_DURATION_VIDEO, sliderPosition.toInt())
            })

        Spacer(Modifier.width(6.dp))
        TextPoppins(formatTotalDuration(playerInfo?.totalDuration), false, size = 16)
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
fun ExtraButtonsData(musicPlayerViewModel: MusicPlayerViewModel, playerInfo: MusicPlayerData?) {
    val tryAgain = stringResource(id = R.string.try_after_a_seconds_fetching_video_details)

    val viewModel: ZeneViewModel = hiltViewModel()
    var isLiked by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    var addToPlaylistSongs by remember { mutableStateOf(false) }
    val musicSpeed by musicSpeedSettings.collectAsState(initial = `1`)
    val musicLoop by musicLoopSettings.collectAsState(initial = false)

    if (playerInfo?.player?.id?.contains(RADIO_ARTISTS) == false) {
        Spacer(Modifier.height(30.dp))

        Row {
            BorderButtons(
                Modifier
                    .weight(1f)
                    .clickable {
                        when (val v = musicPlayerViewModel.videoDetails) {
                            is APIResponse.Success -> {
                                logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_SONG_VIDEO)
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
                                logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_SONG_LYRICS_VIDEO)
                                openVideoPlayer(v.data.lyricsVideoID)
                            }

                            else -> tryAgain.toast()
                        }
                    }, R.drawable.ic_closed_caption, R.string.lyrics_video
            )
        }
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
            when (val v = viewModel.isSongLiked) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> LoadingView(Modifier.size(12.dp))
                is APIResponse.Success -> {
                    AnimatedVisibility(isLiked) {
                        ImgButton(R.drawable.ic_liked) {
                            isLiked = !isLiked
                            viewModel.addRemoveSongFromPlaylists(
                                LIKED_SONGS_ON_ZENE_PLAYLISTS, playerInfo?.player?.id ?: "", false
                            )
                        }
                    }

                    AnimatedVisibility(!isLiked) {
                        ImgButton(R.drawable.ic_non_liked) {
                            isLiked = !isLiked
                            viewModel.addRemoveSongFromPlaylists(
                                LIKED_SONGS_ON_ZENE_PLAYLISTS, playerInfo?.player?.id ?: "", true
                            )
                        }
                    }
                    LaunchedEffect(Unit) {
                        isLiked = v.data
                    }
                }
            }
        }

        item {
            val loopEnabled = stringResource(R.string.looping_song_enabled)
            val loopDisabled = stringResource(R.string.looping_song_disabled)

            Box {
                ImgButton(R.drawable.ic_repeat) {
                    logEvents(FirebaseLogEvents.FirebaseEvents.MUSIC_LOOP_SETTINGS)
                    if (musicLoop) loopDisabled.toast()
                    else loopEnabled.toast()
                    musicLoopSettings = flowOf(!musicLoop)
                }

                AnimatedVisibility(
                    musicLoop,
                    Modifier
                        .padding(top = 27.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Spacer(
                        Modifier
                            .size(4.dp)
                            .clip(RoundedCornerShape(100))
                            .background(White)
                    )
                }
            }
        }

        item {
            ImgButton(R.drawable.ic_add_playlist) {
                addToPlaylistSongs = true
            }
        }

        item {
            ImgButton(R.drawable.ic_share) {
                playerInfo?.player?.let { shareTxtImage(shareUrl(it)) }
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
                                logEvents(FirebaseLogEvents.FirebaseEvents.PLAYBACK_SONG_RATE_SETTINGS)
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


    if (addToPlaylistSongs && playerInfo?.player != null) AddSongToPlaylist(playerInfo.player) {
        addToPlaylistSongs = false
    }

    LaunchedEffect(Unit) {
        playerInfo?.player?.id?.let { viewModel.isSongLiked(it) }
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