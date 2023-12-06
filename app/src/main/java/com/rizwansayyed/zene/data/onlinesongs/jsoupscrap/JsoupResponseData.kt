package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.utils.USER_AGENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.concurrent.TimeUnit


val cookieRetry = HashMap<String, Int>()

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
            .addHeader("referer", getMainDomain(url) ?: "")
            .addHeader("user-agent", USER_AGENT)
            .addHeader("Cookie", DataStorageManager.cookiesData(getMainDomain(url) ?: ""))
            .build()

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val cookieList = response.headers("Set-Cookie")
                if (cookieList.size >= 2 && (cookieRetry[getMainDomain(url) ?: ""] ?: 0) <= 2) {
                    retryWithCookieResponseData(url, response.headers("Set-Cookie"))
                } else
                    response.body?.string()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

suspend fun retryWithCookieResponseData(url: String, headers: List<String>): String? {
    cookieRetry[getMainDomain(url) ?: ""] =
        (cookieRetry[getMainDomain(url) ?: ""] ?: 0) + 1

    var cookies = ""
    headers.forEach {
        cookies += "${it.substringBefore(" ")} "
    }
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
            .addHeader("Cookie", cookies)
            .build()
        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                cookieRetry[getMainDomain(url) ?: ""] = 0
                DataStorageManager.cookiesData(getMainDomain(url) ?: "", cookies)
                response.body?.string()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

suspend fun downloadAFileFromURL(url: String, file: File, percentage: (Int) -> Unit): Boolean? {
    return withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()

        fun updateProgress(bytesRead: Long, contentLength: Long) {
            val progress = (bytesRead.toDouble() / contentLength.toDouble()) * 100
            percentage(progress.toInt())
        }

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {

                val body = response.body!!
                val contentLength = body.contentLength()
                val fileOutputStream = FileOutputStream(file)

                val buffer = ByteArray(8192)
                var bytesRead = 0L

                while (bytesRead < contentLength) {
                    val read = body.byteStream().read(buffer)
                    if (read == -1) {
                        break
                    }

                    fileOutputStream.write(buffer, 0, read)
                    bytesRead += read.toLong()

                    updateProgress(bytesRead, contentLength)
                }

                fileOutputStream.flush()
                fileOutputStream.close()
                true
            } else
                false
        } catch (e: Exception) {
            false
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


fun getFavIcon(url: String): String {
    return "https://icon.horse/icon/${url.substringAfter("https://")}"
}