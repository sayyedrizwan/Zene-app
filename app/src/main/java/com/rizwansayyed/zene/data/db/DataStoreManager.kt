package com.rizwansayyed.zene.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.ARRAY_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.JSON_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_PLAYER
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.SEARCH_HISTORY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.USER_INFOS
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.APPLICATION_ID)

    object DataStoreManagerObjects {
        const val JSON_EMPTY = "{}"
        const val ARRAY_EMPTY = "[]"
        val USER_INFOS = stringPreferencesKey("user_info")
        val SEARCH_HISTORY = stringPreferencesKey("search_history")
        val MUSIC_PLAYER = stringPreferencesKey("music_player")
    }


    var userInfoDB
        get() = context.dataStore.data.map {
            moshi.adapter(UserInfoData::class.java).fromJson(it[USER_INFOS] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(UserInfoData::class.java).toJson(value.first())
            context.dataStore.edit { it[USER_INFOS] = json }
        }

    var searchHistoryDB
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java).fromJson(it[SEARCH_HISTORY] ?: ARRAY_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(Array<String>::class.java).toJson(value.first())
            context.dataStore.edit { it[SEARCH_HISTORY] = json }
        }

    var musicPlayerDB
        get() = context.dataStore.data.map {
            moshi.adapter(MusicPlayerData::class.java).fromJson(it[MUSIC_PLAYER] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(MusicPlayerData::class.java).toJson(value.first())
            context.dataStore.edit { it[MUSIC_PLAYER] = json }
        }

    var musicSettings
        get() = context.dataStore.data.map {
            moshi.adapter(MusicPlayerData::class.java).fromJson(it[MUSIC_PLAYER] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(MusicPlayerData::class.java).toJson(value.first())
            context.dataStore.edit { it[MUSIC_PLAYER] = json }
        }
}