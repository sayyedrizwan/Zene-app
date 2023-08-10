package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.DB_STORE
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_artists_of_week_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_artists_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_global_songs_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_global_songs_timestamp
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.DateTime.getPastTimestamp
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DB_STORE)
    }


    var albumHeaderData: Flow<AlbumsHeadersResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(AlbumsHeadersResponse::class.java).fromJson(it[album_header_data] ?: "{}")
        }
        set(value) {
            SetDataStoreValueClass(album_header_data, AlbumsHeadersResponse::class.java, value)
        }

    var albumHeaderTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[album_header_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(album_header_timestamp, value)
        }


    var topArtistsOfWeekData: Flow<Array<TopArtistsSongs>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<TopArtistsSongs>::class.java)
                .fromJson(it[top_artists_of_week_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(
                top_artists_of_week_data, Array<TopArtistsSongs>::class.java, value
            )
        }

    var topArtistsOfWeekTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[top_artists_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(top_artists_timestamp, value)
        }


    var topGlobalSongsData: Flow<Array<TopArtistsSongs>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<TopArtistsSongs>::class.java)
                .fromJson(it[top_global_songs_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(
                top_global_songs_data, Array<TopArtistsSongs>::class.java, value
            )
        }

    var topGlobalSongsTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[top_global_songs_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(top_global_songs_timestamp, value)
        }
}