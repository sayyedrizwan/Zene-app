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
        val SONG_SPEED_SETTINGS = intPreferencesKey("song_speed_settings")
        val LOOP_SETTINGS = booleanPreferencesKey("loop_settings")
        val AUTOPLAY_SETTINGS = booleanPreferencesKey("autoplay_settings")
        val DO_OFFLINE_DOWNLOAD_WIFI_SETTINGS = booleanPreferencesKey("do_offline_download_wifi_settings")
        val SHOW_PLAYING_SONG_ON_LOCK_SCREEN_SETTINGS = booleanPreferencesKey("show_pLaying_song_on_lock_screen_settings")
        val STAND_BY_MODE_SETTINGS = booleanPreferencesKey("stand_by_mode_settings")
        val SET_WALLPAPER_SETTINGS = intPreferencesKey("set_wallpaper_settings")
        val ALARM_TIME_SETTINGS = stringPreferencesKey("alarm_time_settings")
        val ALARM_SONG_SETTINGS = stringPreferencesKey("alarm_song_settings")
        val USER_AUTH_DATA = stringPreferencesKey("user_auth_data")
    }
}

const val TIME_ALARM = "00:00"

enum class OfflineSongsInfo(val v: Int) {
    LOCAL_SONGS(0), SUGGESTED_SONGS(1), OFFLINE_DOWNLOAD(2)
}

enum class SetWallpaperInfo(val v: Int) {
    SONG_THUMBNAIL(0), ARTIST_IMAGE(1), NONE(2)
}

enum class SongsQualityInfo(val v: Int) {
    HIGH_QUALITY(0), LOW_QUALITY(1), HIGH_QUALITY_WIFI(2)
}

enum class SongSpeed(val v: Int) {
    ZERO_TWO_FIVE(0), ZERO_FIVE(1), ZERO_SEVEN_FIVE(2), ONE(3),
    ONE_TWO_FIVE(4), ONE_FIVE(5), ONE_SEVEN_FIVE(6), TWO(7)
}