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
import kotlin.time.Duration.Companion.seconds

class GetLatestYTMusicIDAPI(private val click: (String) -> Unit) {

    companion object {
        private val lastRunMap = mutableMapOf<String, Long>()
        private val COOLDOWN_MS = 3.seconds.inWholeMilliseconds

        suspend fun runOnce(name: String, block: suspend () -> Unit) {
            val now = System.currentTimeMillis()
            val lastRun = lastRunMap[name] ?: 0L

            if (now - lastRun >= COOLDOWN_MS) {
                block()
                lastRunMap[name] = now
            }
        }
    }

    private val ytMusicURL = "https://music.youtube.com/youtubei/v1/search?prettyPrint=false"
    private val ytBaseURL = "https://music.youtube.com"

    fun readLocalID() = CoroutineScope(Dispatchers.IO).launch {
        val playerInfo = musicPlayerDB.firstOrNull()
        runOnce(playerInfo?.data?.id ?: "") {
            val result = getSongDetailsList()
            val videoID = findMostSuitableVideoId(
                result, playerInfo?.data?.name ?: "", playerInfo?.data?.artists ?: "",
            )
            click(videoID ?: "")
        }
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

            val tabs = jsonObject
                .getJSONObject("contents")
                .getJSONObject("tabbedSearchResultsRenderer")
                .getJSONArray("tabs")

            val videos = mutableListOf<VideoInfo>()

            for (i in 0 until tabs.length()) {
                val tab = tabs.getJSONObject(i).optJSONObject("tabRenderer") ?: continue
                val sectionList =
                    tab.optJSONObject("content")?.optJSONObject("sectionListRenderer") ?: continue
                val contents = sectionList.optJSONArray("contents") ?: continue

                for (j in 0 until contents.length()) {
                    val content = contents.getJSONObject(j)

                    if (content.has("musicCardShelfRenderer")) {
                        val shelf = content.getJSONObject("musicCardShelfRenderer")

                        val runs = shelf.optJSONObject("title")?.optJSONArray("runs") ?: continue
                        val firstRun = runs.optJSONObject(0) ?: continue
                        val title = firstRun.optString("text", "")

                        val subtitleRuns =
                            shelf.optJSONObject("subtitle")?.optJSONArray("runs") ?: continue
                        var artists = ""
                        var views = ""

                        for (k in 0 until subtitleRuns.length()) {
                            val text = subtitleRuns.getJSONObject(k).optString("text", "")
                            if (text.contains("views")) {
                                views = text
                            } else if (text != " • " && text != "Video" && !text.matches(Regex("\\d+:\\d+"))) {
                                artists += text
                            }
                        }

                        val navEndpoint = firstRun.optJSONObject("navigationEndpoint")
                        val videoId =
                            navEndpoint?.optJSONObject("watchEndpoint")?.optString("videoId", null)

                        if (videoId != null) {
                            val viewCount = parseViewCount(views)
                            videos.add(VideoInfo(videoId, title, artists, viewCount))
                        }
                    }

                    if (content.has("musicShelfRenderer")) {
                        val items =
                            content.getJSONObject("musicShelfRenderer").optJSONArray("contents")
                                ?: continue
                        for (k in 0 until items.length()) {
                            val item = items.getJSONObject(k)
                                .optJSONObject("musicResponsiveListItemRenderer") ?: continue

                            val playlistData = item.optJSONObject("playlistItemData")
                            val videoId = playlistData?.optString("videoId", null) ?: continue

                            val flexColumns = item.optJSONArray("flexColumns") ?: continue

                            val title = flexColumns.optJSONObject(0)
                                ?.optJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                ?.optJSONObject("text")
                                ?.optJSONArray("runs")
                                ?.optJSONObject(0)
                                ?.optString("text", "") ?: ""

                            val subtitleRuns = flexColumns.optJSONObject(1)
                                ?.optJSONObject("musicResponsiveListItemFlexColumnRenderer")
                                ?.optJSONObject("text")
                                ?.optJSONArray("runs") ?: continue

                            var artists = ""
                            var views = ""

                            for (r in 0 until subtitleRuns.length()) {
                                val text = subtitleRuns.getJSONObject(r).optString("text", "")
                                if (text.contains("views")) {
                                    views = text
                                } else if (text != " • " && text != "Video" && !text.matches(Regex("\\d+:\\d+"))) {
                                    artists += text
                                }
                            }

                            if (views.isEmpty()) continue

                            val viewCount = parseViewCount(views)
                            videos.add(VideoInfo(videoId, title, artists, viewCount))
                        }
                    }
                }
            }
            val matchingVideos = filterVideos(videos, songName, artistName)
            val videoID = matchingVideos.maxByOrNull { it.viewCount }?.videoId
            return videoID
        } catch (e: Exception) {
            Log.e("JSONParser", "Error parsing JSON: ${e.message}")
            return null
        }
    }

    private fun filterVideos(
        videos: List<VideoInfo>, songName: String, artistStr: String
    ): List<VideoInfo> {
        val requiredArtists = parseArtists(artistStr)

        return videos.filter { video ->
            val titleLower = video.title.lowercase()

            val hasSong = titleLower.contains(songName.lowercase())

            val hasArtists = requiredArtists.all { artist ->
                titleLower.contains(artist)
            }

            hasSong && hasArtists
        }
    }


    private fun parseArtists(artistStr: String): List<String> {
        return artistStr
            .lowercase()
            .split("&", ",", " and ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun parseViewCount(viewString: String): Long {
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