package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.media3.exoplayer.DefaultLoadControl
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.model.MusicPlayerDetails
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.utils.Utils.PATH.cacheLyricsFiles
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.lang.Float.NaN
import java.text.NumberFormat
import java.util.Locale


object Utils {

    object URL {
        const val ARTISTS_DATA = "artistsData"
        const val TOP_ARTIST_THIS_WEEK = "topArtistThisWeek"
        const val TOP_GLOBAL_SONGS_THIS_WEEK = "topGlobalSongsThisWeek"
        const val TOP_COUNTRY_SONGS = "topCountrySongs"
        const val SONG_SUGGESTIONS = "songSuggestions"
        const val SONG_LYRICS = "songLyrics"
        const val SONG_SUGGESTIONS_FOR_YOU = "songSuggestionsForYou"
        const val SEARCH_SONGS = "searchSongs"
        const val SONG_PLAY_DETAILS = "songPlayDetails"
        const val VIDEO_PLAY_DETAILS = "videoPlayDetails"
        const val TRENDING_SONGS_APPLE = "trendingSongsApple"
        const val TRENDING_SONGS_TOP_K_POP = "trendingSongsTopKPop"
        const val TRENDING_SONGS_TOP_50_K_POP = "trendingSongsTop50KPop"
        const val SIMILAR_ARTISTS = "similarArtists"
        const val ARTISTS_INSTAGRAM_POSTS = "artistsInstaPosts"
        const val ARTISTS_TWITTER_TWEETS = "artistsTwitterTweets"


        const val IP_JSON_BASE_URL = "http://ip-api.com/"
        const val IP_JSON = "json"


        const val SAVE_FROM_NET = "https://en.savefrom.net"
        const val TEN_DOWNLOADER = "https://10downloader.com"

        fun videoPaths(id: String): String {
            return "$SAVE_FROM_NET/#url=https://youtube.com/watch?v=$id"
        }

        fun videoPaths10(id: String): String {
            return "$TEN_DOWNLOADER/download?v=https://www.youtube.com/watch?v=$id&type=video"
        }
    }

    object DB {
        const val RECENT_PLAYED_DB = "recent_played_db"
        const val OFFLINE_SONGS_DB = "offline_songs_db"
        const val SONG_DETAILS_DB = "song_details_db"
    }

    object PATH {
        val cacheLyricsFiles = File(context.cacheDir, "lyrics").apply {
            mkdir()
            mkdirs()
        }

        val filesSongDownloader = File(context.filesDir, "offline_songs").apply {
            mkdir()
            mkdirs()
        }
    }


    object EXTRA {
        const val PLAY_URL_PATH = "play_url_path"
        const val SONG_NAME_EXTRA = "song_name_extra"
    }

    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun String.showToast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@showToast, Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

    fun String.shortTextForView(maxLength: Int = 15): String {
        if (this.length <= maxLength) return this
        return this.substring(0, maxLength - 3) + "..."
    }

    fun updateStatus(
        thumbnail: String?, songs: String, artists: String, pId: String, music: MusicPlayerState
    ) {
        val musicPlayerDetails = MusicPlayerDetails(
            thumbnail, songs, artists, pId, 0, 0, music, System.currentTimeMillis()
        )
        dataStoreManager.musicPlayerData = flowOf(musicPlayerDetails)
    }


    fun msToSongDuration(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt()
        val minutes = (seconds / 60)
        val remainingSeconds = seconds % 60

        return String.format("%d:%02d", minutes, remainingSeconds)
    }

    private const val preloadingDurationSeconds = 10
    private const val preloadingDurationMs = preloadingDurationSeconds * 1000L

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun enableCache(): DefaultLoadControl {

        val bufferForPlaybackMs = preloadingDurationMs
        val bufferForPlaybackBufferMs = preloadingDurationMs / 2
        val minBufferMs = 1000
        val maxBufferMs = preloadingDurationMs.toInt() * 2


        return DefaultLoadControl.Builder().setBufferDurationsMs(
            minBufferMs, maxBufferMs, bufferForPlaybackMs.toInt(), bufferForPlaybackBufferMs.toInt()
        ).build()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.enableAll(client: WebViewClient? = null, chrome: WebChromeClient? = null) {
        CookieManager.getInstance().also {
            it.setAcceptCookie(true)
            it.setAcceptThirdPartyCookies(this, true)
        }
        val systemUserAgent = System.getProperty("http.agent")

        settings.apply {
            userAgentString = systemUserAgent?.replace("Mobile", "Android Mobile")
                ?: "Mozilla/5.0 (Linux; Android 11; Google Pixel) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36"
            javaScriptEnabled = true
            setRenderPriority(WebSettings.RenderPriority.HIGH)
            cacheMode = WebSettings.LOAD_DEFAULT
            domStorageEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            useWideViewPort = true
            saveFormData = true
            setEnableSmoothTransition(true)
            setSupportMultipleWindows(true)

            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.databaseEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true

            setSupportMultipleWindows(true)
        }
        this.setInitialScale(1)
        this.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        this.webChromeClient = chrome ?: WebChromeClient()
        if (client != null) {
            this.webViewClient = client
        }
    }

    fun openOnYoutubeVideo(id: String) {
        val youtubePackageName = "com.google.android.youtube"

        val intent = context.packageManager.getLaunchIntentForPackage(youtubePackageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("https://www.youtube.com/watch?v=$id")
        }
        if (intent == null) {
            Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = Uri.parse("https://www.youtube.com/watch?v=$id")
                context.startActivity(this)
            }
        } else {
            context.startActivity(intent)
        }
    }


    fun openOnInstagramVideo(username: String) {
        val packageName = "com.instagram.android"

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            data = Uri.parse("https://www.instagram.com/$username")
        }
        if (intent == null) {
            Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = Uri.parse("https://www.instagram.com/$username")
                context.startActivity(this)
            }
        } else {
            context.startActivity(intent)
        }
    }


    fun saveCaptionsFileTXT(name: String, txt: String) {
        cacheLyricsFiles.mkdirs()
        val file = File(cacheLyricsFiles, "$name.txt")

        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(txt)
    }

    fun ifLyricsFileExistReturn(name: String): String {
        cacheLyricsFiles.mkdirs()
        val file = File(cacheLyricsFiles, "$name.txt")

        if (!file.exists()) {
            return ""
        }

        return FileInputStream(file).bufferedReader().use { it.readText() }
    }

    fun progressBarStatus(playerDuration: Long, lastListenDuration: Long): Float? {
        return try {
            lastListenDuration.toFloat() / playerDuration
        } catch (e: Exception) {
            null
        }
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")

    fun clearUrlForArtistsInfo(url: String): String {
        return url.substringBefore("?")
            .replace("/+wiki", "").replace("/+wiki/", "")
            .replace("/+images", "").replace("/+images/", "")
            .replace("/+tracks", "").replace("/+tracks/", "")
            .replace("/+tracks", "").replace("/+tracks/", "")
            .replace("/+albums", "").replace("/+albums/", "")
    }


    fun Int.convertToNumber(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(this)
    }

    fun convertToAbbreviation(number: Int): String {
        return when {
            number >= 1_000_000 -> "${number / 1_000_000}M"
            number >= 1_000 -> "${number / 1_000}K"
            else -> number.toString()
        }
    }

    fun getUrlPathForUrl(url: String) {

    }

}