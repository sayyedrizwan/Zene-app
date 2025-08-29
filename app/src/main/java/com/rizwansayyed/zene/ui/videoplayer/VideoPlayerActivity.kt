package com.rizwansayyed.zene.ui.videoplayer

import android.app.PictureInPictureUiState
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.videoplayer.view.VideoPlayerVideoView
import com.rizwansayyed.zene.ui.view.LockScreenOrientation
import com.rizwansayyed.zene.utils.ads.InterstitialAdsUtils
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.PlayingVideoInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {

    val viewModel: PlayingVideoInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
        )
        setContent {
            val videoID = intent.getStringExtra(Intent.ACTION_VIEW)
            ZeneTheme {
                LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    VideoPlayerVideoView(Modifier.align(Alignment.Center), videoID, viewModel)
                }

                LaunchedEffect(Unit) {
                    InterstitialAdsUtils(this@VideoPlayerActivity, true)
                }
            }
        }
    }

    override fun onPictureInPictureUiStateChanged(pipState: PictureInPictureUiState) {
        super.onPictureInPictureUiStateChanged(pipState)
        viewModel.showControlView(false)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.showControlView(isInPictureInPictureMode)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        lifecycleScope.safeLaunch {
            val videoID = intent.getStringExtra(Intent.ACTION_VIEW)
            viewModel.showLoadingView(false)
            delay(500)
            viewModel.showControlView(false)
            viewModel.setVideoThumb(videoID)
            viewModel.loadWebView(true)
        }
        InterstitialAdsUtils(this@VideoPlayerActivity, true)
    }
}