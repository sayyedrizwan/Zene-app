package com.rizwansayyed.zene.service

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.db.DataStoreManager.musicAutoplaySettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicLoopSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAYBACK_RATE
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_ENDED
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_PLAYING
import com.rizwansayyed.zene.service.MusicServiceUtils.registerWebViewCommand
import com.rizwansayyed.zene.ui.lockscreen.MusicPlayerActivity
import com.rizwansayyed.zene.utils.Utils.RADIO_ARTISTS
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import dagger.hilt.android.AndroidEntryPoint
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


fun isMusicServiceRunning(context: Activity): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
        if (MusicPlayService::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}


@AndroidEntryPoint
class MusicPlayService : Service() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImpl

    private lateinit var webView: WebView
    private var job: Job? = null
    private var defaultID = "2"
    private var currentVideoID = ""

    private val phoneWake = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            CoroutineScope(Dispatchers.IO).launch {
                val music = musicPlayerDB.firstOrNull() ?: return@launch
                if (music.isPlaying == true && (music.currentDuration ?: 0) > 0) {
                    Intent(this@MusicPlayService, MusicPlayerActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        this@MusicPlayService.startActivity(this)
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            ContextCompat.registerReceiver(
                this@MusicPlayService, phoneWake, this, ContextCompat.RECEIVER_EXPORTED
            )?.apply {
                setPackage(context.packageName)
            }
        }

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

    fun loadURL(vID: String) = CoroutineScope(Dispatchers.Main).launch {
        currentVideoID = vID.replace(RADIO_ARTISTS, "").trim()
        val player = readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_music_player))
            .replace("<<VideoID>>", vID.replace(RADIO_ARTISTS, "").trim())

        webView.loadDataWithBaseURL(YOUTUBE_URL, player, "text/html", "UTF-8", null)

        withContext(Dispatchers.IO) {
            if (vID != defaultID && !vID.contains(RADIO_ARTISTS))
                zeneAPI.addMusicHistory(vID).firstOrNull()
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    private val listener = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            val json = i.getStringExtra(Intent.ACTION_MAIN) ?: return
            val int = i.getIntExtra(Intent.ACTION_MEDIA_EJECT, 0)

            if (json == PLAYBACK_RATE) updatePlaybackSpeed()
            else if (json == NEXT_SONG) forwardAndRewindSong(true)
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

    private fun pause() = CoroutineScope(Dispatchers.Main).launch {
        if (currentVideoID == defaultID) {
            loadCurrentSong()
            return@launch
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

    private fun updatePlaybackSpeed() = CoroutineScope(Dispatchers.IO).launch {
        val v = when (musicSpeedSettings.first()) {
            MusicSpeed.`025` -> 0.25
            MusicSpeed.`05` -> 0.5
            MusicSpeed.`1` -> 1
            MusicSpeed.`15` -> 1.5
            MusicSpeed.`20` -> 2.0
        }
        withContext(Dispatchers.Main) {
            webView.evaluateJavascript("playbackSpeed($v);", null)
        }
    }


    inner class JavaScriptInterface {
        @JavascriptInterface
        fun playerState(v: Int) = CoroutineScope(Dispatchers.IO).launch {
            if (currentVideoID == defaultID) return@launch
            if (v == VIDEO_ENDED) checkAndPlayNextSong()

            val d = musicPlayerDB.first()
            d?.isBuffering = v == VIDEO_BUFFERING
            d?.isPlaying = false
            if (v == VIDEO_PLAYING) {
                d?.isPlaying = true
                updatePlaybackSpeed()
            }
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
        try {
            unregisterReceiver(listener)
        } catch (e: Exception) {
            e.message
        }
        try {
            unregisterReceiver(phoneWake)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun clearCache() {
        webView.clearCache(true)
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies({})
    }

    private fun loadCurrentSong() = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player?.id ?: return@launch
        loadURL(id)
    }

    private fun checkAndPlayNextSong() = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player?.id ?: return@launch
        if (musicLoopSettings.firstOrNull() == true) {
            loadURL(id)
            return@launch
        }
        if (musicAutoplaySettings.firstOrNull() == false) {
            pause()
            return@launch
        }
        forwardAndRewindSong(true)
    }


    private fun forwardAndRewindSong(nextSong: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player?.id ?: return@launch
        try {
            val player = musicPlayerDB.firstOrNull()
            val index = player?.list?.indexOfFirst { it.id == id } ?: -1
            if (index < 0) return@launch

            val p = if (nextSong) index + 1 else index - 1
            val data = player?.list?.get(p) ?: return@launch
            data.id?.let {
                loadURL(it)
                val new = MusicPlayerData(player.list, data, VIDEO_BUFFERING, 0, false, 0, true)
                musicPlayerDB = flowOf(new)
            }
        } catch (e: Exception) {
            e.message
        }
    }
}