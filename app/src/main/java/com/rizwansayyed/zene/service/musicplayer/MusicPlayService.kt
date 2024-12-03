package com.rizwansayyed.zene.service.musicplayer

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.IBinder
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.musicLoopSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.playingSongOnLockScreen
import com.rizwansayyed.zene.data.db.DataStoreManager.songQualityDB
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpMusicDataDB
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.data.roomdb.updates.implementation.UpdatesRoomDBImpl
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.NEXT_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.OPEN_PLAYER
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAYBACK_RATE
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PLAY_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PREVIOUS_SONG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_5S_BACK_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_5S_FORWARD_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SLEEP_PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SLEEP_TIMER_BG
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.VIDEO_BUFFERING
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.WAKE_ALARM
import com.rizwansayyed.zene.service.MusicServiceUtils.registerWebViewCommand
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.service.bluetoothlistener.BluetoothListeners
import com.rizwansayyed.zene.service.musicplayer.notifications.MusicPlayerNotifications
import com.rizwansayyed.zene.ui.lockscreen.MusicPlayerActivity
import com.rizwansayyed.zene.ui.player.view.sleepTime
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.RADIO_ARTISTS
import com.rizwansayyed.zene.utils.Utils.RoomDB.fileCachedSongsCacheDir
import com.rizwansayyed.zene.utils.Utils.RoomDB.fileCachedSongsDir
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.rizwansayyed.zene.utils.Utils.getTimeAfterMinutesInMillis
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.readHTMLFromUTF8File
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.utils.Utils.wvClearCache
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
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

    @Inject
    lateinit var updatesRoomDB: UpdatesRoomDBImpl

    private lateinit var webView: WebView
    private var exoPlayer: ExoPlayer? = null
    private var job: Job? = null
    private var playerInfo: ZeneMusicDataItems? = null
    private var isNewPlay = true
    private var sleepTimer: Job? = null
    private val bluetoothListeners by lazy { BluetoothListeners(updatesRoomDB) }


    private val phoneWake = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            CoroutineScope(Dispatchers.IO).launch {
                if (playingSongOnLockScreen.firstOrNull() == true) return@launch

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

    private val becomingNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            if (i?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                CoroutineScope(Dispatchers.IO).launch {
                    val d = musicPlayerDB.first()
                    if (d?.isPlaying == true) pause()
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2.seconds)
            val player = musicPlayerDB.firstOrNull()?.player

            if (player?.id != null) MusicPlayerNotifications(
                this@MusicPlayService, false, player, 0, 0
            ).generate()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        isNewPlay = true

        MusicPlayerNotifications(this@MusicPlayService).generateEmpty()

        IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            ContextCompat.registerReceiver(
                this@MusicPlayService, phoneWake, this, ContextCompat.RECEIVER_EXPORTED
            )?.apply {
                setPackage(context.packageName)
            }
        }

        IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY).apply {
            ContextCompat.registerReceiver(
                this@MusicPlayService, becomingNoisyReceiver, this, ContextCompat.RECEIVER_EXPORTED
            )?.apply {
                setPackage(context.packageName)
            }
        }

        val playerInterface = MusicPlayerInterface(this, {
            checkAndPlayNextSong()
        }, {
            if (it) updatePlaybackSpeed()
            else {
                if (isNewPlay) {
                    isNewPlay = false
                    pause()
                    CoroutineScope(Dispatchers.IO).launch {
                        val d = musicPlayerDB.first()
                        d?.currentDuration?.let { seekTo(it) }
                    }
                }
            }
        }, {
            if (isNewPlay) pause()
            else play()
        }, {
            playAVideo(it)
        })

        webView = WebViewService(applicationContext).apply {
            enable()
            addJavascriptInterface(playerInterface, "ZeneListener")
            webViewClient = webViewClientObject
            webChromeClient = webViewChromeClientObject
        }

        registerWebViewCommand(applicationContext, listener)

        durationUpdateJob()

        CoroutineScope(Dispatchers.IO).launch {
            musicPlayerDB.firstOrNull()?.player?.let {
                loadURL(it)
            }
        }

        bluetoothListeners.start(this)
    }

    fun loadURL(player: ZeneMusicDataItems) = CoroutineScope(Dispatchers.Main).launch {
        playerInfo = player
        destroyExoPlayer()
        if (player.type() == MusicType.OFFLINE_SONGS) {
            playCachedSong(player)
            player.id?.let { zeneAPI.addMusicHistory(it, player.artists).catch { }.collect() }
            return@launch
        }
        val vID = player.id ?: ""
        if (player.type() == MusicType.RADIO) {
            playARadio(player.extra ?: "")
            logEvents(FirebaseLogEvents.FirebaseEvents.STARTED_PLAYING_RADIO)
        } else {
            playAVideo(vID.replace(RADIO_ARTISTS, "").trim())
            logEvents(FirebaseLogEvents.FirebaseEvents.STARTED_PLAYING_SONG)

            withContext(Dispatchers.IO) {
                if (!vID.contains(RADIO_ARTISTS)) zeneAPI.addMusicHistory(vID, player.artists)
                    .catch { }.collect()
            }
        }
        if (isActive) cancel()
    }

    private fun playCachedSong(info: ZeneMusicDataItems) = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) { webView.wvClearCache() }
        fileCachedSongsCacheDir.deleteRecursively()
        File(fileCachedSongsDir, "${info.id}.cachxc").copyTo(fileCachedSongsCacheDir)
        exoPlayer = ExoPlayer.Builder(this@MusicPlayService).build()
        val mediaItem = MediaItem.fromUri(fileCachedSongsCacheDir.toUri())
        withContext(Dispatchers.Main) {
            exoPlayer!!.setMediaItem(mediaItem)
            exoPlayer!!.prepare()
            exoPlayer!!.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    CoroutineScope(Dispatchers.IO).launch {
                        val d = musicPlayerDB.first()
                        d?.isBuffering = false
                        d?.isPlaying = isPlaying
                        musicPlayerDB = flowOf(d)
                        updatePlaybackSpeed()
                    }
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == STATE_ENDED) sendWebViewCommand(NEXT_SONG)
                }
            })
            exoPlayer!!.play()
            delay(1.seconds)
            if (isNewPlay) exoPlayer!!.pause()
            isNewPlay = false
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    fun durationUpdateJob() {
        job?.cancel()
        job = null

        job = CoroutineScope(Dispatchers.Main).launch {
            delay(4.seconds)
            while (true) {
                getDurations()
                delay(1.seconds)
            }
        }
    }

    private val listener = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            val json = i.getStringExtra(Intent.ACTION_MAIN) ?: return
            val int = i.getIntExtra(Intent.ACTION_MEDIA_EJECT, 0)

            if (json == PLAYBACK_RATE) {
                logEvents(FirebaseLogEvents.FirebaseEvents.UPDATED_PLAYBACK_SPEED)
                updatePlaybackSpeed()
            } else if (json == NEXT_SONG) {
                logEvents(FirebaseLogEvents.FirebaseEvents.TO_NEXT_SONG)
                forwardAndRewindSong(true)
            } else if (json == PREVIOUS_SONG) {
                logEvents(FirebaseLogEvents.FirebaseEvents.TO_PREVIOUS_SONG)
                forwardAndRewindSong(false)
            } else if (json == SEEK_DURATION_VIDEO) seekTo(int)
            else if (json == SLEEP_TIMER_BG) runSleepTimer(int)
            else if (json == SEEK_5S_BACK_VIDEO) seek5sMinus()
            else if (json == SEEK_5S_FORWARD_VIDEO) seek5sPlus()
            else if (json == PLAY_VIDEO) {
                logEvents(FirebaseLogEvents.FirebaseEvents.TAP_PLAYING)
                play()
            } else if (json == OPEN_PLAYER) durationUpdateJob()
            else if (json == SLEEP_PAUSE_VIDEO) pauseSongOnSleep()
            else if (json == WAKE_ALARM) setWakeAlarm()
            else if (json == PAUSE_VIDEO) {
                logEvents(FirebaseLogEvents.FirebaseEvents.TAP_PAUSE)
                pause()
            } else if (json.contains("{\"list\":") && json.contains("\"player\":")) {
                webView.wvClearCache()
                isNewPlay = false
                val d = moshi.adapter(MusicPlayerData::class.java).fromJson(json)
                d?.player?.let { loadURL(it) }
                musicPlayerDB = flowOf(d)
                suggestPlaylistsSongs(d)
            }
        }
    }

    fun suggestPlaylistsSongs(player: MusicPlayerData?) = CoroutineScope(Dispatchers.IO).launch {
        player ?: return@launch
        if ((player.list?.size ?: 0) != 1) return@launch

        var lists: ZeneMusicDataResponse = try {
            zeneAPI.similarSongsToPlay(player.player?.id ?: "").firstOrNull() ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        if (lists.size < 2) {
            lists = try {
                zeneAPI.suggestedSongs(player.player?.id ?: "").firstOrNull() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }


        if (lists.isEmpty()) return@launch
        val info = musicPlayerDB.firstOrNull()
        val list = lists.filter { it.id != player.player?.id }

        Log.d("TAG", "suggestPlaylistsSongs: data111 ${list.size}")
        info?.list = listOf(player.player!!) + list
        musicPlayerDB = flowOf(info)
    }

    fun pauseSongOnSleep() = CoroutineScope(Dispatchers.IO).launch {
        val d = musicPlayerDB.first()
        if (d?.isPlaying == true) {
            pause()
            NotificationUtils(
                context.resources.getString(R.string.sleep_mode_activated),
                context.resources.getString(R.string.your_song_paused_as_scheduled),
                null
            )
        }
    }

    fun setWakeAlarm() = CoroutineScope(Dispatchers.IO).launch {
        val d = wakeUpMusicDataDB.first()
        val m = musicPlayerDB.firstOrNull()
        isNewPlay = false
        if (d?.id == null && m?.player?.id != null) {
            loadURL(m.player)
            NotificationUtils(
                context.resources.getString(R.string.wake_up_alarm),
                context.resources.getString(R.string.scheduled_song_started_playing),
                null
            )
        } else if (d?.id != null) {
            loadURL(d)
            musicPlayerDB =
                flowOf(MusicPlayerData(listOf(d), d, VIDEO_BUFFERING, 0, false, 0, true))
            NotificationUtils(
                context.resources.getString(R.string.wake_up_alarm),
                context.resources.getString(R.string.scheduled_song_started_playing),
                null
            )
        }
    }

    private fun playARadio(v: String) = CoroutineScope(Dispatchers.Main).launch {
        val resource = if (v.contains(".m3u8")) resources.openRawResource(R.raw.hls_audio_player)
        else resources.openRawResource(R.raw.radio_audio_player)

        val html = readHTMLFromUTF8File(resource).replace("<<AudioURL>>", v)
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
        if (isActive) cancel()
    }

    private fun playAVideo(v: String) = CoroutineScope(Dispatchers.Main).launch {
        val html = readHTMLFromUTF8File(resources.openRawResource(R.raw.yt_music_player)).replace(
            "<<VideoID>>", v.trim()
        ).replace("<<Quality>>", songQualityDB.first().value)

        webView.loadDataWithBaseURL(YOUTUBE_URL, html, "text/html", "UTF-8", null)
        if (isActive) cancel()
    }

    private fun pause() = CoroutineScope(Dispatchers.Main).launch {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) exoPlayer?.pause()
        else webView.evaluateJavascript("pauseSong();", null)
        if (isActive) cancel()
    }

    private fun seek5sPlus() = CoroutineScope(Dispatchers.Main).launch {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) {
            val duration = ((exoPlayer?.currentPosition ?: 0).toInt() + 5000)
            exoPlayer?.seekTo(duration.toLong())
        } else webView.evaluateJavascript("seekTo5sForward();", null)
        if (isActive) cancel()
    }

    private fun seek5sMinus() = CoroutineScope(Dispatchers.Main).launch {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) {
            val duration = ((exoPlayer?.currentPosition ?: 0).toInt() - 5000)
            exoPlayer?.seekTo(duration.toLong())
        } else webView.evaluateJavascript("seekTo5sBack();", null)
        if (isActive) cancel()
    }

    private fun play() = CoroutineScope(Dispatchers.Main).launch {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) exoPlayer?.play()
        else webView.evaluateJavascript("playSong();", null)
        if (isActive) cancel()
    }

    private suspend fun getDurations() {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) {
            val d = withContext(Dispatchers.IO) { musicPlayerDB.first() }
            d?.totalDuration = ((exoPlayer?.duration ?: 0).toInt() / 1000)
            d?.currentDuration = ((exoPlayer?.currentPosition ?: 0).toInt() / 1000)
            musicPlayerDB = flowOf(d)
            MusicPlayerNotifications(
                this, exoPlayer?.isPlaying == true, d?.player, d?.totalDuration, d?.currentDuration
            ).generate()
        } else webView.evaluateJavascript("playerDurations();", null)
    }

    private fun seekTo(v: Int) = CoroutineScope(Dispatchers.Main).launch {
        if (playerInfo?.type() == MusicType.OFFLINE_SONGS) exoPlayer?.seekTo((v * 1000).toLong())
        else webView.evaluateJavascript("seekTo($v);", null)
        if (isActive) cancel()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        pause()
    }

    private fun updatePlaybackSpeed() = CoroutineScope(Dispatchers.IO).launch {
        val v = when (musicSpeedSettings.first()) {
            MusicSpeed.`025` -> 0.25
            MusicSpeed.`05` -> 0.5
            MusicSpeed.`1` -> 1.0
            MusicSpeed.`15` -> 1.5
            MusicSpeed.`20` -> 2.0
        }
        withContext(Dispatchers.Main) {
            if (playerInfo?.type() == MusicType.OFFLINE_SONGS) exoPlayer?.setPlaybackSpeed(v.toFloat())
            webView.evaluateJavascript("playbackSpeed($v);", null)
        }
    }

    private val webViewChromeClientObject = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//            Log.d("TAG", "webview: logs ${consoleMessage?.message()}")
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

    private fun destroyExoPlayer() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer?.clearVideoSurface()
        exoPlayer = null
    }

    private fun checkAndPlayNextSong() = CoroutineScope(Dispatchers.IO).launch {
        if (sleepTime == -1L) {
            sleepTime = 0
            pause()
            return@launch
        }
        val id = musicPlayerDB.firstOrNull()?.player ?: return@launch
        if (musicLoopSettings.firstOrNull() == true) {
            loadURL(id)
            return@launch
        }
        forwardAndRewindSong(true)
    }


    private fun forwardAndRewindSong(nextSong: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        val id = musicPlayerDB.firstOrNull()?.player ?: return@launch
        try {
            val player = musicPlayerDB.firstOrNull()
            val index = player?.list?.indexOfFirst { it.id == id.id } ?: -1
            if (index < 0) return@launch

            val p = if (nextSong) index + 1 else index - 1
            val data = if (p >= player?.list?.size!!) player.list?.firstOrNull()
            else player.list?.get(p)
            data ?: return@launch

            val new = MusicPlayerData(player.list, data, VIDEO_BUFFERING, 0, false, 0, true)
            musicPlayerDB = flowOf(new)
            delay(500)
            loadURL(data)
        } catch (e: Exception) {
            e.message
        }
    }

    private fun runSleepTimer(timer: Int) {
        sleepTimer?.cancel()
        sleepTime = getTimeAfterMinutesInMillis(timer)
        if (timer == 0 || timer == -1) sleepTime = timer.toLong()
        if (timer <= 0) return
        sleepTimer = CoroutineScope(Dispatchers.IO).launch {
            delay(timer.minutes)
            pause()
            sleepTime = 0
        }
    }
}