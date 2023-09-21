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
    const val RADIO_BASE_URL = "https://at1.api.radio-browser.info/json/stations/"
    const val RADIO_SEARCH_API = "search"
}


object IpJsonAPI {
    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_API = "json"
}

object CacheFiles {
    val radioList by lazy { File(context.cacheDir, "radio-online.txt").apply { mkdirs() } }
}



val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
