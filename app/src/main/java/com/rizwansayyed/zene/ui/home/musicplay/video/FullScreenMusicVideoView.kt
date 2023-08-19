package com.rizwansayyed.zene.ui.home.musicplay.video

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
import androidx.media3.ui.PlayerView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.musicplay.video.VideoPlayerStatus.*
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.delay


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun FullScreenMusicVideo(nav: HomeNavViewModel, songsViewModel: SongsViewModel) {

    val activity = LocalContext.current as Activity
    val errorLoadingVideo = stringResource(id = R.string.error_loading_the_video_try_again)
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }

    AnimatedVisibility(
        visible = nav.playMusicVideo.value.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            Modifier
                .systemBarsPadding()
                .fillMaxSize()
                .background(Color.Black)
        ) {

            when (songsViewModel.videoPlayingDetails.status) {
                LOADING -> Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(Modifier.size(30.dp), Color.White)
                }

                ERROR -> {
                    nav.playingVideo("")
                    errorLoadingVideo.showToast()
                }

                SUCCESS -> {
                    AndroidView(factory = { ctx ->
                        PlayerView(ctx).apply {
                            resizeMode = RESIZE_MODE_FIXED_WIDTH
                        }
                    }, update = {
                        val url =
                            if (songsViewModel.videoPlayingDetails.data?.hdd?.isNotEmpty() == true)
                                songsViewModel.videoPlayingDetails.data?.hdd
                            else if (songsViewModel.videoPlayingDetails.data?.hd?.isNotEmpty() == true)
                                songsViewModel.videoPlayingDetails.data?.hd
                            else songsViewModel.videoPlayingDetails.data?.sd

                        val mediaItem = MediaItem.fromUri(Uri.parse(url))
                        val loadControl = DefaultLoadControl.Builder()
                            .setBufferDurationsMs(20 * 1024, 90 * 1024, 2* 1024, 2* 1024)
                            .setPrioritizeTimeOverSizeThresholds(true)



                        exoPlayer =
                            ExoPlayer.Builder(activity).setLoadControl(loadControl.build()).build()
                        it.player = exoPlayer
                        val mediaSourceFactory = DefaultMediaSourceFactory(activity)
                        val mediaSource = mediaSourceFactory.createMediaSource(mediaItem)

                        exoPlayer?.setMediaSource(mediaSource)
                        exoPlayer?.playWhenReady = true
                        exoPlayer?.prepare()
                    })

//                    TopHeaderOf(songsViewModel.videoPlayingDetails.data)
                }
            }

        }

        LaunchedEffect(Unit) {
            delay(800)
            songsViewModel.videoPlayingDetails(nav.playMusicVideo.value)
        }

        DisposableEffect(Unit) {
            nav.doPlayer(true)
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            onDispose {
                exoPlayer?.pause()
                exoPlayer?.release()
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}