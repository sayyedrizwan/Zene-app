package com.rizwansayyed.zene.ui.videoplayer.view

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.URLSUtils.YT_VIDEO_BASE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData

@Composable
fun VideoPlayerVideoView(modifier: Modifier) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    val htmlContent = getRawFolderString(R.raw.yt_video_player)
    AndroidView({ ctx ->
        WebView(ctx).apply {
            enable()
            val c = htmlContent.replace("<<Quality>>", "1080")
                .replace("<<VideoID>>", "34Na4j8AVgA")
            webView = this
            loadDataWithBaseURL(YT_VIDEO_BASE_URL, c, "text/html", "UTF-8", null)
        }
    }, modifier.fillMaxSize())

    DisposableEffect(Unit) {
        onDispose {
            webView?.let {
                clearWebViewData(it)
                killWebViewData(it)
            }
        }
    }
}