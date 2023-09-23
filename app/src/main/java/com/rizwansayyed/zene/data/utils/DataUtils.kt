package com.rizwansayyed.zene.data.utils

import androidx.core.net.toUri
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


object DBNAME {
    const val RECENT_PLAYED_DB = "recent_played_db"
    const val OFFLINE_DOWNLOADED_SONGS_DB = "offline_songs_db"
    const val SAVED_PLAYLIST_DB = "saved_playlist_db"
}

object RadioOnlineAPI {
    const val RADIO_BASE_URL = "https://at1.api.radio-browser.info"

    fun radioSearchAPI(base: String): String {
        return "$base/json/stations/search"
    }

    const val RADIO_BASE_URLS = "all.api.radio-browser.info"
}


object IpJsonAPI {
    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_API = "json"
}

object InstagramAPI {
    const val INSTAGRAM_BASE_URL = "https://www.instagram.com/api/v1/users/"
    const val INSTAGRAM_PROFILE_API = "web_profile_info"
}

object SearchEngine {
    fun searchEngineDataURL(name: String): String {
        val n = name.lowercase().replace(" ", "+")
        return "https://www.bing.com/search?q=$n+twitter+and+instagram+official+account"
    }
}

object CacheFiles {
    val radioList by lazy { File(context.cacheDir, "radio-online.txt").apply { mkdirs() } }
    val topArtistsList by lazy { File(context.cacheDir, "top-artists-list.txt").apply { mkdirs() } }
}

object ScrapURL {
    const val TOP_ARTISTS = "https://www.billboard.com/charts/artist-100/"
}


fun getInstagramUsername(i:String): String {
    return i.substringAfter("instagram.com/").replace("/", "")
}


const val USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"

val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
