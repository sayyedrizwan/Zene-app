package com.rizwansayyed.zene.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.fragment.app.FragmentActivity
import coil.imageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.service.PlayerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetAddress
import java.text.DecimalFormat
import java.util.Calendar
import kotlin.math.log10
import kotlin.math.pow
import kotlin.system.exitProcess


object Utils {

    const val OFFICIAL_EMAIL = "contactcreator@protonmail.com"

    val tempEmptyList =
        listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    object ExtraUtils {
        const val DOWNLOAD_SONG_WORKER = "download_song_worker"
    }

    object AppUrl {
        const val APP_URL = "https://zene.vercel.app"

        fun appUrlSongShare(id: String): String {
            val changeIdToOurs = id.trim()
            return "$APP_URL/s/$changeIdToOurs"
        }

    }

    fun isInternetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }

    fun isConnectedToWifi(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
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

    @Suppress("DEPRECATION")
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

    @Suppress("DEPRECATION")
    fun littleVibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibe = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibe)
        } else
            vibrator.vibrate(100)
    }

    fun formatNumberToFollowers(number: Int): String {
        val suffixes = listOf("K", "M", "B", "T", "P", "E")
        var num = number
        var index = 0

        while (num >= 1000 && index < suffixes.size - 1) {
            num /= 1000
            index++
        }

        return if (index == 0) {
            "$num"
        } else {
            val formattedNum = String.format("%.1f", num.toDouble())
            "$formattedNum${suffixes[index - 1]}"
        }
    }

    fun Uri.customBrowser() = CoroutineScope(Dispatchers.Main).launch {
        CustomTabsIntent.Builder().build().apply {
            intent.flags = FLAG_ACTIVITY_NEW_TASK
        }.launchUrl(context, this@customBrowser)
    }

    fun shareTxt(txt: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            flags = FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_TEXT, txt)
            context.startActivity(this)
        }
    }

    fun copyFileAndDelete(sourceFile: File, destinationFile: File) {
        try {
            if (!sourceFile.exists()) return
            val inputStream = FileInputStream(sourceFile)
            val outputStream = FileOutputStream(destinationFile)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.close()

            sourceFile.delete()
        } catch (e: Exception) {
            e.message
        }
    }


    fun cacheSize(): String {
        var size: Long = 0
        val files = context.cacheDir.listFiles()
        for (f in files!!) {
            size += f.length()
        }
        return readableFileSize(size)
    }

    private fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 Bytes"
        val units = arrayOf("Bytes", "kB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    fun isPermission(permission: String): Boolean {
        return ContextCompat
            .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun restartApp() {
        val intent = Intent(context, MainActivity::class.java)
        val mPendingIntent = PendingIntent.getActivity(
            context, 1234, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val mgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent)

        exitProcess(0)
    }

}