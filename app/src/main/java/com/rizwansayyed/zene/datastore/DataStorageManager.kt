package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.data.model.IPResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.DATA_STORE_KEY
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.EMPTY_JSON
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.IP_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.USER_INFO_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.VIDEO_QUALITY_DB
import com.rizwansayyed.zene.datastore.model.VideoQualityEnum
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

object DataStorageManager {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_KEY)

    object DataStorageKey {
        const val DATA_STORE_KEY = "zene_data_store"
        const val EMPTY_JSON = "{}"

        val USER_INFO_DB = stringPreferencesKey("user_info_db")
        val IP_DB = stringPreferencesKey("ip_db")
        val VIDEO_QUALITY_DB = stringPreferencesKey("video_quality_db")
    }

    var userInfo: Flow<UserInfoResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(UserInfoResponse::class.java).fromJson(it[USER_INFO_DB] ?: EMPTY_JSON)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(UserInfoResponse::class.java).toJson(value.first())
                it[USER_INFO_DB] = json
            }
            if (isActive) cancel()
        }

    var ipDB: Flow<IPResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(IPResponse::class.java).fromJson(it[IP_DB] ?: EMPTY_JSON)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(IPResponse::class.java).toJson(value.first())
                it[IP_DB] = json
            }
            if (isActive) cancel()
        }


    var videoQualityDB: Flow<VideoQualityEnum>
        get() = context.dataStore.data.map {
            val videoQuality =
                VideoQualityEnum.entries.firstOrNull { v -> v.name == it[VIDEO_QUALITY_DB] }
            videoQuality ?: VideoQualityEnum.`1440`
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[VIDEO_QUALITY_DB] = value.first().name
            }
            if (isActive) cancel()
        }
}