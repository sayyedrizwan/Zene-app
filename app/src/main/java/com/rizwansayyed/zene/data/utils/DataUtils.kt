package com.rizwansayyed.zene.data.utils

import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


const val PAGINATION_PAGE_SIZE = 50

object DBNAME {
    const val RECENT_PLAYED_DB = "recent_played_db"
    const val OFFLINE_DOWNLOADED_SONGS_DB = "offline_songs_db"
    const val SAVED_PLAYLIST_DB = "saved_playlist_db"
    const val PLAYLIST_SONGS_DB = "playlist_songs_db"
}

object VideoDownloaderAPI {
    const val SAVE_FROM_BASE_URL = "https://www.savefrom.live/"
    const val SAVE_FROM_VIDEO_API = "wp-json/aio-dl/video-data/"
}

object RadioOnlineAPI {
    const val RADIO_BASE_URL = "https://at1.api.radio-browser.info"

    fun radioSearchAPI(base: String): String {
        return "$base/json/stations/search"
    }

    fun radioUUIDSearchAPI(base: String): String {
        return "$base/json/stations/byuuid"
    }

    fun searchRadioNameAPI(base: String, name: String): String {
        return "$base/json/stations/byname/$name"
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

object PinterestAPI {
    const val PINTEREST_BASE_URL = "https://www.pinterest.com/resource/"
    const val PINTEREST_SEARCH_API = "BaseSearchResource/get/"

    const val PINTEREST_DATA_QUERY =
        """{"options":{"article":"","appliedProductFilters":"---","price_max":null,"price_min":null,"query":"from the vault - taylor swift","scope":"pins","auto_correction_disabled":"","top_pin_id":"","filters":""},"context":{}}"""

    fun pinterestSearch(s:String): String {
        return "?q=${s.replace(" ", "%20")}&rs=typed"
    }
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

object GoogleNewsAPI {
    const val GOOGLE_NEWS_BASE_URL = "https://news.google.com/"
    const val GOOGLE_NEWS_API = "rss/search"

}

object LastFM {
    const val LAST_FM_BASE_URL = "https://www.last.fm/"
    const val LFM_BASE_URL = "https://kerve.last.fm"
    const val LFM_TOP_LISTEN_SONGS = "kerve/charts"
    const val LFM_SEARCH_ARTISTS = "kerve/search"


    fun artistsWikiInfo(url: String): String {
        return "$url/+wiki"
    }

    fun artistsEventInfo(url: String): String {
        return "$url/+events"
    }

    fun artistsTopSongsInfo(url: String): String {
        return "$url/+tracks?date_preset=LAST_7_DAYS#top-tracks"
    }

    fun searchLastFMImageURLPath(url: String, id: String): String {
        return "$url/+images/$id/json"
    }
}


object SongDownloader {
    const val APPLICATION_X_FORM_URL_ENCODE = "application/x-www-form-urlencoded; charset=UTF-8"

    const val YT_CW_BASE_URL = "https://yt-cw.fabdl.com/youtube/get"
    const val KEEP_VID_BASE_URL = "https://ww12.keepvid.works/convert/"

    fun ytURL(id: String): String {
        return "https://www.youtube.com/watch?v=$id"
    }

    fun ytConvertor(id: String): String {
        return "https://yt.fabdl.com/youtube/mp3-convert-progress/$id"
    }

    fun keepVidButtonBaseURL(id: String): String {
        return "https://ww12.keepvid.works/button/?url=${ytURL(id)}"
    }
}

object SoundCloudAPI {
    const val SOUND_CLOUD_BASE_URL = "https://api-v2.soundcloud.com/"
    const val SOUND_CLOUD_SEARCH = "search"
    const val SOUND_WEB_PROFILE = "users/{username}/web-profiles"

}


object YoutubeAPI {
    private const val ytLatestClientVersion = "2.20231121.08.00"
    private const val ytMusicLatestClientVersion = "1.20231108.01.00"

    const val YT_BASE_URL = "https://www.youtube.com/youtubei/v1/"
    const val YT_MAIN_GUIDE = "guide"
    const val YT_SEARCH = "search"

    const val YT_MUSIC_BASE_URL = "https://music.youtube.com/youtubei/v1/"
    const val YT_SEARCH_API = "search"
    const val YT_NEXT_API = "next"
    const val YT_SUGGESTIONS_API = "music/get_search_suggestions"
    const val YT_BROWSE_API = "browse"
    const val YT_SEARCH_SUGGESTION_API = "music/get_search_suggestions"
    const val YT_PLAYER_API = "player"


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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "${
            q.replace(
                "\"",
                "\\\""
            )
        }", "params": "EgWKAQIIAWoSEAMQBRAEEAkQDhAKEBAQERAV"
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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMerchandiseInfoJsonBody(ip: IpJsonResponse?, id: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB",
                    "clientVersion": "$ytLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            },
            "videoId": "$id"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicMusicDetails(ip: IpJsonResponse?, id: String): RequestBody {
        val json = """{
            "videoId": "$id",
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}",
                    "clientScreen": "WATCH_FULL_SCREEN"
                }
            }
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicUpNextDetails(ip: IpJsonResponse?, id: String): RequestBody {
        val json = """{
            "videoId": "$id",
            "playlistId": "RDAMVM$$id",
            "params": "wAEB",
            "isAudioOnly": true,
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}",
                    "clientScreen": "WATCH_FULL_SCREEN"
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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "${
            q.replace(
                "\"",
                "\\\""
            )
        }", "params": "EgWKAQIIAWoSEAMQCRAOEAoQBRAEEBEQFRAQ"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytMusicAlbumsDetailsJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
                "context": {
                    "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB_REMIX",
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, "browseId": "$q"
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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, "query": "${
            q.replace(
                "\"",
                "\\\""
            )
        }", "params": "EgWKAQIgAWoQEAMQBBAJEAoQBRAREBAQFQ%3D%3D"
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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "platform": "DESKTOP",
                    "clientFormFactor": "UNKNOWN_FORM_FACTOR",
                    "timeZone": "${ip?.timezone}"
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
                    "clientVersion": "$ytMusicLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, 
            "query": "${q.replace("\"", "\\\"")}", 
            "params": "EgWKAQIYAWoSEAMQCRAOEAoQBBAFEBEQFRAQ"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytArtistsPlaylistJsonBody(ip: IpJsonResponse?, q: String): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB",
                    "clientVersion": "$ytLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, 
            "query": "${q.replace("\"", "\\\"")}", 
           "params": "EgYIBRABGAI%3D"
        }"""

        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }

    fun ytLatestMusicSearch(ip: IpJsonResponse?, q: String, doLatest: Boolean): RequestBody {
        val json = """{
            "context": {
                "client": {
                    "remoteHost": "${ip?.query}",
                    "userAgent": "$USER_AGENT",
                    "clientName": "WEB",
                    "clientVersion": "$ytLatestClientVersion",
                    "timeZone": "${ip?.timezone}"
                }
            }, 
            "query": "${q.replace("\"", "\\\"")}" 
            ${if (doLatest) ", \"params\": \"EgIIBQ%3D%3D\"" else ""}
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
                    "clientVersion": "$ytMusicLatestClientVersion",
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
                    "clientVersion": "$ytMusicLatestClientVersion",
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
                    "clientVersion": "$ytMusicLatestClientVersion",
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
                     "clientVersion": "$ytMusicLatestClientVersion",
                     "timeZone": "${ip?.timezone}"
                }
            },
            "browseId": "FEmusic_new_releases"
        }"""
        val mediaType = "application/json".toMediaTypeOrNull()
        return json.toRequestBody(mediaType)
    }
}

object CacheFiles {
    val radioList by lazy { File(context.cacheDir, "radio-online.json") }
    val favRadio by lazy { File(context.cacheDir, "fav-radio.json") }
    val topArtistsList by lazy { File(context.cacheDir, "top-artists-list.json") }
    val recentMostPlayedSongs by lazy { File(context.cacheDir, "recent-most-played-songs.json") }
    val topGlobalSongs by lazy { File(context.cacheDir, "top-global-songs-list.json") }
    val topCountrySongs by lazy { File(context.cacheDir, "top-country-songs-list.json") }
    val freshAddedSongs by lazy { File(context.cacheDir, "fresh-added-list.json") }
    val topArtistsCountry by lazy { File(context.cacheDir, "top-artists-country-list.json") }
    val songsForYouCache by lazy { File(context.cacheDir, "songs-for-you-cache.json") }
    val albumsForYouCache by lazy { File(context.cacheDir, "albums-for-you-cache.json") }
    val artistsFanWithSongsCache by lazy {
        File(context.cacheDir, "artists-fan-with-songs-cache.json")
    }
    val suggestionYouMayLikeCache by lazy {
        File(context.cacheDir, "suggestion-you-may-like-cache.json")
    }
}

object RentAdvisorSubtitles {
    const val RENT_ADVISER_BASE_URL = "https://www.rentanadviser.com/subtitles/"

    fun searchOnRentAdviser(q: String): String {
        return "${RENT_ADVISER_BASE_URL}subtitles4songs.aspx?q=$q"
    }
}

object GeniusURL {
    const val GENIUS_BASE_URL = "https://genius.com"

    fun geniusMusicSearch(q: String): String {
        return "${GENIUS_BASE_URL}/api/search/multi?q=${q.lowercase()}"
    }
}


object ScrapURL {
    const val TOP_ARTISTS = "https://www.billboard.com/charts/artist-100/"

    const val BING_SEARCH = "https://www.bing.com/videos/search?q="

    fun songKickArtistsSearch(artists: String): String {
        return "https://www.songkick.com/search?page=1&per_page=10&query=${
            artists.trim().lowercase().replace(" ", "+")
        }&type=artists"
    }

    fun songKickArtistsCalendar(path: String): String {
        return "https://www.songkick.com/${path}/calendar"
    }

    fun songKickArtistsCalendarInfo(path: String): String {
        return "https://www.songkick.com${path}"
    }
}


fun getInstagramUsername(i: String): String {
    return i.substringAfter("instagram.com/").replace("/", "")
}

const val USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36,gzip(gfe)"

val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
