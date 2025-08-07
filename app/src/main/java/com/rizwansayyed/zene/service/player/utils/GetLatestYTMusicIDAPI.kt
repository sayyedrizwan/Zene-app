package com.rizwansayyed.zene.service.player.utils

import android.util.Log
import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class GetLatestYTMusicIDAPI(private val click: (String) -> Unit) {

    private val ytMusicURL = "https://music.youtube.com/youtubei/v1/search?prettyPrint=false"
    private val ytBaseURL = "https://music.youtube.com"

    fun readLocalID() = CoroutineScope(Dispatchers.IO).launch {
        val playerInfo = musicPlayerDB.firstOrNull()
        val result = getSongDetailsList()
        val videoID = findMostSuitableVideoId(
            result, playerInfo?.data?.name ?: "", playerInfo?.data?.artists ?: "",
        )

        click(videoID ?: "")
    }

    private suspend fun searchName(): String {
        val playerInfo = musicPlayerDB.firstOrNull()
        val a = playerInfo?.data?.artists?.substringBefore(", ")?.substringBefore(" and ")
        return "${playerInfo?.data?.name} - $a"
    }

    private suspend fun getSongDetailsList() = withContext(Dispatchers.IO) {
        val ip = ipDB.firstOrNull()
        val client = OkHttpClient()
        val jsonBody = JSONObject().apply {
            put("context", JSONObject().apply {
                put("client", JSONObject().apply {
                    put("hl", "en-GB")
                    put("gl", ip?.countryCode?.uppercase())
                    put("clientName", "WEB_REMIX")
                    put("clientVersion", "1.20250804.03.00")
                })
            })
            put("query", searchName())
        }

        val mediaType = "application/json".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder().url(ytMusicURL).post(requestBody).addHeader("accept", "*/*")
            .addHeader("content-type", "application/json").addHeader("origin", ytBaseURL)
            .addHeader("x-origin", ytBaseURL).addHeader("cookie", "GPS=1;").build()


        try {
            val response = client.newCall(request).execute()
            return@withContext response.body.string()
        } catch (_: Exception) {
            return@withContext ""
        }

    }

    data class VideoInfo(
        val videoId: String, val title: String, val artists: String, val viewCount: Long
    )

    fun findMostSuitableVideoId(jsonString: String, songName: String, artistName: String): String? {
        try {
            val jsonObject = JSONObject(jsonString)

            val tabs =
                jsonObject.getJSONObject("contents").getJSONObject("tabbedSearchResultsRenderer")
                    .getJSONArray("tabs")

            val videos = mutableListOf<VideoInfo>()

            for (i in 0 until tabs.length()) {
                val tab = tabs.getJSONObject(i).getJSONObject("tabRenderer")
                val sectionList = tab.getJSONObject("content").getJSONObject("sectionListRenderer")
                val contents = sectionList.getJSONArray("contents")

                for (j in 0 until contents.length()) {
                    val content = contents.getJSONObject(j)
                    if (content.has("musicCardShelfRenderer")) {
                        val shelf = content.getJSONObject("musicCardShelfRenderer")
                        val title =
                            shelf.getJSONObject("title").getJSONArray("runs").getJSONObject(0)
                                .getString("text")
                        val subtitleRuns = shelf.getJSONObject("subtitle").getJSONArray("runs")
                        var artists = ""
                        var views = ""

                        subtitleRuns.forEach { run ->
                            val text = run.getString("text")
                            if (text.contains("views")) {
                                views = text
                            } else if (text != " • " && text != "Video" && !text.matches(Regex("\\d+:\\d+"))) {
                                artists += text
                            }
                        }

                        val videoId =
                            shelf.getJSONObject("title").getJSONArray("runs").getJSONObject(0)
                                .getJSONObject("navigationEndpoint").getJSONObject("watchEndpoint")
                                .getString("videoId")

                        val viewCount = parseViewCount(views)

                        videos.add(VideoInfo(videoId, title, artists, viewCount))
                    }

                    if (content.has("musicShelfRenderer")) {
                        val items =
                            content.getJSONObject("musicShelfRenderer").getJSONArray("contents")
                        for (k in 0 until items.length()) {
                            val item = items.getJSONObject(k)
                                .getJSONObject("musicResponsiveListItemRenderer")

                            if (!item.has("playlistItemData")) {
                                continue
                            }

                            val title = item.getJSONArray("flexColumns").getJSONObject(0)
                                .getJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                .getJSONObject("text").getJSONArray("runs").getJSONObject(0)
                                .getString("text")
                            val subtitleRuns = item.getJSONArray("flexColumns").getJSONObject(1)
                                .getJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                .getJSONObject("text").getJSONArray("runs")
                            var artists = ""
                            var views = ""

                            subtitleRuns.forEach { run ->
                                val text = run.getString("text")
                                if (text.contains("views")) {
                                    views = text
                                } else if (text != " • " && text != "Video" && !text.matches(Regex("\\d+:\\d+"))) {
                                    artists += text
                                }
                            }

                            if (views.isEmpty()) {
                                continue
                            }

                            val videoId =
                                item.getJSONObject("playlistItemData").getString("videoId")
                            val viewCount = parseViewCount(views)

                            videos.add(VideoInfo(videoId, title, artists, viewCount))
                        }
                    }
                }
            }

            val matchingVideos = videos.filter { video ->
                video.title.equals(
                    songName,
                    ignoreCase = true
                ) && video.artists.contains(artistName, ignoreCase = true)
            }

            return matchingVideos.maxByOrNull { it.viewCount }?.videoId
        } catch (e: Exception) {
            Log.e("JSONParser", "Error parsing JSON: ${e.message}")
            return null
        }
    }

    fun parseViewCount(viewString: String): Long {
        return try {
            val numberPart = viewString.replace(" views", "").trim()
            when {
                numberPart.endsWith("M") -> {
                    (numberPart.removeSuffix("M").toFloat() * 1_000_000).toLong()
                }

                numberPart.endsWith("K") -> {
                    (numberPart.removeSuffix("K").toFloat() * 1_000).toLong()
                }

                else -> numberPart.toLongOrNull() ?: 0L
            }
        } catch (_: Exception) {
            0L
        }
    }

    inline fun JSONArray.forEach(action: (JSONObject) -> Unit) {
        for (i in 0 until length()) {
            action(getJSONObject(i))
        }
    }
}