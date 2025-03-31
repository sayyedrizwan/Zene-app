package com.rizwansayyed.zene.service.party

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PartyService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)


        return START_STICKY
    }
}