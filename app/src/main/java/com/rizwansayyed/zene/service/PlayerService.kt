package com.rizwansayyed.zene.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_ACTION
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_TYPE
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SONG_MEDIA_POSITION
import com.rizwansayyed.zene.service.player.notificationservice.PlayerServiceNotificationInterface
import com.rizwansayyed.zene.service.player.playeractions.PlayerServiceActionInterface
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

    private val playerListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("TAG", "onPlayerError: data $retry")
                if (retry >= 1) return@launch

                if (error.message?.lowercase()?.trim()?.contains("source error") == true) {
                    retry += 1
                    withContext(Dispatchers.Main) {
                        currentPlayingMusic = player.currentMediaItem?.mediaId ?: ""
                        Log.d("TAG", "onPlayerError: data runned")
                        playerServiceAction.updatePlaying(player.currentMediaItem)
                    }
                }

                if (isActive) cancel()
            }
        }

//        override fun onPlayerErrorChanged(error: PlaybackException?) {
//            super.onPlayerErrorChanged(error)
//
//            Log.d("TAG", "onPlayerErrorChanged: running")
//        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            Log.d("TAG", "onMediaItemTransition: runnnnedd 111 == $playbackState ")
            if (playbackState == Player.STATE_READY) {
                retry = 0
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            super.onMediaItemTransition(mediaItem, reason)
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
                ADD_ALL_PLAYER_ITEM -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val list = moshi.adapter(Array<MusicData?>::class.java)
                            .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                        val position = intent.getIntExtra(SONG_MEDIA_POSITION, 0)

                        currentPlayingMusic = list?.get(position)?.pId ?: ""
                        playerServiceAction.addMultipleItemsAndPlay(list, position)
                        if (isActive) cancel()
                    }
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