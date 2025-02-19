package com.rizwansayyed.zene.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.media.session.MediaButtonReceiver
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_ID
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_NAME
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaSessionPlayerNotification(private val context: PlayerForegroundService) {
    private lateinit var mediaSession: MediaSessionCompat

    fun createMediaSession() {
        mediaSession = MediaSessionCompat(context, CHANNEL_MUSIC_PLAYER_NAME).apply {
            isActive = true
        }
    }

    private suspend fun loadBitmapFromUrl(imageUrl: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun updateMetadata(data: ZeneMusicData?) {
        val bitmap = loadBitmapFromUrl(data?.thumbnail ?: "")
        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, data?.name)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data?.artists)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 150000)
                .build()
        )
    }

    fun updatePlaybackState(isPlaying: Boolean) {
        val state =
            if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                            PlaybackStateCompat.ACTION_SEEK_TO
                )
                .setState(state, 150000 / 2, 1f)
                .build()
        )
    }

    fun showNotification(isPlaying: Boolean) {
        createNotificationChannel()

        val playPauseIcon = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

        val notification = NotificationCompat.Builder(context, CHANNEL_MUSIC_PLAYER_ID)
            .setContentTitle(mediaSession.controller.metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
            .setContentText(mediaSession.controller.metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
            .setLargeIcon(mediaSession.controller.metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
            .setSmallIcon(R.drawable.zene_logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(
                R.drawable.ic_backward,
                "Previous",
                getPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
            )
            .addAction(
                playPauseIcon,
                if (isPlaying) "Pause" else "Play",
                getPendingIntent(PlaybackStateCompat.ACTION_PLAY_PAUSE)
            )
            .addAction(
                R.drawable.ic_forward,
                "Next",
                getPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
            )
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(1)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context, PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
            .build()


        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }

        ServiceCompat.startForeground(context, 1, notification, serviceType)
    }

    private fun getPendingIntent(action: Long): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).setAction(action.toString())
        return PendingIntent.getBroadcast(
            context,
            action.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_MUSIC_PLAYER_ID,
                    CHANNEL_MUSIC_PLAYER_NAME,
                    NotificationManager.IMPORTANCE_LOW
                )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}