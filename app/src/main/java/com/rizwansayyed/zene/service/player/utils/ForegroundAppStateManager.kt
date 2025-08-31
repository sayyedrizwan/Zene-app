package com.rizwansayyed.zene.service.player.utils

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Suppress("DEPRECATION")
class ForegroundAppStateManager(private val context: PlayerForegroundService) {

    private var serviceStopTimer: Job? = null
    private var activityStateChecker: Job? = null

    private val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

    fun startActivityStateMonitoring() {
        activityStateChecker = CoroutineScope(Dispatchers.IO).safeLaunch {
            while (isActive) {
                delay(2000)

                val runningTasks = try {
                    activityManager.getRunningTasks(1)
                } catch (_: Exception) {
                    null
                }

                val isAppInForeground =
                    runningTasks?.isNotEmpty() == true && runningTasks[0].topActivity?.packageName == context.packageName

                if (!isAppInForeground) startServiceStopTimer()
                else serviceStopTimer?.cancel()
            }
        }
    }

    private fun startServiceStopTimer() {
        serviceStopTimer?.cancel()
        serviceStopTimer = CoroutineScope(Dispatchers.IO).safeLaunch {
            delay(1.seconds)
            if (isActive) {
                val runningTasks = try {
                    activityManager.getRunningTasks(1)
                } catch (_: Exception) {
                    null
                }

                val isAppInForeground = runningTasks?.isNotEmpty() == true &&
                        runningTasks[0].topActivity?.packageName == context.packageName
                if (!isAppInForeground) context.clearAll(5)
            }
        }
    }
}