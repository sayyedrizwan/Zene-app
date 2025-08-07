package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.songSpeedDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum
import com.rizwansayyed.zene.service.player.utils.sleepTimerSelected
import com.rizwansayyed.zene.ui.main.view.share.ShareDataView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.view.VideoSpeedChangeAlert
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun SongSlider(data: MusicPlayerData?, pagerState: PagerState) {
    HorizontalPager(
        pagerState,
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) { page ->
        val lists = data?.lists.orEmpty()
        if (lists.isNotEmpty() && page in lists.indices) {
            lists[page]?.let { item ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    GlideImage(
                        item.thumbnail, item.name,
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(horizontal = 5.dp),
                        contentScale = ContentScale.Crop
                    )
                    if (item.id != data?.data?.id) {
                        Box(
                            Modifier
                                .padding(15.dp)
                                .align(Alignment.BottomEnd)
                                .clip(RoundedCornerShape(100))
                                .background(MainColor)
                                .combinedClickable(
                                    onLongClick = { NavigationUtils.triggerInfoSheet(item) },
                                    onClick = { startMedia(item, lists) }
                                )
                                .padding(10.dp)
                        ) {
                            ImageIcon(R.drawable.ic_play, 24, Color.White)
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(data?.data?.id, data?.lists) {
        try {
            val lists = data?.lists.orEmpty()
            val index = lists.indexOfFirst { it?.id == data?.data?.id }.takeIf { it >= 0 } ?: 0
            pagerState.scrollToPage(index.coerceIn(lists.indices))
        } catch (_: Exception) {
        }
    }
}

@Composable
fun PlayerButtonControl(player: MusicPlayerData?) {
    val isLoopEnabled by isLoopDB.collectAsState(false)
    val isShuffleEnabled by isShuffleDB.collectAsState(false)

    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                isLoopDB = flowOf(!isLoopEnabled)
            }) {
            ImageIcon(
                if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat, 22
            )
        }

        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                getPlayerS()?.toBackSong()
            }) {
            ImageIcon(R.drawable.ic_backward, 27)
        }

        Box(
            Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(100))
                .clickable {
                    if (player?.isPlaying() == true) getPlayerS()?.pause()
                    else getPlayerS()?.play()
                }
                .background(Color.White)
                .padding(10.dp)) {
            when (player?.state) {
                YoutubePlayerState.PLAYING -> ImageIcon(
                    R.drawable.ic_pause, 27, Color.Black
                )

                YoutubePlayerState.BUFFERING, YoutubePlayerState.UNSTARTED -> CircularProgressIndicator(
                    Modifier.size(26.dp), Color.White, 4.dp, MainColor
                )

                else -> ImageIcon(R.drawable.ic_play, 27, Color.Black)
            }
        }

        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                getPlayerS()?.toNextSong()
            }) {
            ImageIcon(R.drawable.ic_forward, 27)
        }

        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                isShuffleDB = flowOf(!isShuffleEnabled)
            }) {
            ImageIcon(
                if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle, 22
            )
        }
    }
}


@Composable
fun PlayerItemButtonView(player: MusicPlayerData?, viewModel: PlayerViewModel) {
    var showShareView by remember { mutableStateOf(false) }
    var songSpeedView by remember { mutableStateOf(false) }
    val videoSpeed by songSpeedDB.collectAsState(null)
    var showTimerSheet by remember { mutableStateOf(false) }

    Spacer(Modifier.height(10.dp))
    Row(
        Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        if (player?.data?.type() == MusicDataTypes.SONGS) {
            when (val v = viewModel.videoForSongs) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> CircularLoadingViewSmall()
                is ResponseResult.Success -> {
                    if (v.data.videoID?.id != null) {
                        Box(
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                startMedia(v.data.videoID)
                            }) {
                            ImageIcon(R.drawable.ic_video_replay, 22)
                        }
                    }

                    if (v.data.lyricsID?.id != null) {
                        Spacer(Modifier.width(25.dp))
                        Box(
                            Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }) {
                                startMedia(v.data.lyricsID)
                            }) {
                            ImageIcon(R.drawable.ic_teaching, 22)
                        }
                    }
                }
            }


        }

        Spacer(Modifier.weight(1f))

        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                songSpeedView = true
            }) {
            ImageIcon(R.drawable.ic_dashboard_speed, 22)
        }

        Spacer(Modifier.width(25.dp))
        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                showTimerSheet = true
            }) {
            ImageIcon(
                R.drawable.ic_timer,
                22,
                if (sleepTimerSelected == SleepTimerEnum.TURN_OFF) Color.White else Color.Red
            )
        }

        Spacer(Modifier.width(25.dp))
        Box(
            Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                showShareView = true
            }) {
            ImageIcon(R.drawable.ic_share, 22)
        }
    }


    if (showShareView) ShareDataView(player?.data) {
        showShareView = false
    }

    if (showTimerSheet) SleepTimerSheet {
        showTimerSheet = false
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
fun PlayerControlView(player: MusicPlayerData?) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val interactionSource by remember { mutableStateOf(MutableInteractionSource()) }

    if (player?.data?.type() == MusicDataTypes.RADIO) {
        TextViewSemiBold(stringResource(R.string.live_streaming), size = 14, center = true)
    } else {
        if (player?.totalDuration?.contains("-") == false && player.currentDuration?.contains("-") == false) {
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                onValueChangeFinished = { getPlayerS()?.seekTo(sliderPosition.toLong()) },
                valueRange = 0f..(player.totalDuration?.toFloatOrNull() ?: 1f),
                colors = SliderDefaults.colors(Color.White),
                modifier = Modifier
                    .padding(horizontal = 11.dp)
                    .fillMaxWidth(),
                track = { sliderState ->
                    SliderDefaults.Track(
                        modifier = Modifier.height(7.dp),
                        sliderState = sliderState,
                        drawStopIndicator = null,
                        thumbTrackGapSize = 2.dp,
                        colors = SliderDefaults.colors(
                            Color.White, Color.White, MainColor, Color.Gray, Color.Gray
                        )
                    )
                },
                interactionSource = interactionSource,
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = interactionSource,
                        thumbSize = DpSize(6.dp, 24.dp),
                        colors = SliderDefaults.colors(Color.White)
                    )
                },
            )
        }

        Row(
            Modifier
                .offset(y = (-10).dp)
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
        ) {
            TextViewSemiBold(player?.currentDuration() ?: "", size = 13)
            Spacer(Modifier.weight(1f))
            TextViewSemiBold(player?.totalDuration() ?: "", size = 13)
        }
    }
    Spacer(Modifier.height(9.dp))


    LaunchedEffect(player?.currentDuration) {
        sliderPosition = player?.currentDuration?.toFloatOrNull() ?: 1f
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
