package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerControlPanel(
    modifier: Modifier = Modifier, player: MusicPlayerData?, viewModel: PlayerViewModel
) {
    val isShuffleEnabled by isShuffleDB.collectAsState(false)
    val isLoopEnabled by isLoopDB.collectAsState(false)
    var sliderPosition by remember { mutableFloatStateOf(0f) }

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

        Slider(value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                getPlayerS()?.seekTo(it)
            },
            valueRange = 0f..(player?.totalDuration?.toFloatOrNull() ?: 0f),
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

        Spacer(Modifier.height(5.dp))

        Row(Modifier.fillMaxWidth()) {
            TextViewSemiBold(player?.currentDuration() ?: "", size = 13)
            Spacer(Modifier.weight(1f))
            TextViewSemiBold(player?.totalDuration() ?: "", size = 13)
        }

        Spacer(Modifier.height(9.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {

            Box(Modifier.clickable {
                viewModel.likeAItem(player?.data, !viewModel.isItemLiked)
            }) {
                if (viewModel.isItemLiked)
                    ImageIcon(R.drawable.ic_thumbs_up, 22, Color.Red)
                else
                    ImageIcon(R.drawable.ic_thumbs_up, 22)
            }

            Box(Modifier.clickable {
                isShuffleDB = flowOf(!isShuffleEnabled)
            }) {
                ImageIcon(
                    if (isShuffleEnabled) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle,
                    22
                )
            }

            Box(
                Modifier
                    .rotate(180f)
                    .clickable {
                        getPlayerS()?.toBackSong()
                    }) {
                ImageIcon(R.drawable.ic_forward, 27)
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
                    YoutubePlayerState.PLAYING -> ImageIcon(R.drawable.ic_pause, 27, Color.Black)
                    YoutubePlayerState.BUFFERING -> CircularProgressIndicator(
                        Modifier.size(26.dp), Color.White, 4.dp, MainColor
                    )

                    else -> ImageIcon(R.drawable.ic_play, 27, Color.Black)
                }
            }
            Box(
                Modifier
                    .clickable {
                        getPlayerS()?.toNextSong()
                    }) {
                ImageIcon(R.drawable.ic_forward, 27)
            }

            Box(Modifier.clickable {
                isLoopDB = flowOf(!isLoopEnabled)
            }) {
                ImageIcon(if (isLoopEnabled) R.drawable.ic_repeat_one else R.drawable.ic_repeat, 22)
            }

            Box(Modifier) {
                ImageIcon(R.drawable.ic_timer, 22)
            }
        }


        Spacer(Modifier.height(40.dp))
    }

    LaunchedEffect(player?.currentDuration) {
        sliderPosition = player?.currentDuration?.toFloatOrNull() ?: 0f
    }
}