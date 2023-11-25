package com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview

import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.musicplayer.view.playerwebview.helper.PlayerWebView

@Composable
fun MusicYoutubeWebView(modifier: Modifier) {
    var playerWebView by remember { mutableStateOf<PlayerWebView?>(null) }

    Box(modifier) {
        AndroidView(factory = {
            playerWebView = PlayerWebView(it, "UNo0TG9LwwI")
            playerWebView!!
        }, update = {
            playerWebView?.loadUrl()
        }, modifier = Modifier.fillMaxSize())
    }
}