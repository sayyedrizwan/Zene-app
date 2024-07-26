package com.rizwansayyed.zene.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.ARRAY_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.JSON_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.LAST_ADS_TIMESTAMP
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_AUTOPLAY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_LOOP
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_PLAYER
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_SPEED
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.PINNED_ARTISTS_LIST
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.SEARCH_HISTORY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.USER_INFOS
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
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
        const val TS_LAST_DATA = 1721759124000
        val USER_INFOS = stringPreferencesKey("user_info")
        val SEARCH_HISTORY = stringPreferencesKey("search_history")
        val MUSIC_PLAYER = stringPreferencesKey("music_player")
        val MUSIC_SPEED = stringPreferencesKey("music_speed")
        val MUSIC_LOOP = booleanPreferencesKey("music_loop")
        val MUSIC_AUTOPLAY = booleanPreferencesKey("music_autoplay")
        val PINNED_ARTISTS_LIST = stringPreferencesKey("pinned_artists_list")
        val LAST_ADS_TIMESTAMP = longPreferencesKey("last_ads_timestamp")
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

    var musicSpeedSettings
        get() = context.dataStore.data.map {
            MusicSpeed.valueOf(it[MUSIC_SPEED] ?: MusicSpeed.`1`.name)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[MUSIC_SPEED] = value.first().name }
        }

    var musicLoopSettings
        get() = context.dataStore.data.map { it[MUSIC_LOOP] ?: false }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[MUSIC_LOOP] = value.first() }
        }


    var musicAutoplaySettings
        get() = context.dataStore.data.map { it[MUSIC_AUTOPLAY] ?: true }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[MUSIC_AUTOPLAY] = value.first() }
        }

    var pinnedArtistsList
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java)
                .fromJson(it[PINNED_ARTISTS_LIST] ?: ARRAY_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(Array<String>::class.java).toJson(value.first())
            context.dataStore.edit { it[PINNED_ARTISTS_LIST] = json }
        }

    var lastAdsTimestamp
        get() = context.dataStore.data.map {
            it[LAST_ADS_TIMESTAMP] ?: TS_LAST_DATA
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[LAST_ADS_TIMESTAMP] = value.first() }
        }
}