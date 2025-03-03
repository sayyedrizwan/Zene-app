package com.rizwansayyed.zene.ui.videoplayer.view

import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import androidx.activity.compose.LocalActivity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.LikeItemType
import com.rizwansayyed.zene.datastore.DataStorageManager.videoCCDB
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgAndBorder
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.utils.MainUtils.hasPIPPermission
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun VideoPlayerControlView(
    viewModel: PlayingVideoInfoViewModel,
    playerViewModel: PlayerViewModel,
    videoID: String?
) {
    Box(Modifier
        .clickable { viewModel.showControlView(false) }
        .fillMaxSize()
        .background(Color.Black.copy(0.7f))) {
        VideoPlayerInfoView(viewModel, Modifier.align(Alignment.TopStart))

        VideoPlayerBottomControl(viewModel, Modifier.align(Alignment.BottomCenter))

        QualityAndControlVideoView(viewModel, Modifier.align(Alignment.TopEnd), playerViewModel, videoID)
    }
}

@Composable
fun QualityAndControlVideoView(
    viewModel: PlayingVideoInfoViewModel,
    modifier: Modifier,
    playerViewModel: PlayerViewModel,
    videoID: String?
) {
    val isClosedCaption by videoCCDB.collectAsState(true)
    val coroutine = rememberCoroutineScope()

    Column(modifier.padding(top = 10.dp, end = 30.dp), Arrangement.Center, Alignment.End) {
        VideoPlayerButtonView(viewModel)

        Spacer(
            Modifier
                .width(100.dp)
                .height(1.5.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(Color.White)
        )

        Spacer(Modifier.height(14.dp))

        Row {
            when (playerViewModel.isItemLiked[videoID]) {
                LikeItemType.LOADING -> CircularLoadingViewSmall()
                LikeItemType.LIKE -> ImageWithBorder(R.drawable.ic_thumbs_up, Color.Red) {
                    playerViewModel.likeAItem(viewModel.videoInfo, false)
                }
                LikeItemType.NONE, null -> ImageWithBorder(R.drawable.ic_thumbs_up) {
                    playerViewModel.likeAItem(viewModel.videoInfo, true)
                }
            }

            ImageWithBorder(R.drawable.ic_playlist) {
                viewModel.showPlaylistDialog(true)
            }
            VideoPlayerSimilarView(viewModel)
            ImageWithBorder(R.drawable.ic_share) {
                viewModel.showShareDialog(true)
            }
        }

        Spacer(Modifier.height(14.dp))

        Row {
            ImageWithBorder(if (isClosedCaption) R.drawable.ic_open_caption else R.drawable.ic_closed_caption) {
                if (isClosedCaption) viewModel.webView?.evaluateJavascript("disableCaption()", null)
                else viewModel.webView?.evaluateJavascript("enableCaption()", null)

                coroutine.launch {
                    videoCCDB = flowOf(!isClosedCaption)
                }
            }

            VideoPlayerSpeedView(viewModel)

            if (viewModel.videoMute) ImageWithBorder(R.drawable.ic_volume_mute) {
                viewModel.webView?.evaluateJavascript("unMute();", null)
            }
            else ImageWithBorder(R.drawable.ic_speaker_full) {
                viewModel.webView?.evaluateJavascript("mute();", null)
            }
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
                formatDurationsForVideo(viewModel.videoCurrentTimestamp), size = 13
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
                formatDurationsForVideo(viewModel.videoDuration), size = 13
            )
        }

        Spacer(Modifier.width(10.dp))
        ImageWithBgAndBorder(R.drawable.ic_go_backward_10sec) {
            viewModel.webView?.evaluateJavascript("seekBack();", null)
        }
        Spacer(Modifier.height(5.dp))

        if (viewModel.playerState == YoutubePlayerState.PLAYING) {
            ImageWithBgAndBorder(R.drawable.ic_pause) {
                viewModel.webView?.evaluateJavascript("pauseVideo();", null)
            }
        } else ImageWithBgAndBorder(R.drawable.ic_play) {
            viewModel.webView?.evaluateJavascript("playVideo();", null)
        }

        Spacer(Modifier.height(5.dp))
        ImageWithBgAndBorder(R.drawable.ic_go_forward_10sec) {
            viewModel.webView?.evaluateJavascript("seekAhead();", null)
        }
    }

    LaunchedEffect(viewModel.videoCurrentTimestamp) {
        sliderPosition = viewModel.videoCurrentTimestamp
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoPlayerInfoView(viewModel: PlayingVideoInfoViewModel, modifier: Modifier) {
    val activity = LocalActivity.current
    val height = LocalConfiguration.current.screenWidthDp.dp

    val needPictureMode = stringResource(R.string.pip_mode_disabled_for_app)

    Column(
        modifier
            .width(height / (2))
            .padding(top = 10.dp, start = 20.dp)
    ) {
        GlideImage(
            viewModel.videoInfo?.thumbnail,
            viewModel.videoInfo?.name,
            Modifier.size(130.dp, 100.dp),
            contentScale = ContentScale.Crop
        )

        TextViewSemiBold(viewModel.videoInfo?.name ?: "", size = 20, line = 2)
        TextViewNormal(viewModel.videoInfo?.artists ?: "", size = 13, line = 1)

        Spacer(Modifier.height(14.dp))

        Row {
            Box(Modifier.rotate(180f)) {
                ImageWithBorder(R.drawable.ic_arrow_right) {
                    activity?.finish()
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ImageWithBorder(R.drawable.ic_picture_in_picture_on) {
                    if (!hasPIPPermission()) {
                        needPictureMode.toast()
                        openAppSettings()
                        return@ImageWithBorder
                    }
                    val params =
                        PictureInPictureParams.Builder().setAspectRatio(Rational(192, 108)).build()
                    activity?.enterPictureInPictureMode(params)
                }
            }
        }
    }
}