package com.rizwansayyed.zene.data.api

import com.rizwansayyed.zene.utils.Utils.RoomDB.fileCachedSongsDir
import com.rizwansayyed.zene.utils.Utils.URLS.USER_AGENT_D
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_MUSIC
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_MUSIC_SEARCH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object APIHttpService {

    suspend fun youtubeMusicPlaylist(): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.MINUTES).build()

        val request = Request.Builder().url(YOUTUBE_MUSIC).addHeader("Cookie", "GPS=1")
            .addHeader("User-Agent", USER_AGENT_D).build()

        try {
            val response = client.newCall(request).execute()
            val data = response.body?.string()
            val id = data?.substringAfterLast("RELEASED\"},")?.substringAfter("/playlist?list=")
                ?.substringBefore("\\u0026")

            return@withContext if ((id?.trim()?.length ?: 100) < 55) id?.trim() ?: "" else ""
        } catch (e: Exception) {
            return@withContext ""
        }
    }


    suspend fun youtubeSearchVideoRegion(name: String, artists: String) =
        withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.MINUTES).build()

            val clientObject = JSONObject().apply {
                put("clientName", "WEB_REMIX")
                put("clientVersion", "1.20241030.00.00")
            }
            val contextObject = JSONObject().apply {
                put("client", clientObject)
            }
            val mainObject = JSONObject().apply {
                put("context", contextObject)
                put("query", "$name - $artists")
                put("params", "EgWKAQIIAWoQEAMQBBAJEAoQBRAREBAQFQ%3D%3D")
            }

            val body = mainObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val request =
                Request.Builder().url(YOUTUBE_MUSIC_SEARCH).post(body).addHeader("Cookie", "GPS=1")
                    .addHeader("User-Agent", USER_AGENT_D).build()

            try {
                var songID: String? = null
                val response = client.newCall(request).execute()
                val data = response.body?.string()

                val objects = JSONObject(data!!).getJSONObject("contents")
                    .getJSONObject("tabbedSearchResultsRenderer").getJSONArray("tabs")
                    .getJSONObject(0).getJSONObject("tabRenderer").getJSONObject("content")
                    .getJSONObject("sectionListRenderer").getJSONArray("contents").getJSONObject(0)
                    .getJSONObject("musicShelfRenderer").getJSONArray("contents")

                for (i in 0 until objects.length()) {
                    val item =
                        objects.getJSONObject(i).optJSONObject("musicResponsiveListItemRenderer")
                    val label = item?.optJSONObject("overlay")
                        ?.optJSONObject("musicItemThumbnailOverlayRenderer")
                        ?.optJSONObject("content")?.optJSONObject("musicPlayButtonRenderer")
                        ?.optJSONObject("accessibilityPlayData")?.optJSONObject("accessibilityData")
                        ?.optString("label")

                    if (label?.lowercase()
                            ?.contains(name.lowercase()) == true && songID == null
                    ) songID = item.optJSONObject("playlistItemData")?.optString("videoId")
                }
                return@withContext songID ?: ""
            } catch (e: Exception) {
                return@withContext ""
            }
        }


    suspend fun downloadAFile(url: String, name: String, close: (Boolean) -> Unit) =
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.MINUTES).build()

            try {
                val response = client.newCall(request).execute()
                response.body?.let { responseBody ->
                    saveToFile(responseBody, "${name}.cache", close)
                }
            } catch (e: Exception) {
                close(false)
            }
        }

    private fun saveToFile(body: ResponseBody, fileName: String, close: (Boolean) -> Unit) {
        try {
            val file = File(fileCachedSongsDir, fileName)
            val outputStream = FileOutputStream(file)
            outputStream.use { output ->
                body.byteStream().use { input ->
                    input.copyTo(output)
                }
            }
            close(true)
        } catch (e: Exception) {
            close(false)
        }
    }
}