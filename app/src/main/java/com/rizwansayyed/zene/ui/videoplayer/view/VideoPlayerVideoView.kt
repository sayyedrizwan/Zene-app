package com.rizwansayyed.zene.ui.videoplayer.view

import android.text.TextUtils
import android.util.Log
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.search.view.removeYoutubeTopView
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.URLSUtils.YT_VIDEO_BASE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
import java.io.ByteArrayInputStream
import java.net.MalformedURLException
import java.net.URL

@Composable
fun VideoPlayerVideoView(modifier: Modifier, videoID: String?) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var didRemoved by remember { mutableStateOf(false) }
    val htmlContent = getRawFolderString(R.raw.yt_video_player)

    AndroidView({ ctx ->
        WebView(ctx).apply {
            enable()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    removeYoutubeTopView(view) {
                        didRemoved = true
                    }
                }
            }
            webView?.alpha = 0f
            val c = htmlContent.replace("<<Quality>>", "1080").replace("<<VideoID>>", videoID ?: "")
            webView = this
            loadDataWithBaseURL(YT_VIDEO_BASE_URL, c, "text/html", "UTF-8", null)
        }
    }, modifier.fillMaxSize())

    if (!didRemoved) Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black),
        Alignment.Center
    ) {
        CircularLoadingView()
    }

    DisposableEffect(Unit) {
        onDispose {
            webView?.let {
                clearWebViewData(it)
                killWebViewData(it)
            }
        }
    }
}

object AdBlocker {
    private val AD_HOSTS: Set<String> = HashSet()

    fun isAd(url: String?): Boolean {
        try {
            return isAdHost(getHost(url))
        } catch (e: MalformedURLException) {
            return false
        }
    }

    private fun isAdHost(host: String): Boolean {
        if (TextUtils.isEmpty(host)) {
            return false
        }
        val index = host.indexOf(".")
        return index >= 0 && (AD_HOSTS.contains(host) || index + 1 < host.length && isAdHost(
            host.substring(
                index + 1
            )
        ))
    }

    fun createEmptyResource(): WebResourceResponse {
        return WebResourceResponse("text/plain", "utf-8", ByteArrayInputStream("".toByteArray()))
    }

    @Throws(MalformedURLException::class)
    fun getHost(url: String?): String {
        return URL(url).host
    }
}