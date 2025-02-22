package com.rizwansayyed.zene.service.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.DataStorageManager.songSpeedDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification
import com.rizwansayyed.zene.service.player.utils.MediaSessionPlayerNotification
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum
import com.rizwansayyed.zene.service.player.utils.SmartShuffle
import com.rizwansayyed.zene.service.player.utils.sleepTimerNotification
import com.rizwansayyed.zene.service.player.utils.sleepTimerSelected
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.MainUtils.toast
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
import kotlin.time.Duration.Companion.minutes

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

    var isNew: Boolean = false
    var currentPlayingSong: ZeneMusicData? = null
    private var songsLists: Array<ZeneMusicData?> = emptyArray()
    private var durationJob: Job? = null
    private var sleepTimer: Job? = null
    private var smartShuffle: SmartShuffle? = null
    private val mediaSession by lazy { MediaSessionPlayerNotification(this) }
    private val exoPlayerSession by lazy { ExoPlaybackService(this) }
    var errorReRun = 0

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
        errorReRun += 0

        return START_STICKY
    }

    fun playSongs(new: Boolean) {
        isNew = new

        if (currentPlayingSong?.type() == MusicDataTypes.SONGS) {
            getSimilarSongInfo()
            loadAVideo(currentPlayingSong?.id)
            exoPlayerSession.stop()
        } else if (currentPlayingSong?.type() == MusicDataTypes.PODCAST_AUDIO) {
            getMediaPlayerPath()
            loadAVideo("")
        } else if (currentPlayingSong?.type() == MusicDataTypes.RADIO) {
            getRadioPlayerPath()
            loadAVideo("")
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun videoState(playerState: Int, currentTS: String, duration: String, playSpeed: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val state = YoutubePlayerState.entries.first { it.v == playerState }
                visiblePlayerNotification(state, currentTS, duration, playSpeed)

                val playerInfo = musicPlayerDB.firstOrNull()
                playerInfo?.state = state
                playerInfo?.speed = playSpeed
                playerInfo?.currentDuration = currentTS
                playerInfo?.totalDuration = duration
                musicPlayerDB = flowOf(playerInfo)
                if (isActive) cancel()
            }
        }

        @JavascriptInterface
        fun videoEnded() {
            songEnded()
        }

        @JavascriptInterface
        fun onError() {
            if (currentPlayingSong?.type() == MusicDataTypes.SONGS) {
                errorReRun += 1
                if (errorReRun <= 3) playSongs(false)
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
                if (currentPlayingSong?.type() == MusicDataTypes.SONGS)
                    playerWebView?.evaluateJavascript("playingStatus();", null)
                else
                    exoPlayerSession.playingStatus()
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

    private fun saveEmpty(list: List<ZeneMusicData?>) = CoroutineScope(Dispatchers.IO).launch {
        val doContain = list.any { it?.id == currentPlayingSong!!.id }
        val finalList = if (doContain) list else ArrayList<ZeneMusicData?>().apply {
            add(currentPlayingSong)
            addAll(list)
        }
        val d = MusicPlayerData(
            finalList, currentPlayingSong, YoutubePlayerState.BUFFERING, "0", "1.0", "0"
        )
        musicPlayerDB = flowOf(d)
        if (isActive) cancel()
    }

    private fun getSimilarSongInfo() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch

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

    private fun getMediaPlayerPath() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch
        saveEmpty(songsLists.asList())
        try {
            val path = zeneAPI.podcastMediaURL(currentPlayingSong!!.path).firstOrNull()
            exoPlayerSession.startPlaying(path?.urlPath?.trim())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isActive) cancel()
    }

    private fun getRadioPlayerPath() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch
        saveEmpty(songsLists.asList())
        try {
            val path = zeneAPI.radioMediaURL(currentPlayingSong!!.id).firstOrNull()
            exoPlayerSession.startPlaying(path?.urlPath?.trim())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (isActive) cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearWebView()
        exoPlayerSession.destroy()
        durationJob?.cancel()
    }

    private fun clearWebView() {
        playerWebView?.let {
            clearWebViewData(it)
            killWebViewData(it)
        }
        playerWebView = null
    }

    fun songEnded() = CoroutineScope(Dispatchers.IO).launch {
        if (sleepTimerSelected == SleepTimerEnum.END_OF_TRACK) {
            sleepTimerSelected = SleepTimerEnum.TURN_OFF
            sleepTimerNotification()
            return@launch
        }
        val isLoop = isLoopDB.firstOrNull() ?: false
        if (isLoop) {
            playSongs(false)
            return@launch
        }

        toNextSong()
        if (isActive) cancel()
    }

    override fun sleepTimer(minutes: SleepTimerEnum) {
        sleepTimer?.cancel()
        sleepTimerSelected = minutes
        if (minutes == SleepTimerEnum.END_OF_TRACK) return
        if (minutes == SleepTimerEnum.TURN_OFF) return
        sleepTimer = CoroutineScope(Dispatchers.IO).launch {
            delay(minutes.time.minutes)
            pause()
            sleepTimerNotification()
            sleepTimerSelected = SleepTimerEnum.TURN_OFF
            if (isActive) cancel()
        }
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

    fun visiblePlayerNotification(
        state: YoutubePlayerState, currentTS: String, duration: String, speed: String
    ) = CoroutineScope(Dispatchers.IO).launch {
        mediaSession.apply {
            updateMetadata(currentPlayingSong, duration)
            showNotification(state == YoutubePlayerState.PLAYING, currentTS, speed)
        }
    }

    override fun pause() {
        CoroutineScope(Dispatchers.Main).launch {
            exoPlayerSession.pause()
            playerWebView?.evaluateJavascript("pauseVideo();", null)
            if (isActive) cancel()
        }
    }

    override fun play() {
        CoroutineScope(Dispatchers.Main).launch {
            exoPlayerSession.play()
            playerWebView?.evaluateJavascript("playVideo();", null)
            if (isActive) cancel()
        }
    }

    override fun seekTo(v: Float) {
        CoroutineScope(Dispatchers.Main).launch {
            exoPlayerSession.seekTo(v)
            playerWebView?.evaluateJavascript("seekTo(${v});", null)
            if (isActive) cancel()
        }
    }

    override fun playbackRate(v: String) {
        CoroutineScope(Dispatchers.Main).launch {
            exoPlayerSession.playRate(v)
            playerWebView?.evaluateJavascript("playbackRate(${v});", null)
            if (isActive) cancel()
        }
    }
}