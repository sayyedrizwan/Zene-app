package com.rizwansayyed.zene.ui.home.musicplay.video

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloader.WebViewShareFrom


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun FullScreenVideoPlayer(center: Modifier, topEnd: Modifier, songsViewModel: SongsViewModel) {

    val activity = LocalContext.current as Activity

    val errorLoadingVideo =
        stringResource(id = R.string.error_loading_the_video_try_again)

    when (songsViewModel.videoPlayingDetails.status) {
        VideoPlayerStatus.LOADING -> CircularProgressIndicator(center.size(30.dp), Color.White)
        VideoPlayerStatus.ERROR -> {
            activity.finish()
            errorLoadingVideo.showToast()
        }

        VideoPlayerStatus.SUCCESS -> {
            var playUrl by remember { mutableStateOf("") }

            WebViewShareFrom(activity, songsViewModel.videoPlayingDetails.data ?: "") {
                if (it == "non") {
                    activity.finish()
                    errorLoadingVideo.showToast()
                } else
                    playUrl = it
            }

            if (playUrl.isEmpty())
                CircularProgressIndicator(center.size(30.dp), Color.White)
            else {
                AndroidView(factory = { ctx ->
                    PlayerView(ctx).apply {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    }
                }, update = {
                    val mediaItem = MediaItem.fromUri(Uri.parse(playUrl))
                    val loadControl = DefaultLoadControl.Builder()
                        .setBufferDurationsMs(
                            8 * 1024, 16 * 1024, 2 * 1024, 2 * 1024
                        )
                        .setPrioritizeTimeOverSizeThresholds(true)

                    BaseApplication.exoPlayerGlobal =
                        ExoPlayer.Builder(activity).setLoadControl(loadControl.build()).build()
                    it.player = BaseApplication.exoPlayerGlobal
                    val mediaSourceFactory = DefaultMediaSourceFactory(activity)
                    val mediaSource = mediaSourceFactory.createMediaSource(mediaItem)

                    BaseApplication.exoPlayerGlobal?.setMediaSource(mediaSource)
                    BaseApplication.exoPlayerGlobal?.playWhenReady = true
                    BaseApplication.exoPlayerGlobal?.prepare()
                    BaseApplication.exoPlayerGlobal?.play()
                }, modifier = center.fillMaxSize())


                Image(
                    painter = painterResource(id = R.drawable.youtube_color_logo),
                    contentDescription = "",
                    modifier = topEnd
                        .padding(15.dp)
                        .size(37.dp)
                        .clickable {
                            Utils.openOnYoutubeVideo(songsViewModel.videoPlayingDetails.data ?: "")
                        },
                    alpha = 0.4f
                )

            }
        }
    }
}