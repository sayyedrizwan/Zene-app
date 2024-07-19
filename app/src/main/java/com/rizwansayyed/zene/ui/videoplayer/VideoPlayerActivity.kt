package com.rizwansayyed.zene.ui.videoplayer

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return
        setContent {
            AndroidView({ ctx ->
                WebView(ctx).apply {
                    webView = this
                    enable()
                    webViewClient = webViewClientObject
                    webChromeClient = WebChromeClient()


                    val player =
                        readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_video_player))
                            .replace("<<VideoID>>", videoID)

                    loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
                }
            }, Modifier.fillMaxSize())

            LaunchedEffect(Unit) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
                )
            }
        }
    }

    private val webViewClientObject = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = false
    }

    override fun onDestroy() {
        super.onDestroy()
        webView?.loadUrl("about:blank")
        webView?.onPause()
        webView?.destroy()
        webView = null
    }
}


