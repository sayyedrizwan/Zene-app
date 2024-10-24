package com.rizwansayyed.zene.ui.videoplayer.webview

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING

class WebAppInterface(private val webView: WebView?) {

    var isPlaying by mutableStateOf(false)
    var isBuffering by mutableStateOf(false)
    var currentDuration by mutableIntStateOf(0)
    var totalDuration by mutableIntStateOf(0)

    @JavascriptInterface
    fun playerState(playerState: Int, totalDuration: Int, currentDuration: Int) {
        this.totalDuration = totalDuration
        this.currentDuration = currentDuration
        if (playerState == VIDEO_BUFFERING) {
            this.isBuffering = true
        } else {
            this.isBuffering = false
            this.isPlaying = playerState == VIDEO_PLAYING
        }
    }

    fun getPlayStatus() {
        webView?.evaluateJavascript("playerStatus()") {}
    }

    fun play() {
        webView?.evaluateJavascript("playSong()") {}
    }

    fun pause() {
        webView?.evaluateJavascript("pauseSong()") {}
    }

    fun seekTo(seek: Int) {
        webView?.evaluateJavascript("seekTo(${seek})") {}
    }
}