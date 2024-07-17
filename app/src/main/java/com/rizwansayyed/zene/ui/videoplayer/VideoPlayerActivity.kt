package com.rizwansayyed.zene.ui.videoplayer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


@AndroidEntryPoint
class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val videoID = intent.getStringExtra(Intent.ACTION_MAIN) ?: return

        setContent {
            val context = LocalContext.current
            val parentViewGroup = remember { LinearLayout(context) }
            val contentViewGroup = remember { LinearLayout(context) }


            AndroidView({ ctx ->
                WebView(ctx).apply {
                    enable()
                    webViewClient = WebViewClient()
                    webChromeClient = CustomWebView(this, this)
                    setInitialScale(1)

                    val player =
                        readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_video_player))
                            .replace("<VideoID>", videoID)

                    loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
                }
            }, Modifier.fillMaxSize())
        }
    }

    private fun readHTMLFromUTF8File(inputStream: InputStream): String {
        inputStream.use {
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
                return bufferedReader.readLines().joinToString("\n")
            } catch (e: Exception) {
                return ""
            }
        }
    }

    private class CustomWebView(var parent: ViewGroup, var content: ViewGroup) :
        WebChromeClient() {
        var layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        var customView: View? = null

        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            super.onShowCustomView(view, callback)

            customView = view
            view.layoutParams = layoutParams
            parent.addView(view)
            content.visibility = View.GONE
        }

        override fun onHideCustomView() {
            super.onHideCustomView()

            content.visibility = View.VISIBLE
            parent.removeView(customView)
            customView = null
        }
    }
}


