package com.rizwansayyed.zene.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.time.Instant
import java.util.Calendar
import java.util.Date
import kotlin.system.exitProcess


object Utils {
    fun isInternetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }

    fun daysOldTimestamp(days: Int = -2): Long {
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, days)
        }
        return calendar.timeInMillis / 1000
    }

    fun timestampDifference(ts: Long): Long {
        val time = ts - System.currentTimeMillis()
        return time / 1000
    }

    fun artistsListToString(list: MutableList<String>): String {
        val artistName = buildString {
            for (i in 0 until list.size) {
                if (i > 0) {
                    if (i == list.size.minus(1)) {
                        append(" & ")
                    } else {
                        append(", ")
                    }
                }
                append(list[i])
            }
        }

        return artistName
    }

    fun restartTheApp(context: Activity) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        context.finishAffinity()
        context.startActivity(intent)
        exitProcess(0)
    }

    suspend fun isInternetAvailable(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                InetAddress.getByName("www.google.com")
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}