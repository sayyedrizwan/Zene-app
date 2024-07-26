package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.DataStoreManager.searchHistoryDB
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.URLS.USER_AGENT_D
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale


object Utils {

    object IDs {
        const val AD_UNIT_ID: String = "ca-app-pub-2941808068005217/7650500204"
        const val AD_INTERSTITIAL_UNIT_ID: String = "ca-app-pub-2941808068005217/1745328219"
        val AD_BANNER_ID: String =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else "ca-app-pub-2941808068005217/1624020934"
    }

    object URLS {
        const val PRIVACY_POLICY = "https://www.zenemusic.co/privacy-policy"

        const val YOUTUBE_MUSIC = "https://www.youtube.com/music"
        const val YOUTUBE_URL = "https://www.youtube.com"


        const val BASE_URL_IP = "http://ip-api.com/"
        const val JSON_IP = "json"

        val BASE_URL =
            if (BuildConfig.DEBUG) "http://192.168.0.101:5173/-api-/" else "http://www.zenemusic.co/-api-/"
        const val ZENE_USER_API = "zuser/users"
        const val ZENE_USER_SONG_HISTORY_API = "zuser/songhistory"
        const val ZENE_USER_UPDATE_ARTISTS_API = "zuser/updateartists"


        const val ZENE_MOODS_API = "moods"
        const val ZENE_NEW_RELEASE_API = "newrelease"
        const val ZENE_PLAYLISTS_API = "playlists"
        const val ZENE_SUGGESTED_SONGS_API = "suggestedsongs"
        const val ZENE_ARTISTS_INFO_API = "artistsdata/info"
        const val ZENE_ARTISTS_DATA_API = "artistsdata/data"
        const val ZENE_PLAYER_SUGGESTED_SONGS_API = "player/suggestedsongs"
        const val ZENE_PLAYER_MERCHANDISE_API = "player/merchandise"
        const val ZENE_PLAYER_LYRICS_API = "player/lyrics"
        const val ZENE_PLAYER_VIDEO_DATA_API = "player/videosdata"
        const val ZENE_SEARCH_API = "search"
        const val ZENE_SEARCH_SUGGESTIONS_API = "search_suggestions"
        const val ZENE_TOP_GLOBAL_ARTISTS_API = "top/globalartists"
        const val ZENE_TOP_LISTEN_SONGS_API = "top/listensongs"
        const val ZENE_TOP_ARTISTS_API = "top/artistssongs"
        const val ZENE_TOP_PLAYLISTS_API = "top/playlists"
        const val ZENE_TOP_ALBUMS_API = "top/albums"
        const val ZENE_TOP_VIDEOS_API = "top/videos"
        const val ZENE_TOP_SONGS_API = "top/songs"


        const val USER_AGENT_D =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36"

    }

    const val TOTAL_GRID_SIZE = 12
    const val TWO_GRID_SIZE = 6
    const val THREE_GRID_SIZE = 4

    const val RADIO_ARTISTS = "_radio_artists"


    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    fun Any.toast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@toast.toString(), Toast.LENGTH_LONG).show()
    }

    fun ytThumbnail(id: String): String {
        return "https://i.ytimg.com/vi/${id}/maxresdefault.jpg"
    }

    fun convertItToMoney(s: String): String {
        return try {
            val m = s.toInt()
            return DecimalFormat("#,###").format(m)
        } catch (e: Exception) {
            s
        }
    }

    fun openBrowser(url: String) = CoroutineScope(Dispatchers.Main).launch {
        try {
            val customTabsIntent = CustomTabsIntent.Builder()
            customTabsIntent.setUrlBarHidingEnabled(true)
            customTabsIntent.setShowTitle(true)

            customTabsIntent.setStartAnimations(
                context, android.R.anim.fade_in, android.R.anim.fade_out
            ).setExitAnimations(
                context, android.R.anim.slide_in_left, android.R.anim.slide_out_right
            )

            val build = customTabsIntent.build()
            build.intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            build.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            i.addFlags(FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
    }

    fun enterUniqueSearchHistory(txt: String) = CoroutineScope(Dispatchers.IO).launch {
        val array = ArrayList<String>()
        val list = searchHistoryDB.first()?.toList() ?: emptyList()
        array.add(txt.trim())
        list.forEach {
            if (array.size <= 100 && it.trim() != txt.trim()) array.add(it)
        }
        searchHistoryDB = flowOf(array.toTypedArray())
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.enable() {
        isFocusable = true
        requestFocus()
        isFocusableInTouchMode = true
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.allowFileAccess = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.mediaPlaybackRequiresUserGesture = false
//        settings.userAgentString = USER_AGENT_D
    }

    fun readHTMLFromUTF8File(inputStream: InputStream): String {
        inputStream.use {
            try {
                val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
                return bufferedReader.readLines().joinToString("\n")
            } catch (e: Exception) {
                return ""
            }
        }
    }

    fun getPercentageLeft(current: Int?, total: Int?): Double {
        return try {
            ((current?.toDouble() ?: 0.0) / (total ?: 0.0).toDouble()) * 100
        } catch (e: Exception) {
            0.0
        }
    }

    fun Context.vibratePhone() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER.uppercase()
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE

        return "$manufacturer $model, Android $osVersion"
    }
}