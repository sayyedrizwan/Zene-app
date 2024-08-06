package com.rizwansayyed.zene.data.api

import android.util.Log
import com.rizwansayyed.zene.utils.Utils.URLS.USER_AGENT_D
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_MUSIC
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object APIHttpService {

    fun youtubeMusicPlaylist(): String = runBlocking(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.MINUTES)
            .build()

        val request = Request.Builder().url(YOUTUBE_MUSIC)
            .addHeader("Cookie", "GPS=1").addHeader("User-Agent", USER_AGENT_D)
            .build()

        try {
            val response = client.newCall(request).execute()
            val data = response.body?.string()
            val id = data?.substringAfterLast("RELEASED\"},")
                ?.substringAfter("/playlist?list=")?.substringBefore("\\u0026")

            return@runBlocking if ((id?.trim()?.length ?: 100) < 55) id?.trim() ?: "" else ""
        } catch (e: Exception) {
            return@runBlocking ""
        }
    }
}