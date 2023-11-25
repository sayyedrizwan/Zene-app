package com.rizwansayyed.zene.service

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
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.service.player.listener.PlayServiceListener
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_ACTION
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_TYPE
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SONG_MEDIA_POSITION
import com.rizwansayyed.zene.service.player.notificationservice.PlayerServiceNotificationInterface
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM_NO_PLAY
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_AT_END_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_NEXT_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_LIVE_RADIO
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_PAUSE_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SEEK_TO_TIMESTAMP
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    companion object {
        var retry = 0
        var currentPlayingMusic: String = ""
    }

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    @Inject
    lateinit var playerNotification: PlayerServiceNotificationInterface

    @Inject
    lateinit var playerServiceAction: PlayerServiceActionInterface

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        playerNotification.buildNotification(this@PlayerService)

        player.addListener(playerListener)

        IntentFilter(PLAYER_SERVICE_ACTION).apply {
            ContextCompat.registerReceiver(
                this@PlayerService, receiver, this, ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
        return START_STICKY
    }

    fun playerError(error: PlaybackException) = CoroutineScope(Dispatchers.IO).launch {
        if (retry >= 1) return@launch

        if (error.message?.lowercase()?.trim()?.contains("source error") == true) {
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

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
           PlayServiceListener.getInstance()
                .isBuffering(playbackState == Player.STATE_BUFFERING)

            if (playbackState == Player.STATE_READY) {
                retry = 0
            } else if (playbackState == Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT) {
                retry = 0
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
            mediaItem?.let { PlayServiceListener.getInstance().mediaItemUpdate(it) }

            CoroutineScope(Dispatchers.IO).launch {
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

                    currentPlayingMusic = list?.get(position)?.pId ?: ""
                    playerServiceAction.addMultipleItemsAndPlay(list, position)
                    if (isActive) cancel()
                }

                ADD_ALL_PLAYER_ITEM_NO_PLAY -> CoroutineScope(Dispatchers.IO).launch {
                    val list = moshi.adapter(Array<MusicData?>::class.java)
                        .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                    val position = intent.getIntExtra(SONG_MEDIA_POSITION, 0)

                    currentPlayingMusic = list?.get(position)?.pId ?: ""
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
        player.release()
        mediaSession.release()
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}