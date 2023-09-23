package com.rizwansayyed.zene.data.db.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStorageUtil {

    const val DATA_STORE_DB = "zene"


    val IP_JSON = stringPreferencesKey("ip_json")
    val GLOBAL_SONG_IS_FULL = booleanPreferencesKey("global_song_is_full")

}