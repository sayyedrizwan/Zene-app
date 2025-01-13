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

    val expiryTime = 25.minutes.inWholeMilliseconds

    val cacheFilesDir = File(context.filesDir, "api-cache-files").apply {
        mkdirs()
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
        } catch (e: Exception) {
            return null
        }
    }
}

//class CacheHelper {
//    private val sharedPreferences =
//        context.getSharedPreferences(DATA_STORE_KEY, Context.MODE_PRIVATE)
//
//    fun saveData(key: String, data: Any) {
//        val jsonAdapter = moshi.adapter(Any::class.java)
//        val jsonData = jsonAdapter.toJson(data)
//        val timestamp = System.currentTimeMillis()
//
//        sharedPreferences.edit().apply {
//            putString(key, jsonData)
//            putLong("$key-timestamp", timestamp)
//            apply()
//        }
//    }
//
//    fun <T> getData(key: String, type: Class<T>): T? {
//        val jsonData = sharedPreferences.getString(key, null)
//        val timestamp = sharedPreferences.getLong("$key-timestamp", 0)
//
//        if (jsonData != null && System.currentTimeMillis() - timestamp <= 10 * 60 * 1000) {
//            val jsonAdapter = moshi.adapter(type)
//            return jsonAdapter.fromJson(jsonData)
//        }
//
//        return null
//    }
//}