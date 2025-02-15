package com.rizwansayyed.zene.viewmodel

import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.videoQualityDB
import com.rizwansayyed.zene.datastore.DataStorageManager.videoSpeedDB
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.URLSUtils.YT_VIDEO_BASE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

enum class YoutubePlayerState(val v: Int) {
    UNSTARTED(-1), ENDED(0), PLAYING(1), PAUSE(2), BUFFERING(3), VIDEO_CUED(5)
}

@HiltViewModel
class PlayingVideoInfoViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) :
    ViewModel() {

    var webView by mutableStateOf<WebView?>(null)
    var playerState by mutableStateOf(YoutubePlayerState.UNSTARTED)
    private var showControlViewJob by mutableStateOf<Job?>(null)
    var showControlView by mutableStateOf(false)
    var showLoadingView by mutableStateOf(false)
    var videoCurrentTimestamp by mutableFloatStateOf(0f)
    var videoDuration by mutableFloatStateOf(0f)
    var videoMute by mutableStateOf(false)
    var videoInfo by mutableStateOf<ZeneMusicData?>(null)
    var videoID by mutableStateOf("")

    fun setVideoThumb(id: String?) {
        videoID = id ?: ""
    }

    fun setWebViewTo(view: WebView) {
        webView = view
    }

    fun showLoadingView(v: Boolean) {
        showLoadingView = v
    }

    fun killWebView() {
        webView?.let {
            clearWebViewData(it)
            killWebViewData(it)
        }
        showControlViewJob?.cancel()
        webView = null
    }

    fun loadWebView(startNew: Boolean = true) = viewModelScope.launch(Dispatchers.Main) {
        val lastDuration = if (startNew) 0 else videoCurrentTimestamp

        val htmlContent = getRawFolderString(R.raw.yt_video_player)
        delay(500)
        val speed = videoSpeedDB.first().name.replace("_", ".")
        val c = htmlContent.replace("<<Quality>>", videoQualityDB.first().name)
            .replace("<<VideoID>>", videoID)
            .replace("setDuration = 0", "setDuration = $lastDuration")
            .replace("setSpeed = 1.0", "setSpeed = $speed")

        webView?.loadDataWithBaseURL(YT_VIDEO_BASE_URL, c, "text/html", "UTF-8", null)
    }

    private fun addToHistory() = viewModelScope.launch(Dispatchers.IO) {
        videoInfo?.let { zeneAPI.addHistory(it).catch { }.collectLatest { } }
    }

    fun setVideoState(state: Int, currentTS: String, duration: String, isMute: Boolean) {
        playerState = YoutubePlayerState.entries.first { it.v == state }
        videoCurrentTimestamp = currentTS.toFloatOrNull() ?: 0f
        videoDuration = duration.toFloatOrNull() ?: 0f
        videoMute = isMute
    }

    fun setVideoInfo(name: String, author: String, videoId: String) {
        val videoThumbnail = "https://i.ytimg.com/vi/${videoId}/hqdefault.jpg"
        videoInfo =
            ZeneMusicData(author, videoID, name, "", videoThumbnail, MusicDataTypes.VIDEOS.name)
        addToHistory()
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