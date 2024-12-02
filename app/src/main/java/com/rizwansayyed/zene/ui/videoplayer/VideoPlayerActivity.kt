package com.rizwansayyed.zene.ui.videoplayer

import android.app.AppOpsManager
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.lifecycle.lifecycleScope
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.videoplayer.view.VideoPlayerControls
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {

    private var webView: WebView? = null
    private var webAppInterface: WebAppInterface? = null
    private var infoJob: Job? = null
    private var videoControl by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE or WindowManager.LayoutParams.FLAG_SECURE)

        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return
        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_VIDEO_VIEW)

        setContent {
            Box(Modifier.fillMaxWidth()) {
                AndroidView(
                    { ctx ->
                        WebView(ctx).apply {
                            webView = this
                            webAppInterface = WebAppInterface(webView)
                            enable()
                            webViewClient = webViewClientObject
                            webChromeClient = webViewChromeClientObject
                            addJavascriptInterface(webAppInterface!!, "Video")
                            webAppInterface?.loadWebView(videoID)
                        }
                    }, Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxSize()
                )

                Spacer(
                    Modifier
                        .fillMaxSize()
                        .clickable {
                            videoControl = true
                        })

                AnimatedVisibility(
                    videoControl,
                    enter = fadeIn(initialAlpha = 0.4f),
                    exit = fadeOut(animationSpec = tween(durationMillis = 250))
                ) {
                    VideoPlayerControls(Modifier.align(Alignment.Center), webAppInterface!!, {
                        enterPIP()
                    }) {
                        videoControl = false
                    }
                }

                LaunchedEffect(webAppInterface) {
                    if (webAppInterface != null) {
                        videoControl = true
                        delay(1.seconds)
                        videoControl = false
                    }
                }
            }

            LaunchedEffect(Unit) {
                ContextCompat.registerReceiver(
                    this@VideoPlayerActivity, pipActionReceiver,
                    IntentFilter("YT_PLAYER_LISTENER"), ContextCompat.RECEIVER_EXPORTED
                )?.apply {
                    setPackage(context.packageName)
                }

                val window = this@VideoPlayerActivity.window
                val decorView: View = this@VideoPlayerActivity.window.decorView

                WindowCompat.setDecorFitsSystemWindows(window, false)
                val controllerCompat = WindowInsetsControllerCompat(window, decorView)
                controllerCompat.hide(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.navigationBars())
                controllerCompat.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            BackHandler {
                if (hasPermission()) enterPIP()
                else finishAffinity()
            }
        }
    }

    private val webViewClientObject = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = true
    }

    private val webViewChromeClientObject = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            super.onConsoleMessage(consoleMessage)
            return true
        }
    }

    private fun stopJob() {
        infoJob?.cancel()
        infoJob = null
    }

    private fun startJob() {
        infoJob = lifecycleScope.launch {
            while (true) {
                webAppInterface?.getPlayStatus()
                delay(1.seconds)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startJob()
        sendWebViewCommand(PAUSE_VIDEO)
        if (!hasPermission()) resources.getString(R.string.pip_app_settings_desc).toast()
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.loadUrl("about:blank")
        webView?.onPause()
        webView?.destroy()
        webView = null
    }

    override fun onPause() {
        super.onPause()
        enterPIP()
        stopJob()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return
        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_VIDEO_VIEW)
        startJob()
        webAppInterface?.loadWebView(videoID)
    }

    private val pipActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return
        }
    }

    private fun enterPIP() = lifecycleScope.launch {
        val aspect = Rational(16, 9)

        val playIntent = PendingIntent.getBroadcast(
            this@VideoPlayerActivity, (1..999).random(),
            Intent("YT_PLAYER_LISTENER"), PendingIntent.FLAG_IMMUTABLE
        )
        val icon = Icon.createWithResource(this@VideoPlayerActivity, R.drawable.ic_empty)

        val playPauseAction = RemoteAction(icon, "", "", playIntent)

        val params = PictureInPictureParams.Builder().setAspectRatio(aspect)
            .setActions(listOf(playPauseAction)).build()

        enterPictureInPictureMode(params)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean, newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            videoControl = false
            stopJob()
        } else {
            startJob()
            sendWebViewCommand(PAUSE_VIDEO)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIP()
    }

    private fun hasPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), packageName
        ) == AppOpsManager.MODE_ALLOWED
    }
}


