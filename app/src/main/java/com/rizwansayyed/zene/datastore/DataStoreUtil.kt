package com.rizwansayyed.zene.datastore

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_TIMESTAMP
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.IP_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_TIMESTAMP
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_OF_WEEK_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_TIMESTAMP
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_TIMESTAMP
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_GLOBAL_SONGS_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_GLOBAL_SONGS_TIMESTAMP

object DataStoreUtil {

    object DATA_STORE_KEY {
        const val DB_STORE = "datastore_zene"

        const val ALBUM_HEADER_DATA = "album_header_data"
        const val ALBUM_HEADER_TIMESTAMP = "album_header_timestamp"
        const val TOP_ARTISTS_OF_WEEK_DATA = "top_artists_of_week"
        const val TOP_ARTISTS_TIMESTAMP = "top_artists_timestamp"
        const val TOP_GLOBAL_SONGS_DATA = "top_global_songs_data"
        const val TOP_GLOBAL_SONGS_TIMESTAMP = "top_global_songs_timestamp"
        const val TOP_COUNTRY_SONGS_DATA = "top_country_songs_data"
        const val TOP_COUNTRY_SONGS_TIMESTAMP = "top_country_songs_timestamp"
        const val SONGS_SUGGESTIONS_DATA = "songs_suggestions_data"
        const val SONGS_SUGGESTIONS_TIMESTAMP = "songs_suggestions_timestamp"
        const val IP_DATA = "ip_data"
    }

    val album_header_data = stringPreferencesKey(ALBUM_HEADER_DATA)
    val album_header_timestamp = longPreferencesKey(ALBUM_HEADER_TIMESTAMP)

    val top_artists_of_week_data = stringPreferencesKey(TOP_ARTISTS_OF_WEEK_DATA)
    val top_artists_timestamp = longPreferencesKey(TOP_ARTISTS_TIMESTAMP)


    val top_global_songs_data = stringPreferencesKey(TOP_GLOBAL_SONGS_DATA)
    val top_global_songs_timestamp = longPreferencesKey(TOP_GLOBAL_SONGS_TIMESTAMP)


    val top_country_songs_data = stringPreferencesKey(TOP_COUNTRY_SONGS_DATA)
    val top_country_songs_timestamp = longPreferencesKey(TOP_COUNTRY_SONGS_TIMESTAMP)


    val songs_suggestions_data = stringPreferencesKey(SONGS_SUGGESTIONS_DATA)
    val songs_suggestions_timestamp = longPreferencesKey(SONGS_SUGGESTIONS_TIMESTAMP)


    val ip_data = stringPreferencesKey(IP_DATA)
}