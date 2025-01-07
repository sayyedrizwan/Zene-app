package com.rizwansayyed.zene.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.MainActivity


class ForegroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundNotification()
    }

    private fun startForegroundNotification() {
        try {
            val mainIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this, 1011, mainIntent, PendingIntent.FLAG_IMMUTABLE
            )
            val notification = NotificationCompat.Builder(this, "ZENE_SERVICE")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setContentIntent(pendingIntent)
                .build()

            val serviceType =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                else 0

            ServiceCompat.startForeground(this, 1011, notification, serviceType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}