package com.rizwansayyed.zene.ui.home.musicplay.video

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.rizwansayyed.zene.BaseApplication.Companion.exoPlayerGlobal
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import com.rizwansayyed.zene.utils.Utils.EXTRA.SONG_NAME_EXTRA
import com.rizwansayyed.zene.utils.Utils.openOnYoutubeVideo
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloader.WebViewShareFrom
import dagger.hilt.android.AndroidEntryPoint

@androidx.media3.common.util.UnstableApi
@AndroidEntryPoint
class FullVideoPlayerActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val songsViewModel: SongsViewModel by viewModels()

    private var songPlayTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        songPlayTitle = intent.getStringExtra(SONG_NAME_EXTRA) ?: ""

        if (songPlayTitle.isEmpty()) {
            resources.getString(R.string.error_loading_the_video_try_again)
            finish()
            return
        }

        networkCallbackStatus = this

        setContent {
            window.setFlags(windowManagerNoLimit, windowManagerNoLimit)

//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
//            )
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

            ZeneTheme {

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                    FullScreenVideoPlayer(
                        Modifier.align(Alignment.Center),
                        Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }

        if (exoPlayerGlobal != null) {
            exoPlayerGlobal!!.playWhenReady = false
            exoPlayerGlobal!!.stop()
            exoPlayerGlobal!!.seekTo(0)
            exoPlayerGlobal!!.release()
        }

        songsViewModel.videoPlayingDetails(songPlayTitle)
    }

    @Composable
    fun FullScreenVideoPlayer(center: Modifier, topEnd: Modifier) {
        val errorLoadingVideo =
            stringResource(id = R.string.error_loading_the_video_try_again)

        when (songsViewModel.videoPlayingDetails.status) {
            VideoPlayerStatus.LOADING -> CircularProgressIndicator(center.size(30.dp), Color.White)
            VideoPlayerStatus.ERROR -> {
                finish()
                errorLoadingVideo.showToast()
            }

            VideoPlayerStatus.SUCCESS -> {
                var playUrl by remember { mutableStateOf("") }

                WebViewShareFrom(this, songsViewModel.videoPlayingDetails.data ?: "") {
                    if (it == "non") {
                        finish()
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

                        exoPlayerGlobal =
                            ExoPlayer.Builder(this@FullVideoPlayerActivity)
                                .setLoadControl(loadControl.build()).build()
                        it.player = exoPlayerGlobal
                        val mediaSourceFactory =
                            DefaultMediaSourceFactory(this@FullVideoPlayerActivity)
                        val mediaSource =
                            mediaSourceFactory.createMediaSource(mediaItem)

                        exoPlayerGlobal?.setMediaSource(mediaSource)
                        exoPlayerGlobal?.playWhenReady = true
                        exoPlayerGlobal?.prepare()
                        exoPlayerGlobal?.play()
                    }, modifier = center.fillMaxSize())


                    Image(
                        painter = painterResource(id = R.drawable.youtube_color_logo),
                        contentDescription = "",
                        modifier = topEnd
                            .padding(15.dp)
                            .size(37.dp)
                            .clickable {
                                openOnYoutubeVideo()
                            },
                        alpha = 0.4f
                    )

                }
            }
        }
    }

    override fun internetConnected() {
        if (exoPlayerGlobal?.isLoading == false) {
            songsViewModel.videoPlayingDetails(songPlayTitle)
        }
    }

    override fun onPause() {
        super.onPause()
        if (exoPlayerGlobal != null) {
            exoPlayerGlobal!!.playWhenReady = false
            exoPlayerGlobal!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerGlobal != null) {
            exoPlayerGlobal!!.playWhenReady = false
            exoPlayerGlobal!!.stop()
            exoPlayerGlobal!!.seekTo(0)
            exoPlayerGlobal!!.release()
        }
//        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}