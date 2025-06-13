package com.rizwansayyed.zene.service.player.utils

import android.os.Handler
import android.os.Looper

object ServiceStopTimerManager {
    private val handler = Handler(Looper.getMainLooper())
    private var stopRunnable: Runnable? = null

    fun startTimer(onTimeout: () -> Unit) {
        stopRunnable?.let { handler.removeCallbacks(it) }

        stopRunnable = Runnable {
            onTimeout()
        }
        handler.postDelayed(stopRunnable!!, 30 * 60 * 1000)
    }

    fun cancelTimer() {
        stopRunnable?.let {
            handler.removeCallbacks(it)
        }
        stopRunnable = null
    }
}