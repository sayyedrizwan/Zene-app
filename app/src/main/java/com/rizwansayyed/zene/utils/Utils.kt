package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.lastAdsTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.DataStoreManager.searchHistoryDB
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.text.DecimalFormat
import java.time.Duration
import java.time.Instant


object Utils {

    object NotificationIDS {
        const val NOTIFICATION_CHANNEL_ID = "ZENE_NOTIFICATION"
        const val NOTIFICATION_M_P_CHANNEL_ID = "ZENE_MUSIC_PLAYER"
    }

    object Share {
        const val WEB_BASE_URL = "https://www.zenemusic.co"
        const val SONG_INNER = "/s/"
        const val ARTISTS_INNER = "/a/"
        const val PLAYLIST_ALBUM_INNER = "/p/"
        const val VIDEO_INNER = "/v/"
    }

    object IDs {
        const val AD_UNIT_ID: String = "ca-app-pub-2941808068005217/7650500204"
        const val AD_INTERSTITIAL_UNIT_ID: String = "ca-app-pub-2941808068005217/1745328219"
        val AD_BANNER_ID: String =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else "ca-app-pub-2941808068005217/1624020934"
        val AD_REWARDS_ID: String =
            if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/5224354917" else "ca-app-pub-2941808068005217/6833134797"
    }

    object URLS {
        const val PRIVACY_POLICY = "https://zenemusic.co/privacy-policy"

        const val YOUTUBE_MUSIC = "https://www.youtube.com/music"
        const val YOUTUBE_URL = "https://www.youtube.com"


        const val BASE_URL_IP = "http://ip-api.com/"
        const val JSON_IP = "json"

        const val IMG_PLAYLISTS = "https://www.zenemusic.co/monthly-playlist.jpg"

        const val GRAPH_FB_API = "graph.facebook.com"

        val BASE_URL =
            if (BuildConfig.DEBUG) BuildConfig.IP_BASE_URL else BuildConfig.DOMAIN_BASE_URL

        const val ZENE_EXTRA_SPONSORS_API = "extra/sponsors"

        const val ZENE_USER_API = "zuser/users"
        const val ZENE_USER_SONG_HISTORY_API = "zuser/songhistory"
        const val ZENE_USER_PLAYLISTS_API = "zuser/playlists"
        const val ZENE_USER_MY_PLAYLISTS_API = "zuser/myplaylists"
        const val ZENE_USER_IS_SONG_IN_PLAYLISTS_API = "zuser/is_song_in_playlists"
        const val ZENE_ADD_SONGS_PLAYLISTS_API = "zuser/add_song_to_playlists"
        const val ZENE_USER_UPDATE_ARTISTS_API = "zuser/updateartists"
        const val ZENE_REMOVE_PLAYLISTS_API = "zuser/remove_playlists"

        const val ZENE_IMPORT_PLAYLISTS_SPOTIFY_API = "importplaylists/spotify"

        const val ZENE_SEARCH_IMG_API = "search_img"
        const val ZENE_FEEDS_API = "feeds"
        const val ZENE_MOODS_API = "moods"
        const val ZENE_SONG_INFO_API = "song_info"
        const val ZENE_PLAYLISTS_API = "playlists"
        const val ZENE_NEW_RELEASE_API = "newrelease"
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

        const val GOOGLE_BUNDLE_EMAIL =
            "com.google.android.libraries.identity.googleid.BUNDLE_KEY_ID"
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

