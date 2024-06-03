package com.rizwansayyed.zene.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Context.BATTERY_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.admobCacheTimestamp
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.encryptData
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.simpleEncode
import com.rizwansayyed.zene.utils.Utils.AdsId.OPEN_ADS_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


object Utils {

    const val OFFSET_LIMIT = 50

    const val OFFICIAL_EMAIL = "knocknock@zenemusic.co"
    const val OFFICIAL_ADS_PAGE = "https://zenemusic.co/adswithus"
    const val ZENE_BUY_ME_A_COFFEE = "https://bit.ly/3XphmuL"
    const val ZENE_BUY_ME_A_COFFEE_MEMBERSHIP = "https://bit.ly/3wQav2B"
    const val OFFICIAL_PRIVACY_POLICY = "https://www.zenemusic.co/privacy-policy"
    const val OFFICIAL_DOWNLOAD_APP = "https://www.zenemusic.co/download"
    const val OFFICIAL_INSTAGRAM_ACCOUNTS = "https://www.instagram.com/zene__music/"

    @RequiresApi(Build.VERSION_CODES.Q)
    const val MEDIA_PLAYBACK_SERVICE = ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK

    val tempEmptyList =
        listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    object FCMTopics {
        const val allUsers = "all_users"
    }

    object AdsId {
        val OPEN_ADS_ID =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else "ca-app-pub-2941808068005217/7650500204"
        val NATIVE_ADS_ID =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1044960115" else "ca-app-pub-2941808068005217/4139593004"
        val BANNER_ADS_ID =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/6300978111" else "ca-app-pub-2941808068005217/1624020934"
    }

    object ExtraUtils {
        const val DOWNLOAD_SONG_WORKER = "download_song_worker"
    }

    object AppUrl {
        const val APP_URL = "https://zenemusic.co"
        const val SONG_URL_DIFFERENTIATE = "/s/"
        const val PARTY_URL_DIFFERENTIATE = "/party/"
        const val ALBUMS_URL_DIFFERENTIATE = "/album/"
        const val ARTIST_URL_DIFFERENTIATE = "/a/"
        const val RADIO_URL_DIFFERENTIATE = "/radio/"

        fun appUrlSongShare(id: String): String {
            val changeIdToOurs = simpleEncode(id.trim())
            return "$APP_URL$SONG_URL_DIFFERENTIATE$changeIdToOurs".trim()
        }

        fun appUrlRadioShare(id: String): String {
            val changeIdToOurs = simpleEncode(id.trim())
            return "$APP_URL$RADIO_URL_DIFFERENTIATE$changeIdToOurs".trim()
        }

        fun appUrlArtistsShare(name: String): String {
            val changeNameToOurs = encryptData(name.trim())
            return "$APP_URL$ARTIST_URL_DIFFERENTIATE$changeNameToOurs".trim()
        }

        fun appUrlAlbums(id: String): String {
            val changeIdToOurs = simpleEncode(id.trim())
            return "$APP_URL$ALBUMS_URL_DIFFERENTIATE$changeIdToOurs".trim()
        }

        fun appPartyJoinUrl(roomId: String): String {
            return "$APP_URL$PARTY_URL_DIFFERENTIATE$roomId".trim()
        }

        fun urlUriType(url: String): MusicType? {
            return if (url.contains("$APP_URL$SONG_URL_DIFFERENTIATE")) MusicType.MUSIC
            else if (url.contains("$APP_URL$RADIO_URL_DIFFERENTIATE")) MusicType.RADIO
            else if (url.contains("$APP_URL$ARTIST_URL_DIFFERENTIATE")) MusicType.ARTISTS
            else if (url.contains("$APP_URL$ALBUMS_URL_DIFFERENTIATE")) MusicType.ALBUMS
            else if (url.contains("$APP_URL$PARTY_URL_DIFFERENTIATE")) MusicType.PARTY
            else null
        }

    }

    fun loadOpenAppAds(context: Activity, adLoadedCallback: AppOpenAd.AppOpenAdLoadCallback) =
        CoroutineScope(Dispatchers.IO).launch {
            if (doShowSplashScreen.firstOrNull() == true) return@launch
            if (timestampDifference(admobCacheTimestamp.first()) < 2.minutes.inWholeSeconds) return@launch

            CoroutineScope(Dispatchers.Main).launch {
                val request = AdRequest.Builder().build()
                AppOpenAd.load(context, OPEN_ADS_ID, request, adLoadedCallback)
            }
        }

    private var bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
    private var myKM = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager


    fun is24Hour() = DateFormat.is24HourFormat(context)
    fun phoneBatteryLevel() = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

    fun isDeviceLocked() = myKM.isDeviceLocked

    fun isInternetConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null
    }

    fun Exception.printStack() {
        Log.d(context.packageName, "Throwable Exception: $message")
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
        val time = System.currentTimeMillis() - ts
        return time / 1000
    }

    fun String.toLongWithPlaceHolder(): Long {
        return try {
            this.toLong()
        } catch (e: Exception) {
            0
        }
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
            intent.setPackage("com.android.chrome")
            intent.flags = FLAG_ACTIVITY_NEW_TASK
        }.launchUrl(context, this@customBrowser)
    }

    fun shareTxt(txt: String) {
        val sendIntent: Intent = Intent().apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, txt)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        shareIntent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
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
            e.printStack()
        }
    }


    fun cacheSize(): String {
        val totalSize =
            context.cacheDir.listFiles()?.fold(0L) { sum, file -> sum + file.length() } ?: 0L

        val sizeInKb = totalSize / 1024
        val sizeInMb = totalSize / (1024 * 1024)
        val sizeInGb = totalSize / (1024 * 1024 * 1024)

        return when {
            sizeInGb > 0 -> "$sizeInGb GB"
            sizeInMb > 0 -> "$sizeInMb MB"
            else -> "$sizeInKb KB"
        }
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

    fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "${seconds}s ago"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            else -> {
                val dateFormat = java.text.SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                val date = Date(timestamp)
                dateFormat.format(date)
            }
        }
    }

    fun Uri.copyFileTo(dest: File): Boolean {
        dest.deleteRecursively()
        return try {
            val inputStream = context.contentResolver.openInputStream(this) ?: return false
            val outputStream = FileOutputStream(dest)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            true
        } catch (e: Exception) {
            false
        }

    }

    fun File.copyFileTo(dest: File) {
        val `in`: InputStream = FileInputStream(this)
        val out: OutputStream = FileOutputStream(dest)

        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (`in`.read(buffer).also { bytesRead = it } > 0) {
            out.write(buffer, 0, bytesRead)
        }

        `in`.close()
        out.close()
        MediaScannerConnection.scanFile(context, arrayOf(dest.toString()), null, null)
    }

    fun registerAppReceiver(r: BroadcastReceiver, i: IntentFilter, c: Context) {
        ContextCompat.registerReceiver(c, r, i, ContextCompat.RECEIVER_EXPORTED)?.apply {
            setPackage(c.packageName)
        }
    }

    fun registerAppNonReceiver(r: BroadcastReceiver, i: IntentFilter, c: Context) {
        ContextCompat.registerReceiver(c, r, i, ContextCompat.RECEIVER_NOT_EXPORTED)?.apply {
            setPackage(c.packageName)
        }
    }

    fun shareFeedback() {
        Intent(Intent.ACTION_SEND).apply {
            setType("plain/text")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(OFFICIAL_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            putExtra(Intent.EXTRA_TEXT, "")
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun openAppOnPlayStore(){
        Intent(Intent.ACTION_VIEW).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            context.startActivity(this)
        }
    }
}