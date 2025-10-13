package com.rizwansayyed.zene.di.utils

import android.app.Application
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.ui.main.MainActivity
import kotlin.time.Duration.Companion.seconds

class AppCrashHandler(private val application: Application) : Thread.UncaughtExceptionHandler {

    private val crashThreshold: Int = 3
    private val crashIntervalMs: Long = 20.seconds.inWholeMilliseconds

    private val crashTimes = mutableListOf<Long>()
    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (BuildConfig.DEBUG) Log.d("TAG", "uncaughtException: ${throwable.message}")
        val now = SystemClock.elapsedRealtime()
        crashTimes.add(now)
        crashTimes.removeAll { now - it > crashIntervalMs }

        FirebaseCrashlytics.getInstance().recordException(throwable)

        if (crashTimes.size <= crashThreshold) {
            val intent = Intent(application, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            application.startActivity(intent)
        } else {
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}