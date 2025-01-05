package com.rizwansayyed.zene.data.cache

import android.content.Context
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.DATA_STORE_KEY
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.moshi

class CacheHelper {
    private val sharedPreferences =
        context.getSharedPreferences(DATA_STORE_KEY, Context.MODE_PRIVATE)

    fun saveData(key: String, data: Any) {
        val jsonAdapter = moshi.adapter(Any::class.java)
        val jsonData = jsonAdapter.toJson(data)
        val timestamp = System.currentTimeMillis()

        sharedPreferences.edit().apply {
            putString(key, jsonData)
            putLong("$key-timestamp", timestamp)
            apply()
        }
    }

    fun <T> getData(key: String, type: Class<T>): T? {
        val jsonData = sharedPreferences.getString(key, null)
        val timestamp = sharedPreferences.getLong("$key-timestamp", 0)

        if (jsonData != null && System.currentTimeMillis() - timestamp <= 10 * 60 * 1000) {
            val jsonAdapter = moshi.adapter(type)
            return jsonAdapter.fromJson(jsonData)
        }

        return null
    }
}