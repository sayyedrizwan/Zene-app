package com.rizwansayyed.zene.service.partycall

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.rizwansayyed.zene.service.notification.callNotification


class PartyCallService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        callNotification("eeee")

        return START_STICKY
    }
}