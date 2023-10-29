package com.rizwansayyed.zene.service.player.notificationservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_CHANNEL_ID
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_CHANNEL_NAME
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
class PlayerServiceNotification @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaSession: MediaSession,
    private val player: ExoPlayer
) : PlayerServiceNotificationInterface {

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }
    private val n = PlayerNotificationManager.Builder(
        context, PLAYER_NOTIFICATION_ID, PLAYER_NOTIFICATION_CHANNEL_ID
    )

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun buildNotificationChannel() {
        val channel = NotificationChannel(
            PLAYER_NOTIFICATION_CHANNEL_ID,
            PLAYER_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    override fun buildNotification(playerService: PlayerService) {
        n.setMediaDescriptionAdapter(SimpleMediaNotificationAdapter(mediaSession.sessionActivity))
            .setSmallIconResourceId(R.mipmap.logo)
            .build().apply {
                setUseFastForwardActionInCompactView(true)
                setUseRewindActionInCompactView(true)
                setUseNextActionInCompactView(false)
                setPriority(NotificationCompat.PRIORITY_LOW)
                setMediaSessionToken(mediaSession.sessionCompatToken)
                setPlayer(player)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(context, PLAYER_NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            playerService.startForeground(PLAYER_NOTIFICATION_ID, notification)
        }
    }

}