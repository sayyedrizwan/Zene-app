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
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING
import com.rizwansayyed.zene.service.MusicServiceUtils.registerWebViewCommand
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class MusicPlayService : Service() {
    private lateinit var webView: WebView
    private var job: Job? = null

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
        loadURL("2")
    }

    fun loadURL(vID: String) {
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

            if (json == PLAY_VIDEO) play()
            else if (json == PAUSE_VIDEO) pause()
            else if (json.contains("{\"list\":") && json.contains("\"player\":")) {
                clearCache()

                val d = moshi.adapter(MusicPlayerData::class.java).fromJson(json)
                d?.player?.id?.let { loadURL(it) }
                musicPlayerDB = flowOf(d)
            }
        }
    }

    private fun pause() {
        webView.evaluateJavascript("pauseSong();", null)
    }

    private fun play() {
        webView.evaluateJavascript("playSong();", null)
    }

    private fun getDurations() {
        webView.evaluateJavascript("playerDurations();", null)
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

    fun clearCache() {
        webView.clearCache(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies({})
    }
}