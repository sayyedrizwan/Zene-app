package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.model.formatTotalDuration
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerControls(
    modifier: Modifier = Modifier, webApp: WebAppInterface, pip: () -> Unit, doHide: () -> Unit
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
    ) {
        Spacer(
            Modifier
                .fillMaxSize()
                .padding(bottom = 30.dp)
                .clickable { doHide() })

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .heightIn(min = 50.dp)
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                ),
            colors = CardColors(MainColor, MainColor, MainColor, MainColor)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 9.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(6.dp))
                VideoSettingsInfoView(webApp)
                Spacer(Modifier.width(5.dp))
                VideoSettingsCaption(webApp)
                Spacer(Modifier.width(5.dp))
                VideoPlaybackSettings(webApp)
                Spacer(Modifier.width(5.dp))
                ImageIcon(R.drawable.ic_picture_in_picture_on, 27) {
                    pip()
                }
                Spacer(Modifier.width(5.dp))
                Spacer(
                    Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(White)
                        .size(5.dp, 27.dp)
                )
                Spacer(Modifier.width(5.dp))

                Spacer(Modifier.width(5.dp))
                if (webApp.isBuffering) LoadingView(Modifier.size(23.dp))
                else ImageIcon(
                    if (webApp.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    27
                ) {
                    if (webApp.isPlaying) webApp.pause()
                    else webApp.play()
                }

                Spacer(Modifier.width(5.dp))

                TextPoppins(formatTotalDuration(webApp.currentDuration), size = 18)
                TextPoppins("/", size = 18)
                TextPoppins(formatTotalDuration(webApp.totalDuration), size = 18)
                Spacer(Modifier.width(10.dp))

                Slider(value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    Modifier.weight(1f),
                    valueRange = 0f..(webApp.totalDuration).toFloat(),
                    colors = SliderColors(
                        activeTickColor = DarkCharcoal,
                        activeTrackColor = DarkCharcoal,
                        disabledActiveTickColor = Color.DarkGray,
                        disabledInactiveTickColor = Color.DarkGray,
                        disabledInactiveTrackColor = Color.DarkGray,
                        disabledThumbColor = Color.DarkGray,
                        inactiveTickColor = Color.DarkGray,
                        inactiveTrackColor = Color.DarkGray,
                        thumbColor = DarkCharcoal,
                        disabledActiveTrackColor = DarkCharcoal,
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
                        webApp.seekTo(sliderPosition.toInt())
                    })

                Spacer(Modifier.width(10.dp))
                ImageIcon(R.drawable.ic_add_playlist, 24) {

                }
                Spacer(Modifier.width(5.dp))
                ImageIcon(R.drawable.ic_add_playlist, 24) {

                }
                Spacer(Modifier.width(6.dp))
            }
        }
    }
    LaunchedEffect(webApp.currentDuration) {
        sliderPosition = webApp.currentDuration.toFloat()
    }
}