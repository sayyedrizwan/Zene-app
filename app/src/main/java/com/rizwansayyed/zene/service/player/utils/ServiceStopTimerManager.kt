package com.rizwansayyed.zene.service.player.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.minutes

object ServiceStopTimerManager {
    private var runnableJob: Job? = null

    fun startTimer(onTimeout: () -> Unit) {
        cancelTimer()
        runnableJob = CoroutineScope(Dispatchers.IO).launch {
            delay(25.minutes)
            onTimeout()
        }
    }

    fun cancelTimer() {
        runnableJob?.cancel()
        runnableJob = null
    }
}