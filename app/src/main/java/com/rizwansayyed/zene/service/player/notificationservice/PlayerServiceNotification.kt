package com.rizwansayyed.zene.service.player.notificationservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_CHANNEL_ID
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_CHANNEL_NAME
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotification.PLAYER_NOTIFICATION_ID
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.OPEN_MUSIC_PLAYER
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
            .setSmallIconResourceId(R.drawable.logo_color)
            .setPlayActionIconResourceId(R.drawable.ic_play)
            .setPauseActionIconResourceId(R.drawable.ic_pause)
            .setNextActionIconResourceId(R.drawable.ic_song_next)
            .setPreviousActionIconResourceId(R.drawable.ic_song_previous)
            .setRewindActionIconResourceId(R.drawable.ic_go_backward_5sec)
            .setFastForwardActionIconResourceId(R.drawable.ic_go_forward_5sec)
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
                .setContentIntent(openMusicPlayerIntent())
                .build()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) playerService.startForeground(
                PLAYER_NOTIFICATION_ID,
                notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
            else
                playerService.startForeground(PLAYER_NOTIFICATION_ID, notification)
        }
    }


    override fun openMusicPlayerIntent(): PendingIntent {
        val resultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(OPEN_MUSIC_PLAYER, true)
        }

        return TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(
                1,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}