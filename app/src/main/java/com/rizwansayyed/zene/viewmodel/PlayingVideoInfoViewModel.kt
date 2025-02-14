package com.rizwansayyed.zene.viewmodel

import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

enum class YoutubePlayerState(val v: Int) {
    UNSTARTED(-1), ENDED(0), PLAYING(1), PAUSE(2), BUFFERING(3), VIDEO_CUED(5)
}

class PlayingVideoInfoViewModel : ViewModel() {

    var webView by mutableStateOf<WebView?>(null)
    var playerState by mutableStateOf(YoutubePlayerState.UNSTARTED)
    var showControlViewJob by mutableStateOf<Job?>(null)
    var showControlView by mutableStateOf(false)
    var videoCurrentTimestamp by mutableFloatStateOf(0f)
    var videoDuration by mutableFloatStateOf(0f)
    var videoMute by mutableStateOf(false)
    var videoName by mutableStateOf("")
    var videoAuthor by mutableStateOf("")
    var videoThumbnail by mutableStateOf("")

    fun setVideoThumb(videoID: String?) {
        videoThumbnail = "https://i.ytimg.com/vi/${videoID}/hqdefault.jpg"
    }

    fun setWebViewTo(view: WebView) {
        webView = view
    }

    fun killWebView() {
        webView?.let {
            clearWebViewData(it)
            killWebViewData(it)
        }
        webView = null
    }

    fun setVideoState(state: Int, currentTS: String, duration: String, isMute: Boolean) {
        playerState = YoutubePlayerState.entries.first { it.v == state }
        videoCurrentTimestamp = currentTS.toFloatOrNull() ?: 0f
        videoDuration = duration.toFloatOrNull() ?: 0f
        videoMute = isMute
    }

    fun setVideoInfo(name: String, author: String) {
        videoName = name
        videoAuthor = author
    }

    fun showControlView(doShow: Boolean) {
        showControlViewJob?.cancel()
        showControlView = doShow

        if (doShow) showControlViewJob = viewModelScope.launch {
            delay(4.seconds)
            showControlView = false
        }
    }
}