package com.rizwansayyed.zene.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.net.toUri
import coil.imageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.service.player.utils.Utils.downloadImageAsBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.InetAddress
import java.util.Calendar
import kotlin.system.exitProcess


object Utils {

    object AppUrl {
        private const val APP_URL = "https://zene.vercel.app"

        fun appUrlSongShare(id: String): String {
            val changeIdToOurs = id.lowercase().trim()
            return "$APP_URL/s/$changeIdToOurs"
        }

    }

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

    fun forSearchTxt(name: String): String {
        val count = name.count { it == '|' }
        if (count > 2) {
            return name.substringBefore("|")
        }
        return name
    }


    fun checkAndClearCache() = CoroutineScope(Dispatchers.IO).launch {
        var size: Long = 0
        for (f in context.cacheDir.listFiles()!!) {
            size += f.length()
        }
        val sizeInMB = size / (1024 * 1024)
        if (sizeInMB.toInt() > 500) {
            context.cacheDir.deleteRecursively()
        }

        if (isActive) cancel()
    }

    suspend fun bitmapFromURL(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .target(onSuccess = { result ->
                    bitmap = result.toBitmapOrNull()
                }).build()
            context.imageLoader.execute(request)
        }
        return bitmap
    }

    fun ifPlayerServiceNotRunning(): Boolean {
        return (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
            .getRunningServices(Integer.MAX_VALUE)
            .any { it.service.className == PlayerService::class.java.name }
    }


    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    fun littleVibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibe = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibe)
        } else
            vibrator.vibrate(100)
    }

    fun homeSetWallpaper(url: String) {
        downloadImageAsBitmap(url.toUri()) {

        }
    }
}