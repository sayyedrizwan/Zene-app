package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel


@Composable
fun VideoPlayerControlView(viewModel: PlayingVideoInfoViewModel) {
    Box(Modifier
        .clickable { viewModel.showControlView(false) }
        .fillMaxSize()
        .background(Color.Black.copy(0.7f))) {
        VideoPlayerInfoView(viewModel, Modifier.align(Alignment.TopStart))

        VideoPlayerBottomControl(viewModel, Modifier.align(Alignment.BottomCenter))

        QualityVideoView(viewModel, Modifier.align(Alignment.TopEnd))
    }
}

@Composable
fun QualityVideoView(viewModel: PlayingVideoInfoViewModel, modifier: Modifier) {
    Column(modifier.padding(top = 10.dp, end = 30.dp), Arrangement.Center, Alignment.End) {
        ButtonWithBorder(R.string.one_forty_p) {

        }
        Spacer(Modifier.height(14.dp))
        ButtonWithBorder(R.string.seven_twenty_p, Color.Gray) {

        }
        Spacer(Modifier.height(14.dp))
        ButtonWithBorder(R.string.four_eighty_p, Color.Gray) {

        }
        Spacer(Modifier.height(14.dp))
        Spacer(
            Modifier
                .width(100.dp)
                .height(1.5.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White)
        )
        Spacer(Modifier.height(14.dp))

        Row {
            ImageWithBorder(R.drawable.ic_camera) { }
            ImageWithBorder(R.drawable.ic_link_backward) { }
            ImageWithBorder(R.drawable.ic_playlist) { }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerBottomControl(viewModel: PlayingVideoInfoViewModel, modifier: Modifier) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Row(
        modifier
            .fillMaxWidth()
            .padding(15.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        Row(
            Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 12.dp),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            TextViewNormal(
                formatDurationsForVideo(
                    viewModel.videoCurrentTimestamp, viewModel.videoDuration
                ).first, size = 13
            )

            Slider(value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    viewModel.showControlView(true)
                    viewModel.webView?.evaluateJavascript("seekTo(${it});", null)
                },
                valueRange = 0f..viewModel.videoDuration,
                colors = SliderDefaults.colors(Color.White),
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)
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

            TextViewNormal(
                formatDurationsForVideo(
                    viewModel.videoCurrentTimestamp, viewModel.videoDuration
                ).second, size = 13
            )
        }

        Row(
            Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MainColor)
                .padding(horizontal = 10.dp, vertical = 7.dp)
        ) {

            Box(Modifier.clickable {
                viewModel.showControlView(true)
                if (viewModel.playerState == PlayingVideoInfoViewModel.Companion.YoutubePlayerState.PLAYING) {
                    viewModel.webView?.evaluateJavascript("pauseVideo();", null)
                } else {
                    viewModel.webView?.evaluateJavascript("playVideo();", null)
                }
            }) {
                if (viewModel.playerState == PlayingVideoInfoViewModel.Companion.YoutubePlayerState.PLAYING) {
                    ImageIcon(R.drawable.ic_pause, 20)
                } else {
                    ImageIcon(R.drawable.ic_play, 20)
                }
            }
        }
    }

    LaunchedEffect(viewModel.videoCurrentTimestamp) {
        sliderPosition = viewModel.videoCurrentTimestamp
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoPlayerInfoView(viewModel: PlayingVideoInfoViewModel, modifier: Modifier) {
    Column(modifier.padding(top = 10.dp, start = 20.dp)) {
        GlideImage(
            viewModel.videoThumbnail,
            viewModel.videoName,
            Modifier.size(130.dp, 100.dp),
            contentScale = ContentScale.Crop
        )

        TextViewSemiBold(viewModel.videoName, size = 20)
        TextViewNormal(viewModel.videoAuthor, size = 13)
    }
}