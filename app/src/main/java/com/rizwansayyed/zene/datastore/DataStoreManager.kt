package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.DB_STORE
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.album_header_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.ip_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.songs_suggestions_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.songs_suggestions_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_artists_of_week_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_artists_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_country_songs_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_country_songs_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_global_songs_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.top_global_songs_timestamp
import com.rizwansayyed.zene.datastore.DataStoreUtil.trending_song_s_top_50_data
import com.rizwansayyed.zene.datastore.DataStoreUtil.trending_song_s_top_50_timestamp
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.IpJSONResponse
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


    var topCountrySongsData: Flow<Array<TopArtistsSongs>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<TopArtistsSongs>::class.java)
                .fromJson(it[top_country_songs_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(
                top_country_songs_data, Array<TopArtistsSongs>::class.java, value
            )
        }

    var topCountrySongsTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[top_country_songs_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(top_country_songs_timestamp, value)
        }


    var trendingSongsTop50Timestamp: Flow<Long>
        get() = context.dataStore.data.map {
            it[trending_song_s_top_50_timestamp] ?: getPastTimestamp()
        }
        set(value) {
            SetDataStoreValue(trending_song_s_top_50_timestamp, value)
        }


    var trendingSongsTop50Data: Flow<Array<TopArtistsSongs>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<TopArtistsSongs>::class.java)
                .fromJson(it[trending_song_s_top_50_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(
                trending_song_s_top_50_data, Array<TopArtistsSongs>::class.java, value
            )
        }


    var songsSuggestionsData: Flow<Array<TopArtistsSongs>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<TopArtistsSongs>::class.java)
                .fromJson(it[songs_suggestions_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(
                songs_suggestions_data, Array<TopArtistsSongs>::class.java, value
            )
        }

    var songsSuggestionsTimestamp: Flow<Long>
        get() = context.dataStore.data.map { it[songs_suggestions_timestamp] ?: getPastTimestamp() }
        set(value) {
            SetDataStoreValue(songs_suggestions_timestamp, value)
        }


    var ipData: Flow<IpJSONResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(IpJSONResponse::class.java).fromJson(it[ip_data] ?: "[]")
        }
        set(value) {
            SetDataStoreValueClass(ip_data, IpJSONResponse::class.java, value)
        }
}