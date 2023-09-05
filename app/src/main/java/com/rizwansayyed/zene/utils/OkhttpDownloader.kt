package com.rizwansayyed.zene.utils

import com.rizwansayyed.zene.domain.model.YTMusicResponse
import com.rizwansayyed.zene.presenter.jsoup.model.YTSearchData
import com.rizwansayyed.zene.presenter.jsoup.model.YTSearchResponse
import com.rizwansayyed.zene.utils.Utils.URL.ytSearch
import com.rizwansayyed.zene.utils.Utils.USER_AGENT
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.saveCaptionsFileJson
import com.rizwansayyed.zene.utils.Utils.showToast
import com.squareup.moshi.adapter
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


fun getYtSearch(ip: String, q: String): YTSearchData? {
    val client = OkHttpClient().newBuilder()
        .build()
    val mediaType = "text/plain".toMediaTypeOrNull()

    val clientObject = JSONObject().apply {
        put("remoteHost", ip)
        put("userAgent", USER_AGENT)
        put("clientName", "WEB_REMIX")
        put("clientVersion", "1.20230828.00.00")
    }
    val contextObject = JSONObject().apply {
        put("client", clientObject)
    }
    val jsonObject = JSONObject().apply {
        put("context", contextObject)
        put("query", q)
        put("params", "EgWKAQIIAWoOEAMQBBAOEAoQBRAVEAk%3D")
    }
    val body: RequestBody = jsonObject.toString().toRequestBody(mediaType)

    val request = Request.Builder()
        .url(ytSearch).method("POST", body)
        .addHeader("authority", "music.youtube.com")
        .addHeader("x-origin", "https://music.youtube.com")
        .addHeader("Content-Type", "text/plain")
        .build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        try {
            val json = JSONObject(response.body?.string()!!).getJSONObject("contents")
                .getJSONObject("tabbedSearchResultsRenderer").getJSONArray("tabs").getJSONObject(0)
                .getJSONObject("tabRenderer").getJSONObject("content")
                .getJSONObject("sectionListRenderer").getJSONArray("contents")
                .getJSONObject(0).getJSONObject("musicShelfRenderer")

            saveCaptionsFileJson("test_cgec", json.toString())

            val search = moshi.adapter(YTSearchResponse::class.java).fromJson(json.toString())
            var thumbnail = ""
            var songName = ""
            var songMP3ID = ""
            val artistNames = ArrayList<String>(15)

            search?.contents?.forEach {
                val tSongName =
                    it?.musicResponsiveListItemRenderer?.flexColumns?.get(0)?.musicResponsiveListItemFlexColumnRenderer
                        ?.text?.runs?.get(0)?.text
                if (songName.isEmpty()) {
                    songName = tSongName ?: ""


                    thumbnail =
                        it!!.musicResponsiveListItemRenderer!!.thumbnail?.musicThumbnailRenderer
                            ?.thumbnail?.thumbnails?.get(0)?.url?.replace("w120", "h544")
                            ?.replace("w60", "h544") ?: ""

                    it.musicResponsiveListItemRenderer!!.flexColumns!!.forEach { name ->
                        name?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { artists ->
                            if (artists?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST") {
                                artistNames.add(artists.text ?: "")
                            }
                        }
                    }

                    songMP3ID = it.musicResponsiveListItemRenderer.playlistItemData?.videoId ?: ""
                }

                val detailsForEmptyStrings = search.contents[0]?.musicResponsiveListItemRenderer
                if (thumbnail == "") {
                    thumbnail = detailsForEmptyStrings?.thumbnail?.musicThumbnailRenderer
                        ?.thumbnail?.thumbnails?.get(0)?.url?.replace("w120", "h544")
                        ?.replace("w60", "h544") ?: ""
                }

                if (songName == "") {
                    songName =
                        detailsForEmptyStrings?.flexColumns?.get(0)
                            ?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.get(0)?.text
                            ?: ""
                }

                if (artistNames.size == 0) {
                    val a = detailsForEmptyStrings?.flexColumns?.get(1)
                        ?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.get(0)?.text
                        ?: ""
                    artistNames.add(a)
                }

                if (songMP3ID == "") {
                    songMP3ID = detailsForEmptyStrings?.playlistItemData?.videoId ?: ""
                }
            }

            return YTSearchData(thumbnail, songName, artistNames.joinToString(", "), songMP3ID)

        } catch (e: Exception) {
            e.message
        }
    }

    return null
}

fun getYoutubePlayUrl(id: String): String? {
    val client = OkHttpClient().newBuilder().build()

    val request = Request.Builder()
        .url("https://yt-cw.fabdl.com/youtube/get?url=https://www.youtube.com/watch?v=$id&mp3_task=2")
        .method("GET", null)
        .addHeader("authority", "yt-cw.fabdl.com")
        .addHeader("origin", "https://listentoyoutube.ch")
        .addHeader("referer", "https://listentoyoutube.ch/")
        .addHeader("user-agent", USER_AGENT)
        .build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return try {
            moshi.adapter(YTMusicResponse::class.java)
                .fromJson(response.body?.string()!!)?.result?.audios?.get(0)?.url
        } catch (e: Exception) {
            null
        }
    }

    return null
}

fun postOkHttps(url: String, json: String): String? {
    val cookies = OkhttpCookies()
    val client = OkHttpClient().newBuilder().cookieJar(cookies).build()
    val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
    val body: RequestBody = json.toRequestBody(mediaType)
    val request: Request = Request.Builder()
        .url(url).method("POST", body).addHeader("user-agent", USER_AGENT)
        .addHeader("Content-Type", "text/plain")
        .build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return response.body?.string()
    }

    return null
}

fun downloadHTMLOkhttp(url: String): String? {
    val cookies = OkhttpCookies()
    val client = OkHttpClient().newBuilder().cookieJar(cookies).build()
    val request = Request.Builder().url(url)
        .addHeader("User-Agent", USER_AGENT).method("GET", null).build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return response.body?.string()
    }
    return null
}

fun downloadHeaderOkhttp(url: String, pair: Pair<String, String>): String? {
    val cookies = OkhttpCookies()
    val client = OkHttpClient().newBuilder().cookieJar(cookies).build()
    val request = Request.Builder().url(url)
        .addHeader("User-Agent", USER_AGENT)
        .addHeader(pair.first, pair.second)
        .method("GET", null).build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return response.body?.string()
    }
    return null
}

class OkhttpCookies : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }
}