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
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return
        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_VIDEO_VIEW)

        setContent {
            AndroidView({ ctx ->
                WebView(ctx).apply {
                    webView = this
                    enable()
                    webViewClient = webViewClientObject
                    webChromeClient = WebChromeClient()
                    loadWebView(videoID)
                }
            }, Modifier.fillMaxSize())

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

    private fun loadWebView(id: String) {
        val player = readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_video_player))
            .replace("<<VideoID>>", id)

        webView?.loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
    }

    private val webViewClientObject = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean = true
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
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return
        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_VIDEO_VIEW)
        loadWebView(videoID)
    }

    override fun onStart() {
        super.onStart()
        if (!hasPermission()) resources.getString(R.string.pip_app_settings_desc).toast()
    }

    private val pipActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return
        }
    }

    private fun enterPIP() {
        val aspect = Rational(16, 9)

        val playIntent = PendingIntent.getBroadcast(
            this, (1..999).random(), Intent("YT_PLAYER_LISTENER"), PendingIntent.FLAG_IMMUTABLE
        )
        val icon = Icon.createWithResource(this, R.drawable.ic_empty)

        val playPauseAction = RemoteAction(icon, "", "", playIntent)

        val params = PictureInPictureParams.Builder()
            .setAspectRatio(aspect).setActions(listOf(playPauseAction)).build()

        enterPictureInPictureMode(params)
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        println()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIP()
    }

    private fun hasPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
            android.os.Process.myUid(),
            packageName
        ) == AppOpsManager.MODE_ALLOWED
    }
}


