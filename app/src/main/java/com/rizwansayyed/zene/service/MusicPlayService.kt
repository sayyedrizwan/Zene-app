package com.rizwansayyed.zene.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING
import com.rizwansayyed.zene.service.MusicServiceUtils.registerWebViewCommand
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds


class MusicPlayService : Service() {
    private lateinit var webView: WebView
    private var job: Job? = null
    private var defaultID = "2"
    private var currentVideoID = ""

    override fun onCreate() {
        super.onCreate()

        webView = WebViewService(applicationContext).apply {
            enable()
            addJavascriptInterface(JavaScriptInterface(), "ZeneListener")
            webViewClient = webViewClientObject
            webChromeClient = webViewChromeClientObject
        }

        registerWebViewCommand(applicationContext, listener)

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(4.seconds)
            while (true) {
                getDurations()
                delay(1.seconds)
            }
        }
        loadURL(defaultID)
    }

    fun loadURL(vID: String) {
        currentVideoID = vID
        val player = readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_music_player))
            .replace("<<VideoID>>", vID)

        webView.loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private val listener = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            val json = i.getStringExtra(Intent.ACTION_MAIN) ?: return
            val int = i.getIntExtra(Intent.ACTION_MEDIA_EJECT, 0)

            if (json == NEXT_SONG) forwardAndRewindSong(true)
            else if (json == PREVIOUS_SONG) forwardAndRewindSong(false)
            else if (json == SEEK_DURATION_VIDEO) seekTo(int)
            else if (json == PLAY_VIDEO) play()
            else if (json == PAUSE_VIDEO) pause()
            else if (json.contains("{\"list\":") && json.contains("\"player\":")) {
                clearCache()

                val d = moshi.adapter(MusicPlayerData::class.java).fromJson(json)
                d?.player?.id?.let { loadURL(it) }
                currentVideoID = d?.player?.id ?: ""
                musicPlayerDB = flowOf(d)
            }
        }
    }

    private fun pause() {
        if (currentVideoID == defaultID) {
            loadCurrentSong()
            return
        }
        webView.evaluateJavascript("pauseSong();", null)
    }

    private fun play() {
        if (currentVideoID == defaultID) {
            loadCurrentSong()
            return
        }
        webView.evaluateJavascript("playSong();", null)
    }

    private fun getDurations() {
        webView.evaluateJavascript("playerDurations();", null)
    }

    private fun seekTo(v: Int) {
        webView.evaluateJavascript("seekTo($v);", null)
    }


    inner class JavaScriptInterface {
        @JavascriptInterface
        fun playerState(v: Int) = CoroutineScope(Dispatchers.IO).launch {
            val d = musicPlayerDB.first()
            d?.isBuffering = v == VIDEO_BUFFERING
            d?.isPlaying = false
            if (v == VIDEO_PLAYING) d?.isPlaying = true

            musicPlayerDB = flowOf(d)

            if (isActive) cancel()
        }

        @JavascriptInterface
        fun playerDuration(current: Int, total: Int) = CoroutineScope(Dispatchers.IO).launch {
            val d = musicPlayerDB.first()
            d?.totalDuration = total
            d?.currentDuration = current
            musicPlayerDB = flowOf(d)
            if (isActive) cancel()
        }
    }


    private val webViewChromeClientObject = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            //undefined (reading 'target')
//            Log.d("TAG", "playerTotalDuration: runned 111 ${consoleMessage?.message()}")
            return true
        }
    }


    private val webViewClientObject = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = true
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
        job = null
    }

    private fun clearCache() {
        webView.clearCache(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies({})
    }

    private fun loadCurrentSong() = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player?.id ?: return@launch
        withContext(Dispatchers.Main) {
            loadURL(id)
        }
    }

    private fun forwardAndRewindSong(nextSong: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player?.id ?: return@launch
        try {
            val player = musicPlayerDB.firstOrNull()
            val index = player?.list?.indexOfFirst { it.id == id } ?: -1
            if (index < 0) return@launch

            val p = if(nextSong) index + 1 else index - 1
            val data = player?.list?.get(p) ?: return@launch
            data.id?.let {
                withContext(Dispatchers.Main) {
                    loadURL(it)
                }
                val new =
                    MusicPlayerData(player.list, data, VIDEO_BUFFERING, 0, false, 0, true)
                musicPlayerDB = flowOf(new)
            }
        } catch (e: Exception) {
            e.message
        }

    }
}