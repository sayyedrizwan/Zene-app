package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.songSpeedDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum
import com.rizwansayyed.zene.service.player.utils.sleepTimerSelected
import com.rizwansayyed.zene.ui.main.view.AddToPlaylistsView
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.view.VideoSpeedChangeAlert
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.MiniWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerControlPanel(
    modifier: Modifier = Modifier,
    player: MusicPlayerData?,
    viewModel: PlayerViewModel,
    pagerStateMain: PagerState
) {
    val coroutine = rememberCoroutineScope()

    var songSpeedView by remember { mutableStateOf(false) }
    var addToPlaylistView by remember { mutableStateOf(false) }
    var showShareView by remember { mutableStateOf(false) }
    var showTimerSheet by remember { mutableStateOf(false) }
    val isShuffleEnabled by isShuffleDB.collectAsState(false)
    val isLoopEnabled by isLoopDB.collectAsState(false)
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    val videoSpeed by songSpeedDB.collectAsState(null)

    Column(
        modifier
            .padding(horizontal = 15.dp, vertical = 10.dp)
            .padding(bottom = 70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .padding(horizontal = 12.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        if (player?.data?.type() == MusicDataTypes.RADIO) {
            TextViewSemiBold(stringResource(R.string.live_streaming), size = 14, center = true)
        } else {
            if (player?.totalDuration?.contains("-") == false && player.currentDuration?.contains("-") == false) {
                Slider(value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    onValueChangeFinished = { getPlayerS()?.seekTo(sliderPosition.toLong()) },
                    valueRange = 0f..(player.totalDuration?.toFloatOrNull() ?: 1f),
                    colors = SliderDefaults.colors(Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    track = { sliderState ->
                        SliderDefaults.Track(
                            modifier = Modifier.height(4.dp),
                            sliderState = sliderState,
                            drawStopIndicator = null,
                            thumbTrackGapSize = 0.dp,
                            colors = SliderDefaults.colors(
                                Color.White, MainColor, MainColor, Color.Gray, Color.Gray
                            )
                        )
                    })
            }
            Spacer(Modifier.height(5.dp))

            Row(Modifier.fillMaxWidth()) {
                TextViewSemiBold(player?.currentDuration() ?: "", size = 13)
                Spacer(Modifier.weight(1f))
                TextViewSemiBold(player?.totalDuration() ?: "", size = 13)
            }
        }
        Spacer(Modifier.height(9.dp))

        if (pagerStateMain.currentPage == 1) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                Box(Modifier.clickable {
                    viewModel.likeAItem(player?.data, !viewModel.isItemLiked)
                }) {
                    if (viewModel.isItemLiked) ImageIcon(R.drawable.ic_thumbs_up, 22, Color.Red)
                    else ImageIcon(R.drawable.ic_thumbs_up, 22)
                }

                Box(Modifier.clickable {
                    isShuffleDB = flowOf(!isShuffleEnabled)
                }) {
                    ImageIcon(
                        if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle,
                        22
                    )
                }

                Box(Modifier.clickable {
                    getPlayerS()?.toBackSong()
                }) {
                    ImageIcon(R.drawable.ic_backward, 27)
                }

                Box(Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(100))
                    .clickable {
                        if (player!!.state == YoutubePlayerState.PLAYING) getPlayerS()?.pause()
                        else getPlayerS()?.play()
                    }
                    .background(Color.White)
                    .padding(10.dp)) {
                    when (player?.state) {
                        YoutubePlayerState.PLAYING -> ImageIcon(
                            R.drawable.ic_pause,
                            27,
                            Color.Black
                        )

                        YoutubePlayerState.BUFFERING -> CircularProgressIndicator(
                            Modifier.size(26.dp), Color.White, 4.dp, MainColor
                        )

                        else -> ImageIcon(R.drawable.ic_play, 27, Color.Black)
                    }
                }
                Box(Modifier.clickable {
                    getPlayerS()?.toNextSong()
                }) {
                    ImageIcon(R.drawable.ic_forward, 27)
                }

                Box(Modifier.clickable {
                    isLoopDB = flowOf(!isLoopEnabled)
                }) {
                    ImageIcon(
                        if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat,
                        22
                    )
                }

                Box(Modifier.clickable { showTimerSheet = true }) {
                    ImageIcon(
                        R.drawable.ic_timer,
                        22,
                        if (sleepTimerSelected == SleepTimerEnum.TURN_OFF) Color.White else MainColor
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                MiniWithImageAndBorder(R.drawable.ic_share, R.string.share) {
                    showShareView = true
                }

                if (player?.data?.type() == MusicDataTypes.SONGS) {
                    MiniWithImageAndBorder(R.drawable.ic_video_replay, R.string.song_video) {

                    }

                    MiniWithImageAndBorder(R.drawable.ic_teaching, R.string.lyrics_video) {

                    }
                }

                if (player?.data?.type() == MusicDataTypes.SONGS ||
                    player?.data?.type() == MusicDataTypes.AI_MUSIC
                ) MiniWithImageAndBorder(R.drawable.ic_note, R.string.lyrics) {
                    coroutine.launch {
                        pagerStateMain.animateScrollToPage(0)
                    }
                } else
                    MiniWithImageAndBorder(R.drawable.ic_information_circle, R.string.info) {
                        coroutine.launch {
                            pagerStateMain.animateScrollToPage(0)
                        }
                    }


                if (player?.data?.type() == MusicDataTypes.PODCAST_AUDIO)
                    MiniWithImageAndBorder(R.drawable.ic_music_note, R.string.similar_podcasts) {
                        coroutine.launch {
                            pagerStateMain.animateScrollToPage(2)
                        }
                    }
                else
                    MiniWithImageAndBorder(R.drawable.ic_music_note, R.string.similar_songs) {
                        coroutine.launch {
                            pagerStateMain.animateScrollToPage(2)
                        }
                    }

                MiniWithImageAndBorder(R.drawable.ic_playlist, R.string.add_to_playlist) {
                    addToPlaylistView = true
                }

                MiniWithImageAndBorder(R.drawable.ic_dashboard_speed, R.string.playback_speed) {
                    songSpeedView = true
                }

            }
            Spacer(Modifier.height(20.dp))
        }
        Spacer(Modifier.height(20.dp))
    }

    LaunchedEffect(player?.currentDuration) {
        sliderPosition = player?.currentDuration?.toFloatOrNull() ?: 1f
    }

    if (showTimerSheet) SleepTimerSheet {
        showTimerSheet = false
    }

    if (showShareView) ShareDataView(player?.data) {
        showShareView = false
    }

    if (addToPlaylistView) AddToPlaylistsView(player?.data) {
        addToPlaylistView = false
    }

    if (songSpeedView) Dialog(
        { songSpeedView = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val coroutines = rememberCoroutineScope()

        VideoSpeedChangeAlert(videoSpeed) {
            val name = it.name.replace("_", ".")
            coroutines.launch {
                songSpeedDB = flowOf(it)
            }
            getPlayerS()?.playbackRate(name)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerSheet(close: () -> Unit) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        { close() }, sheetState = state, contentColor = MainColor, containerColor = MainColor,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            TextViewBold(stringResource(R.string.sleep_timer), 23)
            Spacer(Modifier.height(25.dp))
            SleepTimerEnum.entries.forEach {
                Box(Modifier.clickable {
                    getPlayerS()?.sleepTimer(it)
                    close()
                }) {
                    when (it) {
                        SleepTimerEnum.SIXTY_MINUTES -> TextViewNormal(
                            stringResource(R.string.one_hour), 16
                        )

                        SleepTimerEnum.END_OF_TRACK -> TextViewNormal(
                            stringResource(R.string.end_of_track), 16
                        )

                        SleepTimerEnum.TURN_OFF -> if (sleepTimerSelected != SleepTimerEnum.TURN_OFF) TextViewNormal(
                            stringResource(R.string.turn_off), 16
                        )

                        else -> TextViewNormal("${it.time} ${stringResource(R.string.minutes)}", 16)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}
