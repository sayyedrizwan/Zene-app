package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap

import com.rizwansayyed.zene.data.utils.USER_AGENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL
import java.util.concurrent.TimeUnit


suspend fun jsoupResponseData(url: String): String? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .addHeader("authority", getMainDomain(url) ?: "")
            .addHeader("Referer", getMainDomain(url) ?: "")
            .addHeader("user-agent", USER_AGENT)
            .build()

        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            return@withContext response.body?.string()
        } else {
           return@withContext null
        }
    }
}


fun getMainDomain(url: String): String? {
    return try {
        val parsedUrl = URL(url)
        parsedUrl.protocol + "://" + parsedUrl.host + "/"
    } catch (e: Exception) {
        null
    }
}