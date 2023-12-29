package com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.WhiteColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview.helper.PlayerWebView

@Composable
fun MusicYoutubeWebView(modifier: Modifier, videoId: String) {
    val activity = LocalContext.current as Activity
    var playerWebView by remember { mutableStateOf<PlayerWebView?>(null) }
    val fullScreenView = remember { mutableStateOf<View?>(null) }

    Box(modifier) {
        AndroidView(
            factory = {
                playerWebView = PlayerWebView(activity, videoId, fullScreenView) {
                    if (it) activity.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    else activity.requestedOrientation =
                        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
                playerWebView!!
            }, modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        )

        if (playerWebView?.showIcons == true)
            Row(
                Modifier.align(Alignment.BottomEnd),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                SmallIcons(
                    if (playerWebView?.isVideoEnded == true) R.drawable.ic_replay else if (playerWebView?.isVideoPlaying == true) R.drawable.ic_pause else R.drawable.ic_play,
                    24, 9
                ) {
                    if (playerWebView?.isVideoEnded == true) playerWebView?.play()
                    else if (playerWebView?.isVideoPlaying == true) playerWebView?.pause()
                    else playerWebView?.play()
                }
                SmallIcons(
                    icon = if (playerWebView?.isMuted == true) R.drawable.ic_volume_off else R.drawable.ic_volume_high,
                    24, 9
                ) {
                    if (playerWebView?.isMuted == true) playerWebView?.unMute() else playerWebView?.mute()
                }

                Spacer(Modifier.weight(1f))

                SmallIcons(icon = R.drawable.ic_maximize_screen, 24, 9) {
                    playerWebView?.enterFullScreen()
                }
            }
    }

    DisposableEffect(Unit){
        onDispose {
            playerWebView?.destroy()
        }
    }

    if (fullScreenView.value != null) {
        BackHandler {
            playerWebView?.buttonBackPressed()
        }
    }
}