package com.rizwansayyed.zene.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification

class ForegroundService : Service() {

    private val backgroundLocation by lazy { BackgroundLocationTracking(this) }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        EmptyServiceNotification.generate(this)
        backgroundLocation.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundLocation.stop()
    }

}