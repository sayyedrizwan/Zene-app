package com.rizwansayyed.zene.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.SongPlayerService
import com.rizwansayyed.zene.utils.Utils.IntentExtra.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.utils.Utils.IntentExtra.SONG_MEDIA_POSITION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    object IntentExtra {
        const val PLAY_SONG_MEDIA = "play_song_media"
        const val SONG_MEDIA_POSITION = "song_media_position"
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

    fun startPlayingSong(m: Array<MusicData>?, p: Int) {
        m ?: return
        val l = moshi.adapter(Array<MusicData>::class.java).toJson(m)
        Intent(context, SongPlayerService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(PLAY_SONG_MEDIA, l)
            putExtra(SONG_MEDIA_POSITION, p)
            context.startService(this)
        }
    }
}