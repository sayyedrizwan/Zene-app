package com.rizwansayyed.zene.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL_ID
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import kotlin.system.exitProcess

class AppCrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    private var lastCrashTime = daysOldTimestamp(-2)

    override fun uncaughtException(t: Thread, e: Throwable) {
        context.cacheDir.deleteRecursively()

        if (BuildConfig.DEBUG) {
            showCrashNotification(
                "The App crash on ${t.name} thread", e.message ?: "No Crash Registered"
            )
            generateCrashLog(e)
        }

        if (!BuildConfig.DEBUG) FirebaseCrashlytics.getInstance().recordException(e)

        if (timestampDifference(lastCrashTime) > 15)
            Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        else {
            showCrashNotification(
                context.resources.getString(R.string.app_crashed),
                context.resources.getString(R.string.app_crashed)
            )

            exitProcess(0)
        }

        lastCrashTime = System.currentTimeMillis()
    }

    private fun generateCrashLog(e: Throwable) {
        val stackTrace: Array<StackTraceElement> = e.stackTrace
        if (stackTrace.isNotEmpty()) {
            val element = stackTrace[0]
            val fileName = element.fileName
            val lineNumber = element.lineNumber
            val methodName = element.methodName

            Log.e(context.packageName, "App Crash Log: Crash Info ${e.message}")
            Log.e(context.packageName, "App Crash Log: Crash at $element")
            Log.e(context.packageName, "App Crash Log: Crash file $fileName")
            Log.e(context.packageName, "App Crash Log: Crash number $lineNumber")
            Log.e(context.packageName, "App Crash Log: Crash method $methodName")
        } else {
            Log.e(context.packageName, "App Crash Log: Unknown error occurred")
        }
    }

    private fun showCrashNotification(t: String, d: String) {
        NotificationViewManager(context)
            .title(t).body(d).nIds(CRASH_CHANNEL_ID, CRASH_CHANNEL).generate()

    }
}