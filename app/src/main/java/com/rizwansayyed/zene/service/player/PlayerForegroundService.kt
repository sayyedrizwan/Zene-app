package com.rizwansayyed.zene.service.player

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.autoPausePlayerSettings
import com.rizwansayyed.zene.datastore.DataStorageManager.isLoopDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isShuffleDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.DataStorageManager.smoothSongTransitionSettings
import com.rizwansayyed.zene.datastore.DataStorageManager.songSpeedDB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification
import com.rizwansayyed.zene.service.player.utils.MediaSessionPlayerNotification
import com.rizwansayyed.zene.service.player.utils.SleepTimerEnum
import com.rizwansayyed.zene.service.player.utils.SmartShuffle
import com.rizwansayyed.zene.service.player.utils.sleepTimerNotification
import com.rizwansayyed.zene.service.player.utils.sleepTimerSelected
import com.rizwansayyed.zene.ui.partycall.PartyCallActivity
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.URLSUtils.YT_VIDEO_BASE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
import com.rizwansayyed.zene.utils.share.MediaContentUtils.TEMP_ZENE_MUSIC_DATA_LIST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            } catch (_: Exception) {
                return null
            }
        }
    }

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    private var playerWebView: WebView? = null

    var isNew: Boolean = false
    var currentPlayingSong: ZeneMusicData? = null
    var songsLists: Array<ZeneMusicData?> = emptyArray()
    private var durationJob: Job? = null
    private var sleepTimer: Job? = null
    private val smartShuffle by lazy { SmartShuffle(this) }
    private val mediaSession by lazy { MediaSessionPlayerNotification(this) }
    private val exoPlayerSession by lazy { ExoPlaybackService(this) }

    var errorReRun = 0

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isNew = intent?.getBooleanExtra(Intent.ACTION_USER_PRESENT, false) ?: false

        val musicData = intent?.getStringExtra(Intent.ACTION_VIEW) ?: "{}"
        currentPlayingSong = moshi.adapter(ZeneMusicData::class.java).fromJson(musicData)
        songsLists = TEMP_ZENE_MUSIC_DATA_LIST.toTypedArray()

        playSongs(isNew)
        errorReRun = 0

        CoroutineScope(Dispatchers.IO).launch {
            currentPlayingSong?.let { zeneAPI.addHistory(it).catch { }.collectLatest { } }
            if (isActive) cancel()
        }


        return START_STICKY
    }

    fun playSongs(new: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        isNew = new

        if (!new)
            currentPlayingSong?.let { PartyCallActivity.declinePartyCallInterface?.changeUpdate(it) }


        if (currentPlayingSong?.type() == MusicDataTypes.SONGS) {
            getSimilarSongInfo()
            loadAVideo(currentPlayingSong?.id)
            CoroutineScope(Dispatchers.Main).launch {
                exoPlayerSession.stop()
            }
            registerEvents(FirebaseEventsParams.PLAYED_SONG)
        } else if (currentPlayingSong?.type() == MusicDataTypes.PODCAST_AUDIO) {
            getMediaPlayerPath()
            loadAVideo("")
            registerEvents(FirebaseEventsParams.PLAYED_PODCAST)
        } else if (currentPlayingSong?.type() == MusicDataTypes.RADIO) {
            getRadioPlayerPath()
            loadAVideo("")
            registerEvents(FirebaseEventsParams.PLAYED_RADIO)
        } else if (currentPlayingSong?.type() == MusicDataTypes.AI_MUSIC) {
            getAIMusicPlayerPath()
            getSimilarAISongInfo()
            loadAVideo("")
            registerEvents(FirebaseEventsParams.PLAYED_AI_MUSIC)
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun videoState(playerState: Int, currentTS: String, duration: String, playSpeed: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val isSmooth = smoothSongTransitionSettings.firstOrNull() ?: false

                val state = YoutubePlayerState.entries.first { it.v == playerState }
                visiblePlayerNotification(state, currentTS, duration, playSpeed)

                val playerInfo = musicPlayerDB.firstOrNull()
                playerInfo?.state = state
                playerInfo?.speed = playSpeed
                playerInfo?.currentDuration = currentTS
                playerInfo?.totalDuration = duration

                try {
                    if (isSmooth && (duration.toFloat() - currentTS.toFloat()) <= 5) songEnded()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

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
                else exoPlayerSession.playingStatus()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            musicPlayerDB.catch { }.collectLatest {
                songsLists = it?.lists?.toTypedArray() ?: emptyArray()
            }
        }
    }

    private fun loadAVideo(videoID: String?) = CoroutineScope(Dispatchers.Main).launch {
        val htmlContent = getRawFolderString(R.raw.yt_music_player)
        val speed = songSpeedDB.first().name.replace("_", ".")

        val c = htmlContent.replace("<<VideoID>>", videoID ?: "")
            .replace("setSpeed = 1.0", "setSpeed = $speed")
            .replace("isNew = false", "isNew = $isNew")

        playerWebView?.loadDataWithBaseURL(YT_VIDEO_BASE_URL, c, "text/html", "UTF-8", null)

        if (isActive) cancel()
    }

    private fun saveEmpty(list: List<ZeneMusicData?>) = CoroutineScope(Dispatchers.IO).launch {
        val finalList = ArrayList<ZeneMusicData?>().apply {
            if (!list.any { it?.id == currentPlayingSong?.id }) add(currentPlayingSong)
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

    private fun getSimilarAISongInfo() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch

        saveEmpty(songsLists.asList())
        if (songsLists.size > 1) return@launch

        try {
            val lists = zeneAPI.similarAISongs(currentPlayingSong!!.artists).firstOrNull()
            saveEmpty(lists?.toList() ?: emptyList())
        } catch (_: Exception) {
            saveEmpty(songsLists.asList())
        }
        if (isActive) cancel()
    }

    private fun getMediaPlayerPath() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch
        saveEmpty(songsLists.asList())
        try {
            val path = zeneAPI.podcastMediaURL(currentPlayingSong!!.id).firstOrNull()
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

    private fun getAIMusicPlayerPath() = CoroutineScope(Dispatchers.IO).launch {
        currentPlayingSong ?: return@launch
        saveEmpty(songsLists.asList())
        try {
            val path = zeneAPI.aiMusicMediaURL(currentPlayingSong!!.id).firstOrNull()
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

        val autoPauseSettings = autoPausePlayerSettings.firstOrNull() ?: false
        if (autoPauseSettings) {
            pause()
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
                currentPlayingSong = smartShuffle.getNextSong()
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

            playSongs(false)
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
            showNotification(state, currentTS, speed)
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

    override fun playNext(v: ZeneMusicData) {
        CoroutineScope(Dispatchers.IO).launch {
            val player = musicPlayerDB.firstOrNull()
            var array = ArrayList<ZeneMusicData?>(player?.lists ?: emptyList())
            val index = player?.lists?.indexOfFirst { it?.id == currentPlayingSong?.id }
            if (index != -1) {
                array.add(index?.plus(1) ?: 0, v)
            }

            player?.lists = array.distinctBy { it?.id }
            withContext(Dispatchers.IO) {
                musicPlayerDB = flowOf(player)
            }

            songsLists = array.toTypedArray()
        }
    }

    override fun addToQueue(v: ZeneMusicData) {
        CoroutineScope(Dispatchers.IO).launch {
            val player = musicPlayerDB.firstOrNull()
            var array = ArrayList<ZeneMusicData?>(player?.lists ?: emptyList())
            array.add(v)

            player?.lists = array.distinctBy { it?.id }.toList()
            withContext(Dispatchers.IO) {
                musicPlayerDB = flowOf(player)
            }

            songsLists = array.toTypedArray()
        }
    }

    override fun seekTo(v: Long) {
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

    override fun addListsToNext(list: List<ZeneMusicData>) {
        CoroutineScope(Dispatchers.IO).launch {
            val index = songsLists.indexOfLast { it?.id == currentPlayingSong?.id }

            val arrayList = ArrayList<ZeneMusicData?>()
            arrayList.addAll(songsLists)
            arrayList.addAll(index + 1, list)
            val l = arrayList.distinctBy { it?.id }.toList()

            val player = musicPlayerDB.firstOrNull()
            player?.lists = arrayList.toList()
            withContext(Dispatchers.IO) {
                musicPlayerDB = flowOf(player)
            }

            songsLists = l.toTypedArray()
        }
    }

    override fun addListsToQueue(list: List<ZeneMusicData>) {
        CoroutineScope(Dispatchers.IO).launch {
            val arrayList = ArrayList<ZeneMusicData?>()

            arrayList.addAll(songsLists)
            arrayList.addAll(list)
            val l = arrayList.distinctBy { it?.id }.toList()

            val player = musicPlayerDB.firstOrNull()
            player?.lists = arrayList.toList()
            withContext(Dispatchers.IO) {
                musicPlayerDB = flowOf(player)
            }

            songsLists = l.toTypedArray()
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        pause()
    }
}