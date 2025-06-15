package com.rizwansayyed.zene.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.ui.main.MainActivity

object EmptyServiceNotification {

    const val CHANNEL_MUSIC_PLAYER_ID = "zene_music_channel"
    val CHANNEL_MUSIC_PLAYER_NAME = context.resources.getString(R.string.zene_music_player)


    fun generate(context: PlayerForegroundService) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_MUSIC_PLAYER_ID, CHANNEL_MUSIC_PLAYER_NAME, NotificationManager.IMPORTANCE_NONE
                ).apply {
                    lightColor = Color.BLUE
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    description = context.resources.getString(R.string.zene_music_player_service)
                }
                val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }

            val mainIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 2, mainIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_MUSIC_PLAYER_ID)
            val notification: Notification =
                notificationBuilder.setOngoing(true).setSmallIcon(R.drawable.zene_logo)
                    .setContentTitle("Zene is running..")
                    .setPriority(NotificationCompat.PRIORITY_MIN).setContentIntent(pendingIntent)
                    .setCategory(Notification.CATEGORY_SERVICE).build()

            val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            } else {
                0
            }

            ServiceCompat.startForeground(context, 1, notification, serviceType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}