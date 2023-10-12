package com.rizwansayyed.zene.data.db.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStorageUtil {

    const val DATA_STORE_DB = "zene"


    val IP_JSON = stringPreferencesKey("ip_json")
    val GLOBAL_SONG_IS_FULL = booleanPreferencesKey("global_song_is_full")
    val SELECTED_FAVOURITE_ARTISTS_SONGS = stringPreferencesKey("selected_favourite_artists_songs")
    val DO_SHOW_SPLASH_SCREEN = booleanPreferencesKey("do_show_splash_screen")
    val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")

}