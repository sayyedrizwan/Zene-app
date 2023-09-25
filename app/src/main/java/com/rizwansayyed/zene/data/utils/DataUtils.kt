package com.rizwansayyed.zene.data.utils

import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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

object SpotifyAPI {
    const val ACCOUNT_SPOTIFY_API = "https://accounts.spotify.com/api/token"
    const val ACCOUNT_SPOTIFY_C_ID = "07cca9af3ee4411baaf2355a8ea61d3f"
    const val ACCOUNT_SPOTIFY_C_SECRET = "120327dd093345f6aaab5fb943f1ceb1"


    const val SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1/"
    const val SPOTIFY_API_SEARCH = "search"
    const val SPOTIFY_API_PLAYLIST = "playlists"


    const val SPOTIFY_GLOBAL_SEARCH = "top+50+global"
    const val SPOTIFY_COUNTRY_SEARCH = "top+50+"
}

object YoutubeAPI {
    const val YT_BASE_URL = "https://www.youtube.com/youtubei/v1/"
    const val YT_MAIN_GUIDE = "guide"

    const val YT_MUSIC_BASE_URL = "https://music.youtube.com/youtubei/v1/"
    const val YT_SEARCH_API = "search"


    fun ytMusicJsonBody(ip: IpJsonResponse, q: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip.timezone}"
                }
            }, "query": "$q"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }


    fun ytJsonBody(ip: IpJsonResponse): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB",
                    "clientVersion": "2.20230921.04.01",
                    "timeZone": "${ip.timezone}"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }
}

object SearchEngine {
    fun searchEngineDataURL(name: String): String {
        val n = name.lowercase().replace(" ", "+")
        return "https://www.bing.com/search?q=$n+twitter+and+instagram+official+account"
    }
}

object CacheFiles {
    val updateTheDate by lazy { File(context.cacheDir, "texxxt.json").apply { mkdirs() } }
    val radioList by lazy { File(context.cacheDir, "radio-online.txt").apply { mkdirs() } }
    val topArtistsList by lazy { File(context.cacheDir, "top-artists-list.txt").apply { mkdirs() } }
    val topGlobalSongs by lazy {
        File(context.cacheDir, "top-global-songs-list.txt").apply { mkdirs() }
    }
    val topCountrySongs by lazy {
        File(context.cacheDir, "top-country-songs-list.txt").apply { mkdirs() }
    }
    val freshAddedSongs by lazy {
        File(context.cacheDir, "fresh-added-list.txt").apply { mkdirs() }
    }
}

object ScrapURL {
    const val TOP_ARTISTS = "https://www.billboard.com/charts/artist-100/"
}


fun getInstagramUsername(i: String): String {
    return i.substringAfter("instagram.com/").replace("/", "")
}

fun sortNameForSearch(q: String): String {

    var name = ""

   val n = q.lowercase().replace("(official video)", "")
        .replace("(official audio)", "")
        .replace("full audio", "")
        .replace("(audio)", "")

    if (n.contains(" | ")) {
        if (n.split(" | ").size > 2) {
            n.split(" | ").forEachIndexed { index, s ->
                if (index == 0) name = s
                if (index == 1) name += " - $s"
            }
        } else {
            name = n.substringBefore(" | ")
        }
        return name
    }

    return n
}

const val USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36"

val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
