package com.rizwansayyed.zene.ui.videoplayer.webview

import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.songQualityDB
import com.rizwansayyed.zene.data.db.DataStoreManager.videoCaptionDB
import com.rizwansayyed.zene.data.db.DataStoreManager.videoQualityDB
import com.rizwansayyed.zene.data.db.DataStoreManager.videoSpeedSettingsDB
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.utils.Utils.ytThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class WebAppInterface(private val webView: WebView?) {

    private var lastDuration = 0
    private var lastID = ""

    var isPlaying by mutableStateOf(false)
    var isBuffering by mutableStateOf(false)
    var isCaptionAvailable by mutableStateOf(false)
    var songInfo by mutableStateOf<ZeneMusicDataItems?>(null)
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

    @JavascriptInterface
    fun playerState() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1.seconds)
            if (lastDuration > 0) {
                seekTo(lastDuration)
                lastDuration = 0
            }

            caption(videoCaptionDB.first())
            updatePlaybackSpeed()
            songDetails()
        }
    }

    @JavascriptInterface
    fun captionAvailable(b: Boolean) {
        isCaptionAvailable = b
    }

    @JavascriptInterface
    fun info(title: String, author: String, videoID: String) {
        songInfo = ZeneMusicDataItems(
            title, author, videoID, ytThumbnail(videoID), "", MusicType.VIDEO.name
        )
    }

    fun caption(b: Boolean) {
        if (b)
            webView?.evaluateJavascript("enableCaption()") {}
        else
            webView?.evaluateJavascript("disableCaption()") {}
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

    fun seekTo(seek: Int) = CoroutineScope(Dispatchers.Main).launch {
        webView?.evaluateJavascript("seekTo(${seek})") {}
    }

    fun forwardVideo() = CoroutineScope(Dispatchers.Main).launch {
        val duration = currentDuration + 10
        webView?.evaluateJavascript("seekTo(${duration})") {}
    }

    fun backwardVideo() = CoroutineScope(Dispatchers.Main).launch {
        val duration = currentDuration - 10
        webView?.evaluateJavascript("seekTo(${duration})") {}
    }

    fun updatePlaybackSpeed() = CoroutineScope(Dispatchers.IO).launch {
        delay(1.seconds)
        val v = when (videoSpeedSettingsDB.first()) {
            MusicSpeed.`025` -> 0.25
            MusicSpeed.`05` -> 0.5
            MusicSpeed.`1` -> 1
            MusicSpeed.`15` -> 1.5
            MusicSpeed.`20` -> 2.0
        }
        withContext(Dispatchers.Main) {
            webView?.evaluateJavascript("playbackSpeed($v);", null)
        }
    }

    private fun songDetails() = CoroutineScope(Dispatchers.Main).launch {
        delay(1.seconds)
        webView?.evaluateJavascript("getSongDetails();", null)
    }

    fun playLastDuration() = CoroutineScope(Dispatchers.Main).launch {
        delay(1.5.seconds)
        lastDuration = currentDuration
        loadWebView(lastID)
    }

    fun loadWebView(id: String) = CoroutineScope(Dispatchers.Main).launch {
        lastID = id
        lastDuration = 0
        val player = readHTMLFromUTF8File(context.resources.openRawResource(R.raw.yt_video_player))
            .replace("<<VideoID>>", id)
            .replace("<<Quality>>", videoQualityDB.first().value)

        webView?.loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
    }
}