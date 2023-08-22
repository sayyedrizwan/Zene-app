package com.rizwansayyed.zene.ui.musicplay.video

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
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.BUFF_PLAYBACK
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.MAX_BUFF
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.MIN_BUFF
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.exoPlayerGlobal
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.generateMediaSource
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.openOnYoutubeVideo
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
                    if (playUrl.isEmpty()) playUrl = it
            }

            if (playUrl.isEmpty())
                CircularProgressIndicator(center.size(30.dp), Color.White)
            else {
                AndroidView(factory = { ctx ->
                    PlayerView(ctx).apply {
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
                    }
                }, update = {

                    val loadControl = DefaultLoadControl.Builder()
                        .setBufferDurationsMs(MIN_BUFF, MAX_BUFF, BUFF_PLAYBACK, BUFF_PLAYBACK)
                        .setPrioritizeTimeOverSizeThresholds(true)

                    exoPlayerGlobal =
                        ExoPlayer.Builder(activity).setLoadControl(loadControl.build()).build()
                    it.player = exoPlayerGlobal
                    val mediaSource = generateMediaSource(activity, playUrl)

                    exoPlayerGlobal.setMediaSource(mediaSource)
                    exoPlayerGlobal.playWhenReady = true
                    exoPlayerGlobal.prepare()
                    exoPlayerGlobal.play()
                }, modifier = center.fillMaxSize())


                Image(
                    painter = painterResource(id = R.drawable.youtube_color_logo),
                    contentDescription = "",
                    modifier = topEnd
                        .padding(15.dp)
                        .size(37.dp)
                        .clickable {
                            openOnYoutubeVideo(songsViewModel.videoPlayingDetails.data ?: "")
                        },
                    alpha = 0.4f
                )

            }
        }
    }
}