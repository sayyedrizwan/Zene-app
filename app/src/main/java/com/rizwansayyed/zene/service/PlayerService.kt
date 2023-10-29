package com.rizwansayyed.zene.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
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
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

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
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            when (intent.getStringExtra(PLAYER_SERVICE_TYPE)) {
                ADD_ALL_PLAYER_ITEM -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val list = moshi.adapter(Array<MusicData>::class.java)
                            .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
                        val position = intent.getIntExtra(SONG_MEDIA_POSITION, 0)
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