package com.rizwansayyed.zene.service

import android.app.Notification
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.domain.MusicData

object Utils {
    const val PLAYER_NOTIFICATION_ID = 350
    const val PLAYER_NOTIFICATION_CHANNEL_NAME = "start_player_notification"
    const val PLAYER_NOTIFICATION_CHANNEL_ID = "start_player_notification_id"


    fun MusicData.toMediaItem(url: String): MediaItem {
        val metadata = MediaMetadata.Builder().setTitle(this.name)
            .setDisplayTitle(this.name).setArtist(this.artists)
            .setArtworkUri(this.thumbnail?.toUri()).build()

        return MediaItem.Builder()
            .setUri(url)
            .setMediaId(this.pId ?: "")
            .setMediaMetadata(metadata)
            .build()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun startPlayerNotification(player: ExoPlayer, service: PlayerService) {
        PlayerNotificationManager.Builder(
            ApplicationModule.context, PLAYER_NOTIFICATION_ID, PLAYER_NOTIFICATION_CHANNEL_ID
        ).setSmallIconResourceId(R.mipmap.logo).build().apply {
//            setMediaSessionToken(mediaSession.sessionCompatToken)
            setUseFastForwardActionInCompactView(true)
            setUseRewindActionInCompactView(true)
            setUseNextActionInCompactView(false)
            setPriority(NotificationCompat.PRIORITY_LOW)
            setPlayer(player)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = Notification.Builder(ApplicationModule.context, PLAYER_NOTIFICATION_CHANNEL_ID)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()
            service.startForeground(PLAYER_NOTIFICATION_ID, notification)
        }
    }
}