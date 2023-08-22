package com.rizwansayyed.zene.ui.musicplay.video

import android.app.PictureInPictureParams
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.exoPlayerGlobal
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.isExoPlayerGlobalInitialized
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import com.rizwansayyed.zene.utils.Utils.EXTRA.SONG_NAME_EXTRA
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

            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
            )
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
                        Modifier.align(Alignment.TopEnd),
                        songsViewModel
                    )
                }

//                BackHandler {
//                    if (exoPlayerGlobal?.isPlaying == true) {
//                        if (!isInPictureInPictureMode) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                enterPictureInPictureMode(
//                                    PictureInPictureParams.Builder()
//                                        .setAspectRatio(Rational(16, 9))
//                                        .build()
//                                )
//                            } else
//                                enterPictureInPictureMode()
//                        }
//                    }
//            }
            }
        }

        if (isExoPlayerGlobalInitialized()) {
            exoPlayerGlobal.playWhenReady = false
            exoPlayerGlobal.stop()
            exoPlayerGlobal.seekTo(0)
            exoPlayerGlobal.release()
        }
        songsViewModel.videoPlayingDetails(songPlayTitle)
    }


    override fun internetConnected() {
        if (isExoPlayerGlobalInitialized() && !exoPlayerGlobal.isLoading) {
            songsViewModel.videoPlayingDetails(songPlayTitle)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isExoPlayerGlobalInitialized()) {
            exoPlayerGlobal.playWhenReady = false
            exoPlayerGlobal.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isExoPlayerGlobalInitialized()) {
            exoPlayerGlobal.playWhenReady = false
            exoPlayerGlobal.stop()
            exoPlayerGlobal.seekTo(0)
            exoPlayerGlobal.release()
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}