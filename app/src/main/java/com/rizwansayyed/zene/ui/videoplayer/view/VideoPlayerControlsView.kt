package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.model.formatTotalDuration
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins

@Composable
fun VideoPlayerControls(modifier: Modifier = Modifier, webApp: WebAppInterface) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Box(
        modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
    ) {
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 5.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(6.dp))
            ImageIcon(R.drawable.ic_setting, 27) {

            }
            Spacer(Modifier.width(5.dp))
            ImageIcon(R.drawable.ic_closed_caption, 27) {

            }
            Spacer(Modifier.width(5.dp))
            ImageIcon(R.drawable.ic_dashboard_speed, 27) {

            }
            Spacer(Modifier.width(5.dp))
            ImageIcon(R.drawable.ic_picture_in_picture_on, 27) {

            }
            Spacer(Modifier.width(5.dp))
            Spacer(
                Modifier
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color.White)
                    .size(5.dp, 27.dp)
            )
            Spacer(Modifier.width(5.dp))

            Spacer(Modifier.width(5.dp))
            if (webApp.isBuffering) LoadingView(Modifier.size(23.dp))
            else ImageIcon(if (webApp.isPlaying) R.drawable.ic_pause else R.drawable.ic_play, 27) {
                if (webApp.isPlaying) webApp.pause()
                else webApp.play()
            }

            Spacer(Modifier.width(5.dp))
            ImageIcon(R.drawable.ic_add_playlist, 25) {

            }
            Spacer(Modifier.width(5.dp))

            TextPoppins(formatTotalDuration(webApp.currentDuration), size = 18)
            TextPoppins("/", size = 18)
            TextPoppins(formatTotalDuration(webApp.totalDuration), size = 18)
            Spacer(Modifier.width(6.dp))
            Slider(
                sliderPosition,
                { sliderPosition = it },
                modifier
                    .padding(horizontal = 5.dp)
                    .weight(1f),
                true, 0f..webApp.totalDuration.toFloat(),
                onValueChangeFinished = {
                    webApp.seekTo(sliderPosition.toInt())
                }
            )
        }
    }

    LaunchedEffect(webApp.currentDuration) {
        sliderPosition = webApp.currentDuration.toFloat()
    }
}