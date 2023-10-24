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
    const val IP_AWS_BASE_URL = "https://checkip.amazonaws.com/"

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

object LastFM {
    const val LFM_BASE_URL = "https://kerve.last.fm"
    const val LFM_TOP_LISTEN_SONGS = "kerve/charts"
    const val LFM_SEARCH_ARTISTS = "kerve/search"


    fun searchLastFMImageURLPath(id: String): String {
        return "https://www.last.fm/music/Taylor+Swift/+images/$id/json"
    }
}

object YoutubeAPI {
    const val YT_BASE_URL = "https://www.youtube.com/youtubei/v1/"
    const val YT_MAIN_GUIDE = "guide"

    const val YT_MUSIC_BASE_URL = "https://music.youtube.com/youtubei/v1/"
    const val YT_SEARCH_API = "search"
    const val YT_NEXT_API = "next"
    const val YT_SUGGESTIONS_API = "music/get_search_suggestions"
    const val YT_BROWSE_API = "browse"
    const val YT_SEARCH_SUGGESTION_API = "music/get_search_suggestions"

    fun generateYTMusicPlaylistURL(pid: String): String {
        return "https://music.youtube.com/playlist?list=$pid"
    }

    fun ytMusicMainSearchJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "$q", "params": "EgWKAQIIAWoSEAMQBRAEEAkQDhAKEBAQERAV"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicSearchSuggestionJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
            "input": "$q",
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicSearchAllSongsJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
                "context": {
                    "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "$q", "params": "EgWKAQIIAWoSEAMQCRAOEAoQBRAEEBEQFRAQ"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }


    fun ytMusicArtistsSearchJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "$q", "params": "EgWKAQIgAWoQEAMQBBAJEAoQBRAREBAQFQ%3D%3D"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicNextSearchJsonBody(ip: IpJsonResponse?): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "platform": "DESKTOP",
                    "clientFormFactor": "UNKNOWN_FORM_FACTOR",
                    "timeZone": "${ip?.timezone}",
                    "screenWidthPoints": 705,
                    "screenHeightPoints": 825
                },
                "adSignalsInfo": {
                    "params": []
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicArtistsAlbumsJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20231016.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }, 
            "query": "$q", 
            "params": "EgWKAQIYAWoSEAMQCRAOEAoQBBAFEBEQFRAQ"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicNextJsonBody(ip: IpJsonResponse?, pId: String): RequestBody {
        val json = """{
            "videoId": "$pId",
            "isAudioOnly": true,
            "responsiveSignals": {
                "videoInteraction": []
            },
            "queueContextParams": "",
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "originalUrl": "https://music.youtube.com/watch?v=$pId",
                    "timeZone": "${ip?.timezone}"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }


    fun ytMusicUpNextJsonBody(ip: IpJsonResponse?, pId: String): RequestBody {
        val json = """{
            "playlistId": "RDAMVM$pId",
            "isAudioOnly": true,
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20231016.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicBrowseSuggestJsonBody(ip: IpJsonResponse?, bId: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "1.20230918.01.00",
                    "timeZone": "${ip?.timezone}"
                }
            },
            "browseId": "$bId"
        }"""
        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicNewReleaseJsonBody(ip: IpJsonResponse?): RequestBody {
        val json = """{
            "context": {
                "client": {
                     "remoteHost": "${ip?.query}",
                     "userAgent": "$USER_AGENT", 
                     "clientName": "WEB_REMIX",
                     "clientVersion": "1.20231009.01.00",
                     "timeZone": "${ip?.timezone}"
                }
            },
            "browseId": "FEmusic_new_releases"
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
    val radioList by lazy { File(context.cacheDir, "radio-online.json") }
    val topArtistsList by lazy { File(context.cacheDir, "top-artists-list.json") }
    val recentMostPlayedSongs by lazy { File(context.cacheDir, "recent-most-played-songs.json") }
    val topGlobalSongs by lazy { File(context.cacheDir, "top-global-songs-list.json") }
    val topCountrySongs by lazy { File(context.cacheDir, "top-country-songs-list.json") }
    val freshAddedSongs by lazy { File(context.cacheDir, "fresh-added-list.json") }
    val topArtistsCountry by lazy { File(context.cacheDir, "top-artists-country-list.json") }
    val songsForYouCache by lazy { File(context.cacheDir, "songs-for-you-cache.json") }
    val albumsForYouCache by lazy { File(context.cacheDir, "albums-for-you-cache.json") }
    val artistsFanWithSongsCache by lazy {
        File(
            context.cacheDir,
            "artists-fan-with-songs-cache.json"
        )
    }
    val suggestionYouMayLikeCache by lazy {
        File(context.cacheDir, "suggestion-you-may-like-cache.json")
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
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36,gzip(gfe)"

val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
