package com.rizwansayyed.zene.datastore

import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_DATA
import com.rizwansayyed.zene.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_TIMESTAMP

object DataStoreUtil {

    object DATA_STORE_KEY {
        const val DB_STORE = "datastore_zene"

        const val ALBUM_HEADER_DATA = "album_header_data"
        const val ALBUM_HEADER_TIMESTAMP = "album_header_timestamp"
    }

    val album_header_data = stringPreferencesKey(ALBUM_HEADER_DATA)
    val album_header_timestamp = longPreferencesKey(ALBUM_HEADER_TIMESTAMP)



}