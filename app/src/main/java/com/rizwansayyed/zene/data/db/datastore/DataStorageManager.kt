package com.rizwansayyed.zene.data.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DATA_STORE_DB
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DO_SHOW_SPLASH_SCREEN
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.FAVOURITE_RADIO_LIST
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.IP_JSON
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.LAST_SYNC_TIME
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.MUSIC_PLAYER_DATA
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.PINNED_ARTISTS_LIST
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.SEARCH_HISTORY_LIST
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.SELECTED_FAVOURITE_ARTISTS_SONGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.cookiesName
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.PinnedArtistsData
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

object DataStorageManager {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_DB)
    private const val ARRAY_LIST = "[]"
    const val JSON_LIST = "{}"

    var userIpDetails: Flow<IpJsonResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(IpJsonResponse::class.java).fromJson(it[IP_JSON] ?: JSON_LIST)
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(IpJsonResponse::class.java).toJson(v.first())
            context.dataStore.edit { it[IP_JSON] = moshi }
        }


    var selectedFavouriteArtistsSongs: Flow<Array<String>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java)
                .fromJson(it[SELECTED_FAVOURITE_ARTISTS_SONGS] ?: ARRAY_LIST)
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(Array<String>::class.java).toJson(v.first())
            context.dataStore.edit { it[SELECTED_FAVOURITE_ARTISTS_SONGS] = moshi }
        }

    var favouriteRadioList: Flow<Array<String>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java)
                .fromJson(it[FAVOURITE_RADIO_LIST] ?: ARRAY_LIST)
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(Array<String>::class.java).toJson(v.first())
            context.dataStore.edit { it[FAVOURITE_RADIO_LIST] = moshi }
        }

    var searchHistoryList: Flow<Array<String>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java).fromJson(it[SEARCH_HISTORY_LIST] ?: ARRAY_LIST)
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(Array<String>::class.java).toJson(v.first())
            context.dataStore.edit { it[SEARCH_HISTORY_LIST] = moshi }
        }


    var musicPlayerData: Flow<MusicPlayerData?>
        get() = context.dataStore.data.map {
            moshi.adapter(MusicPlayerData::class.java).fromJson(it[MUSIC_PLAYER_DATA] ?: JSON_LIST)
        }
        set(v) = synchronized(this) {
            runBlocking {
                val moshi = moshi.adapter(MusicPlayerData::class.java).toJson(v.first())
                context.dataStore.edit { it[MUSIC_PLAYER_DATA] = moshi }
            }
        }


    var doShowSplashScreen: Flow<Boolean>
        get() = context.dataStore.data.map { it[DO_SHOW_SPLASH_SCREEN] ?: true }
        set(v) = runBlocking {
            context.dataStore.edit { it[DO_SHOW_SPLASH_SCREEN] = v.first() }
        }


    var lastAPISyncTime: Flow<Long>
        get() = context.dataStore.data.map { it[LAST_SYNC_TIME] ?: daysOldTimestamp() }
        set(v) = runBlocking {
            context.dataStore.edit { it[LAST_SYNC_TIME] = v.first() }
        }

    var pinnedArtists: Flow<Array<PinnedArtistsData>>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<PinnedArtistsData>::class.java)
                .fromJson(it[PINNED_ARTISTS_LIST] ?: ARRAY_LIST) ?: emptyArray()
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(Array<PinnedArtistsData>::class.java).toJson(v.first())
            context.dataStore.edit { it[PINNED_ARTISTS_LIST] = moshi }
        }


    suspend fun cookiesData(domain: String): String {
        return context.dataStore.data.map { it[cookiesName(domain)] ?: "" }.first()
    }

    fun cookiesData(domain: String, v: String) = runBlocking {
        context.dataStore.edit { it[cookiesName(domain)] = v }
    }
}