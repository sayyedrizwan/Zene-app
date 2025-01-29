package com.rizwansayyed.zene.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.ui.main.MainActivity

class ForegroundService : Service() {

    private val backgroundLocation by lazy { BackgroundLocationTracking(this) }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundNotification()
        backgroundLocation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundLocation.stop()
    }

    private fun startForegroundNotification() {
        try {
            val channelName = context.resources.getString(R.string.zene_music_player)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel(
                    packageManager.toString(), channelName, NotificationManager.IMPORTANCE_NONE
                ).apply {
                    lightColor = Color.BLUE
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                    val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    manager.createNotificationChannel(this)
                }
            }

            val mainIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 2, mainIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(this, packageManager.toString())
            val notification: Notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.zene_logo)
                .setContentTitle("Zene is running..")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build()

            val serviceType =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                else 0

            ServiceCompat.startForeground(this, 1, notification, serviceType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}