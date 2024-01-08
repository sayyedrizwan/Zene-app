package com.rizwansayyed.zene.data.onlinesongs.cache

import androidx.core.net.toUri
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.printStack
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader


fun readTxtFileFromCache(file: File): String {
    return try {
        file.inputStream().bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        ""
    }
}

fun writeToCacheFile(file: File, res: String) {
    file.deleteRecursively()
    try {
        val stream = FileOutputStream(file)
        stream.use { s ->
            s.write(res.toByteArray())
        }
    } catch (e: Exception) {
        e.printStack()
    }
}


fun <T> responseCache(file: File, adapter: Class<T>): T? {
    if (!file.exists()) return null

    val txt = readTxtFileFromCache(file)
    if (txt.isEmpty()) return null

    return try {
        moshi.adapter(adapter).fromJson(txt)
    } catch (e: Exception) {
        null
    }
}


fun returnFromCache1Hour(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    return min <= 60
}


fun returnFromCache1Days(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    return min <= 1140
}

fun returnFromCache2Days(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    return min <= 2880
}


fun returnFromCache2Hours(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    return min <= 120
}

fun returnFromCache8Hours(cacheTs: Long): Boolean {
    val min = (System.currentTimeMillis() - cacheTs) / (1000 * 60)
    return min <= 480
}


