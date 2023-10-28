package com.rizwansayyed.zene.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerNotificationManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData

object Utils {
    private const val PLAYER_NOTIFICATION_ID = 350
    private const val PLAYER_NOTIFICATION_CHANNEL_NAME = "start_player_notification"
    private const val PLAYER_NOTIFICATION_CHANNEL_ID = "start_player_notification_id"


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

    @UnstableApi
    fun startPlayerNotification(
        player: ExoPlayer, service: PlayerService, mediaSession: MediaSession
    ) {

    }
}