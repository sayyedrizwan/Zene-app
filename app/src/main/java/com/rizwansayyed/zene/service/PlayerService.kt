package com.rizwansayyed.zene.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.autoplaySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.loopSettings
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.RADIO_NAME
import com.rizwansayyed.zene.receivers.ChargingDeviceReceiver
import com.rizwansayyed.zene.service.implementation.listener.ScreenLockAndOnListener
import com.rizwansayyed.zene.service.implementation.recentplay.RecentPlayingSongInterface
import com.rizwansayyed.zene.service.player.listener.PlayServiceListener
import com.rizwansayyed.zene.service.player.notificationservice.PlayerServiceNotificationInterface
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM_NO_PLAY
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_AT_END_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_NEXT_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_ACTION
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_TYPE
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_LIVE_RADIO
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_PAUSE_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SEEK_TO_TIMESTAMP
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SONG_MEDIA_POSITION
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.pauseSongChange
import com.rizwansayyed.zene.service.songparty.Utils.ActionFunctions.playSongChange
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.printStack
import com.rizwansayyed.zene.utils.Utils.registerAppNonReceiver
import com.rizwansayyed.zene.utils.Utils.registerAppReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    companion object {
        var retry = 0
        var currentPlayingMusic: String = ""
    }

    private var timeJob: Job? = null

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var recentPlaying: RecentPlayingSongInterface

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var playerNotification: PlayerServiceNotificationInterface

    @Inject
    lateinit var playerServiceAction: PlayerServiceActionInterface

    private val screenLockListener by lazy { ScreenLockAndOnListener(player) }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        registerEvent(FirebaseEvents.FirebaseEvent.STARTED_PLAYER_SERVICE)

        try {
            playerNotification.buildNotification(this@PlayerService)
        } catch (e: Exception) {
            e.message
        }

        player.addListener(playerListener)
        screenLockListener.register()

        IntentFilter(PLAYER_SERVICE_ACTION).apply {
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            registerAppReceiver(receiver, this, this@PlayerService)
        }

        IntentFilter().apply {
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            registerAppNonReceiver(ChargingDeviceReceiver(), this, this@PlayerService)
        }

        timeJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                val checkRadio =
                    withContext(Dispatchers.Main) { player.currentMediaItem?.mediaMetadata?.artist }
                if (checkRadio == RADIO_NAME) {
                    delay(1.seconds)
                    return@launch
                }
                val doLoop = loopSettings.first()
                val autoPlay = autoplaySettings.first()

                if (doLoop) withContext(Dispatchers.Main) {
                    if (player.duration - player.currentPosition <= 1000)
                        player.seekTo(0)
                }

                if (!autoPlay) withContext(Dispatchers.Main) {
                    if (player.duration - player.currentPosition <= 2000) {
                        player.pause()
                        player.seekTo(player.duration)
                        PlayServiceListener.getInstance().isBuffering(false)
                    }
                }

                recentPlaying.updateRecentPlayingSongInfo().catch { }.collect()
                recentPlaying.updateLatestListenTiming().catch { }.collect()

                delay(1.seconds)
            }
        }

        return START_STICKY
    }

    fun playerError(error: PlaybackException) = CoroutineScope(Dispatchers.IO).launch {
        val artist =
            withContext(Dispatchers.Main) { player.currentMediaItem?.mediaMetadata?.artist }
        if (artist == RADIO_NAME) return@launch
        if (retry >= 1) return@launch

        if (error.message?.lowercase()?.trim()?.contains("source error") == true) {
            registerEvent(FirebaseEvents.FirebaseEvent.DOWNLOADING_SONG_URL)
            retry += 1
            withContext(Dispatchers.Main) {
                currentPlayingMusic = player.currentMediaItem?.mediaId ?: ""
                playerServiceAction.updatePlaying(player.currentMediaItem)
            }
        }
        if (isActive) cancel()
    }


    private val playerListener = object : Player.Listener {
        override fun onPlayerErrorChanged(error: PlaybackException?) {
            super.onPlayerErrorChanged(error)
            error ?: return
            playerError(error)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            PlayServiceListener.getInstance().playingState()
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            playerError(error)
        }

        @SuppressLint("SwitchIntDef")
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            PlayServiceListener.getInstance()
                .isBuffering(playbackState == Player.STATE_BUFFERING)

            when (playbackState) {
                Player.STATE_READY -> CoroutineScope(Dispatchers.IO).launch {
                    retry = 0
                    val checkRadio =
                        withContext(Dispatchers.Main) { player.currentMediaItem?.mediaMetadata?.artist }
                    if (checkRadio == RADIO_NAME) return@launch

                    playerServiceAction.updatePlaybackSpeed()
                    playerServiceAction.updateSongsWallpaper()

                    if (isActive) cancel()
                }

                Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT -> retry = 0
                Player.STATE_ENDED -> retry = 0
                Player.STATE_BUFFERING -> {}
                Player.STATE_IDLE -> {}
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            mediaItem?.let { PlayServiceListener.getInstance().mediaItemUpdate(it) }

            registerEvent(FirebaseEvents.FirebaseEvent.CHANGE_SONG_TRACK_ITEM)

            CoroutineScope(Dispatchers.Main).launch {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO || reason == Player.MEDIA_ITEM_TRANSITION_REASON_SEEK) {
                    if (currentPlayingMusic != mediaItem?.mediaId)
                        playerServiceAction.updatePlaying(mediaItem)
                }

                if (isActive) cancel()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            when (intent.getStringExtra(PLAYER_SERVICE_TYPE)) {
                ADD_ALL_PLAYER_ITEM -> CoroutineScope(Dispatchers.IO).launch {
                    val list = moshi.adapter(Array<MusicData?>::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    val position = intent.getIntExtra(SONG_MEDIA_POSITION, 0)

                    currentPlayingMusic = list?.get(position)?.songId ?: ""
                    playerServiceAction.addMultipleItemsAndPlay(list, position)
                    if (isActive) cancel()
                }

                ADD_ALL_PLAYER_ITEM_NO_PLAY -> CoroutineScope(Dispatchers.IO).launch {
                    val list = moshi.adapter(Array<MusicData?>::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    val position = intent.getIntExtra(SONG_MEDIA_POSITION, 0)

                    currentPlayingMusic = list?.get(position)?.songId ?: ""
                    playerServiceAction.addMultipleItemsAndNotPlay(list, position)
                    if (isActive) cancel()
                }

                PLAY_LIVE_RADIO -> CoroutineScope(Dispatchers.IO).launch {
                    val r = moshi.adapter(OnlineRadioResponseItem::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    r?.let { playerServiceAction.playLiveRadio(it) }
                    if (isActive) cancel()
                }

                ADD_PLAY_NEXT_ITEM -> CoroutineScope(Dispatchers.IO).launch {
                    val r = moshi.adapter(MusicData::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    r?.let { playerServiceAction.addItemToNext(it) }
                    if (isActive) cancel()
                }

                ADD_PLAY_AT_END_ITEM -> CoroutineScope(Dispatchers.IO).launch {
                    val r = moshi.adapter(MusicData::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    r?.let { playerServiceAction.addItemToEnd(it) }
                    if (isActive) cancel()
                }

                PLAY_PAUSE_MEDIA -> CoroutineScope(Dispatchers.Main).launch {
                    val doPlay = intent.getBooleanExtra(PLAY_SONG_MEDIA, false)
                    if (doPlay) player.play() else player.pause()
                    if (isActive) cancel()
                }

                SEEK_TO_TIMESTAMP -> CoroutineScope(Dispatchers.IO).launch {
                    val seekTo = intent.getLongExtra(SONG_MEDIA_POSITION, 0)
                    playerServiceAction.seekTo(seekTo)
                    if (isActive) cancel()
                }
            }
        }
    }

    override fun onDestroy() {
        try {
            player.release()
        } catch (e: Exception) {
            e.printStack()
        }
        try {
            mediaSession.release()
        } catch (e: Exception) {
            e.printStack()
        }
        try {
            unregisterReceiver(receiver)
        } catch (e: Exception) {
            e.printStack()
        }
        try {
            timeJob?.cancel()
        } catch (e: Exception) {
            e.printStack()
        }
        try {
            screenLockListener.unregister()
        } catch (e: Exception) {
            e.printStack()
        }
        super.onDestroy()
    }
}