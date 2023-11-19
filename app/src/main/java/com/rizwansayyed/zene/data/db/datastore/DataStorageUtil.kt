package com.rizwansayyed.zene.data.db.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStorageUtil {

    const val DATA_STORE_DB = "zene"

    val IP_JSON = stringPreferencesKey("ip_json")
    val SELECTED_FAVOURITE_ARTISTS_SONGS = stringPreferencesKey("selected_favourite_artists_songs")
    val SEARCH_HISTORY_LIST = stringPreferencesKey("search_history_list")
    val DO_SHOW_SPLASH_SCREEN = booleanPreferencesKey("do_show_splash_screen")
    val LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
    val FAVOURITE_RADIO_LIST = stringPreferencesKey("favourite_radio_list")
    val MUSIC_PLAYER_DATA = stringPreferencesKey("music_player_data")

    fun cookiesName(domain: String): Preferences.Key<String> {
        return stringPreferencesKey("${domain}_cookie")
    }


    object DataStorageSettings {
        val OFFLINE_SONGS_SETTINGS = intPreferencesKey("offline_songs_settings")
        val SONGS_QUALITY_SETTINGS = intPreferencesKey("songs_quality_settings")
        val SEEK_BUTTON_SETTINGS = intPreferencesKey("seek_button_settings")
        val SONG_SPEED_SETTINGS = intPreferencesKey("song_speed_settings")
    }
}

enum class OfflineSongsInfo(val v: Int) {
    LOCAL_SONGS(0), SUGGESTED_SONGS(1), OFFLINE_DOWNLOAD(2)
}

enum class SongsQualityInfo(val v: Int) {
    HIGH_QUALITY(0), LOW_QUALITY(1), HIGH_QUALITY_WIFI(2)
}

enum class SeekButton(val v: Int) {
    HIDE(0), FIVE(1), TEN(2), FIFTEEN(3)
}

enum class SongSpeed(val v: Int) {
    ZERO_TWO_FIVE(0), ZERO_FIVE(0), ZERO_SEVEN_FIVE(1), ONE(2),
    ONE_TWO_FIVE(3), ONE_FIVE(3), ONE_SEVEN_FIVE(3), TWO(3)
}