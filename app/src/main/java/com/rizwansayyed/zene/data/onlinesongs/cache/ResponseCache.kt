package com.rizwansayyed.zene.data.onlinesongs.cache

import androidx.core.net.toUri
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.toCache
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader


fun readTxtFileFromCache(file: File): String {
    return try {
        val inputStreamReader =
            InputStreamReader(context.contentResolver.openInputStream(file.toUri()))
        val bufferedReader = BufferedReader(inputStreamReader)
        val sb = StringBuilder()
        var s: String?
        while (bufferedReader.readLine().also { s = it } != null) {
            sb.append(s)
        }
        sb.toString()
    } catch (e: Exception) {
        ""
    }
}

fun writeToCacheFile(file: File, res: OnlineRadioCacheResponse) {
    try {
        val stream = FileOutputStream(file)
        val responseToString = moshi.adapter(OnlineRadioCacheResponse::class.java).toJson(res)
        stream.use { s ->
            s.write(responseToString.toByteArray())
        }
    } catch (e: Exception) {
        e.message
    }
}


fun <T> responseCache(file: File, adapter: Class<T>): T? {
    if (!file.exists()) return null

    val txt = readTxtFileFromCache(file)

    if (txt.isNotEmpty()) return null

    return try {
        moshi.adapter(adapter).fromJson(txt)
    } catch (e: Exception) {
        null
    }
}


fun returnFromCache(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    if (min > 8) {
        return false
    }

    return true
}


