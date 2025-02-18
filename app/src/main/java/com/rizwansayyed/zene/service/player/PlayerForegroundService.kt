package com.rizwansayyed.zene.service.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.DataStorageManager.songSpeedDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification
import com.rizwansayyed.zene.service.player.utils.SmartShuffle
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.URLSUtils.X_VIDEO_BASE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
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
import javax.inject.Inject

@AndroidEntryPoint
class PlayerForegroundService : Service(), PlayerServiceInterface {

    companion object {
        lateinit var playerService: PlayerServiceInterface

        fun getPlayerS(): PlayerServiceInterface? {
            try {
                if (Companion::playerService.isInitialized) return playerService
                return null
            } catch (e: Exception) {
                return null
            }
        }
    }

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    private var playerWebView: WebView? = null

    private var isNew: Boolean = false
    private var currentPlayingSong: ZeneMusicData? = null
    private var songsLists: Array<ZeneMusicData?> = emptyArray()
    private var durationJob: Job? = null
    private var smartShuffle: SmartShuffle? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isNew = intent?.getBooleanExtra(Intent.ACTION_USER_PRESENT, false) ?: false

        val musicData = intent?.getStringExtra(Intent.ACTION_VIEW) ?: "{}"
        val musicList = intent?.getStringExtra(Intent.ACTION_RUN) ?: "[]"
        currentPlayingSong = moshi.adapter(ZeneMusicData::class.java).fromJson(musicData)
        songsLists =
            moshi.adapter(Array<ZeneMusicData?>::class.java).fromJson(musicList) ?: emptyArray()
        smartShuffle = SmartShuffle(songsLists.toList())
        playSongs(isNew)
        return START_STICKY
    }

    private fun playSongs(new: Boolean) {
        isNew = new
        getSimilarSongInfo()
        loadAVideo(currentPlayingSong?.id)
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun videoState(playerState: Int, currentTS: String, duration: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val playerInfo = musicPlayerDB.firstOrNull()
                playerInfo?.state = YoutubePlayerState.entries.first { it.v == playerState }
                playerInfo?.currentDuration = currentTS
                playerInfo?.totalDuration = duration
                musicPlayerDB = flowOf(playerInfo)
                if (isActive) cancel()
            }
        }

        @JavascriptInterface
        fun videoEnded() {
            CoroutineScope(Dispatchers.IO).launch {
                val isLoop = isLoopDB.firstOrNull() ?: false
                if (isLoop) {
                    playSongs(false)
                    return@launch
                }

                toNextSong()

                if (isActive) cancel()
            }
        }
    }


    override fun onCreate() {
        super.onCreate()
        playerService = this
        EmptyServiceNotification.generate(this)

        WebView(this).apply {
            enable()
            addJavascriptInterface(WebAppInterface(), "Zene")
            playerWebView = this
            loadAVideo(currentPlayingSong?.id)
        }

        durationJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(500)
                playerWebView?.evaluateJavascript("playingStatus();", null)
            }
        }
    }

    private fun loadAVideo(videoID: String?) = CoroutineScope(Dispatchers.Main).launch {
        val htmlContent = getRawFolderString(R.raw.yt_music_player)
        val speed = songSpeedDB.first().name.replace("_", ".")

        val c = htmlContent.replace("<<VideoID>>", videoID ?: "")
            .replace("setSpeed = 1.0", "setSpeed = $speed")
            .replace("isNew = false", "isNew = $isNew")

        playerWebView?.loadDataWithBaseURL(X_VIDEO_BASE_URL, c, "text/html", "UTF-8", null)

        if (isActive) cancel()
    }

    private fun getSimilarSongInfo() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch

        fun saveEmpty(list: List<ZeneMusicData?>) = CoroutineScope(Dispatchers.IO).launch {
            val doContain = list.any { it?.id == currentPlayingSong!!.id }
            val finalList = if (doContain) list else ArrayList<ZeneMusicData?>().apply {
                add(currentPlayingSong)
                addAll(list)
            }
            val d = MusicPlayerData(
                finalList, currentPlayingSong, YoutubePlayerState.BUFFERING, "0", "0"
            )
            musicPlayerDB = flowOf(d)
            if (isActive) cancel()
        }

        saveEmpty(songsLists.asList())
        if (songsLists.size > 1) return@launch

        try {
            val lists = zeneAPI.similarPlaylistsSongs(currentPlayingSong!!.id).firstOrNull()
            saveEmpty(lists?.toList() ?: emptyList())
        } catch (e: Exception) {
            saveEmpty(songsLists.asList())
        }
        if (isActive) cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerWebView?.let {
            clearWebViewData(it)
            killWebViewData(it)
        }
        durationJob?.cancel()
        playerWebView = null
    }

    override fun toNextSong() {
        CoroutineScope(Dispatchers.IO).launch {
            val isShuffle = isShuffleDB.firstOrNull() ?: false
            if (isShuffle) {
                currentPlayingSong = smartShuffle?.getNextSong()
                playSongs(false)
                return@launch
            }

            val index = songsLists.indexOfLast { it?.id == currentPlayingSong?.id }
            val info =
                if (index != -1 && index < songsLists.size - 1) songsLists[index + 1] else null
            if (info != null) {
                currentPlayingSong = info
                playSongs(false)
            }
        }
    }

    override fun toBackSong() {
        CoroutineScope(Dispatchers.IO).launch {
            val index = songsLists.indexOfLast { it?.id == currentPlayingSong?.id }
            val info = if (index > 0) songsLists[index - 1] else null
            if (info != null) {
                currentPlayingSong = info
                playSongs(false)
            }
        }
    }

    override fun pause() {
        playerWebView?.evaluateJavascript("pauseVideo();", null)
    }

    override fun play() {
        playerWebView?.evaluateJavascript("playVideo();", null)
    }

    override fun seekTo(v: Float) {
        playerWebView?.evaluateJavascript("seekTo(${v});", null)
    }

}