    fun userAlphabetsImg(n: String): String {
        return "https://ui-avatars.com/api/?name=${n.replace(" ", "+")}" +
                "&background=2F3C7E&color=fff&size=128&length=2"
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
            build.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
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
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.enableSimple() {
        isFocusable = true
        requestFocus()
        isFocusableInTouchMode = true
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
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

    fun vibratePhone() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER.uppercase()
        val model = Build.MODEL
        val osVersion = Build.VERSION.RELEASE

        return "$manufacturer $model, Android $osVersion"
    }


    val savePlaylistFilePath = File(context.filesDir, "playlist_img.png")

    fun saveBitmap(file: File, bitmap: Bitmap?): File? {
        file.deleteRecursively()

        try {
            if (bitmap == null) return null

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val initialByteArray = stream.toByteArray()
            val fileSizeInKB = initialByteArray.size / 1024
            var compressionQuality = 100

            when {
                fileSizeInKB in 101..400 -> compressionQuality = 60
                fileSizeInKB in 401..800 -> compressionQuality = 40
                fileSizeInKB > 800 -> compressionQuality = 10
            }

            stream.reset()

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, compressionQuality, stream)
            }
            return file
        } catch (e: Exception) {
            return null
        }
    }

    fun addSongToNext(m: ZeneMusicDataItems) = CoroutineScope(Dispatchers.IO).launch {
        val dataStore = musicPlayerDB.firstOrNull() ?: return@launch
        val list = ArrayList<ZeneMusicDataItems>()

        dataStore.list?.forEach {
            if (it.id != m.id) list.add(it)

            if (it.id == dataStore.player?.id) {
                list.add(it)
                list.add(m)
            }
        }
        dataStore.list = list.distinctBy { it.id }
        musicPlayerDB = flowOf(dataStore)
    }

    fun addSongToLast(m: ZeneMusicDataItems) = CoroutineScope(Dispatchers.IO).launch {
        val dataStore = musicPlayerDB.firstOrNull() ?: return@launch
        val list = ArrayList<ZeneMusicDataItems>()

        dataStore.list?.forEach {
            list.add(it)
        }
        list.add(m)

        dataStore.list = list.distinctBy { it.id }
        musicPlayerDB = flowOf(dataStore)
    }

    fun shareTxtImage(url: String, name: String? = null) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            flags = FLAG_ACTIVITY_NEW_TASK
        }

        val shareIntent = Intent.createChooser(sendIntent, name).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(shareIntent)
    }

    suspend fun loadBitmap(img: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val url = URL(img)
            return@withContext BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            return@withContext null
        }

    }

    fun feedbackMail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.setType("plain/text")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("knocknock@zenemusic.co"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
        intent.putExtra(Intent.EXTRA_TEXT, "<<<<<<< Feedback Android >>>>>>>")

        val select = Intent.createChooser(intent, "").apply {
            flags = FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(select)
    }

    fun openEqualizer() {
        val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        intent.flags = FLAG_ACTIVITY_NEW_TASK

        if ((intent.resolveActivity(context.packageManager) != null)) {
            context.startActivity(intent)
        } else {
            context.resources.getString(R.string.equalizer_not_found).toast()
        }
    }

    fun isPermissionDisabled(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_DENIED
    }

    fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.setData(uri)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun openAppPageOnPlayStore() {
        val playStoreURL = "market://details?id=${context.packageName}"
        val playStoreBrowser =
            "https://play.google.com/store/apps/details?id=${context.packageName}"
        try {
            Intent(Intent.ACTION_VIEW, Uri.parse(playStoreURL)).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        } catch (e: Exception) {
            Intent(Intent.ACTION_VIEW, Uri.parse(playStoreBrowser)).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }

        }
    }

    fun internetIsConnected(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return (Runtime.getRuntime().exec(command).waitFor() == 0)
        } catch (e: Exception) {
            return false
        }
    }

    fun timeDifferenceInMinutes(ts: Long?): Long {
        val timestamp = ts ?: TS_LAST_DATA
        val now = Instant.now().toEpochMilli()
        val duration = Duration.between(Instant.ofEpochMilli(timestamp), Instant.ofEpochMilli(now))
        return duration.toMinutes()
    }

    fun timeDifferenceInSeconds(ts: Long?): Long {
        val timestamp = ts ?: TS_LAST_DATA
        val now = Instant.now().toEpochMilli()
        val duration = Duration.between(Instant.ofEpochMilli(timestamp), Instant.ofEpochMilli(now))
        return duration.seconds
    }

}