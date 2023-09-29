package com.rizwansayyed.zene.data.db.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DATA_STORE_DB
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.GLOBAL_SONG_IS_FULL
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.IP_JSON
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.SELECTED_FAVOURITE_ARTISTS_SONGS
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.IpJsonResponse
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

object DataStorageManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATA_STORE_DB)

    var userIpDetails: Flow<IpJsonResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(IpJsonResponse::class.java).fromJson(it[IP_JSON] ?: "{}")
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(IpJsonResponse::class.java).toJson(v.first())
            context.dataStore.edit { it[IP_JSON] = moshi }
            if (isActive) cancel()
        }


    var selectedFavouriteArtistsSongs: Flow<Array<String>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java).fromJson(it[SELECTED_FAVOURITE_ARTISTS_SONGS] ?: "[]")
        }
        set(v) = runBlocking {
            val moshi = moshi.adapter(Array<String>::class.java).toJson(v.first())
            context.dataStore.edit { it[SELECTED_FAVOURITE_ARTISTS_SONGS] = moshi }
            if (isActive) cancel()
        }


    var globalSongIsFull: Flow<Boolean>
        get() = context.dataStore.data.map { it[GLOBAL_SONG_IS_FULL] ?: false }
        set(v) = runBlocking {
            context.dataStore.edit { it[GLOBAL_SONG_IS_FULL] = v.first() }
            if (isActive) cancel()
        }
}