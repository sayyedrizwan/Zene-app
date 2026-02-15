package com.rizwansayyed.zene.data.cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import java.io.File
import kotlin.time.Duration.Companion.minutes

class CacheHelper {
    val gson = Gson()

    data class CacheWrapper<T>(
        val data: T, val timestamp: Long
    )

    val expiryTime = 7.minutes.inWholeMilliseconds

    val cacheFilesDir = File(context.cacheDir, "api-cache-files").apply {
        mkdirs()
    }

    fun clear(fileName: String) {
        File(cacheFilesDir, fileName.replace("/", "-")).deleteRecursively()
    }

    inline fun <reified T> save(fileName: String, data: T) {
        try {
            val wrapper = CacheWrapper(data, System.currentTimeMillis())
            val json = gson.toJson(wrapper)
            File(cacheFilesDir, fileName.replace("/", "-")).writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inline fun <reified T> get(fileName: String): T? {
        try {
            val file = File(cacheFilesDir, fileName.replace("/", "-"))
            if (!file.exists()) return null

            val json = file.readText()
            val wrapperType = object : TypeToken<CacheWrapper<T>>() {}.type
            val wrapper: CacheWrapper<T> = gson.fromJson(json, wrapperType)

            return if (System.currentTimeMillis() - wrapper.timestamp < expiryTime) {
                wrapper.data
            } else {
                file.delete()
                null
            }
        } catch (_: Exception) {
            return null
        }
    }
}