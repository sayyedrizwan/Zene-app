package com.rizwansayyed.zene.data.db.datastore

import androidx.datastore.preferences.core.edit
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.dataStore
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.AUTOPLAY_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.DO_OFFLINE_DOWNLOAD_WIFI_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.LOOP_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.OFFLINE_SONGS_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.PAUSE_MUSIC_ON_HEADPHONE_DETACH_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.SEEK_BUTTON_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.SET_WALLPAPER_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.SHOW_PLAYING_SONG_ON_LOCK_SCREEN_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.SONGS_QUALITY_SETTINGS
import com.rizwansayyed.zene.data.db.datastore.DataStorageUtil.DataStorageSettings.SONG_SPEED_SETTINGS
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStorageSettingsManager {

    var offlineSongsSettings: Flow<Int>
        get() = context.dataStore.data.map {
            it[OFFLINE_SONGS_SETTINGS] ?: OfflineSongsInfo.LOCAL_SONGS.v
        }
        set(v) = runBlocking {
            context.dataStore.edit { it[OFFLINE_SONGS_SETTINGS] = v.first() }
        }

    var songQualitySettings: Flow<Int>
        get() = context.dataStore.data.map {
            it[SONGS_QUALITY_SETTINGS] ?: SongsQualityInfo.HIGH_QUALITY.v
        }
        set(v) = runBlocking {
            context.dataStore.edit { it[SONGS_QUALITY_SETTINGS] = v.first() }
        }

    var seekButtonSettings: Flow<Int>
        get() = context.dataStore.data.map { it[SEEK_BUTTON_SETTINGS] ?: SeekButton.FIVE.v }
        set(v) = runBlocking {
            context.dataStore.edit { it[SEEK_BUTTON_SETTINGS] = v.first() }
        }

    var songSpeedSettings: Flow<Int>
        get() = context.dataStore.data.map { it[SONG_SPEED_SETTINGS] ?: SongSpeed.ONE.v }
        set(v) = runBlocking {
            context.dataStore.edit { it[SONG_SPEED_SETTINGS] = v.first() }
        }

    var loopSettings: Flow<Boolean>
        get() = context.dataStore.data.map { it[LOOP_SETTINGS] ?: false }
        set(v) = runBlocking {
            context.dataStore.edit { it[LOOP_SETTINGS] = v.first() }
        }

    var autoplaySettings: Flow<Boolean>
        get() = context.dataStore.data.map { it[AUTOPLAY_SETTINGS] ?: true }
        set(v) = runBlocking {
            context.dataStore.edit { it[AUTOPLAY_SETTINGS] = v.first() }
        }

    var doOfflineDownloadWifiSettings: Flow<Boolean>
        get() = context.dataStore.data.map { it[DO_OFFLINE_DOWNLOAD_WIFI_SETTINGS] ?: false }
        set(v) = runBlocking {
            context.dataStore.edit { it[DO_OFFLINE_DOWNLOAD_WIFI_SETTINGS] = v.first() }
        }

    var showPlayingSongOnLockScreenSettings: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[SHOW_PLAYING_SONG_ON_LOCK_SCREEN_SETTINGS] ?: false
        }
        set(v) = runBlocking {
            context.dataStore.edit { it[SHOW_PLAYING_SONG_ON_LOCK_SCREEN_SETTINGS] = v.first() }
        }

    var setWallpaperSettings: Flow<Int>
        get() = context.dataStore.data.map { it[SET_WALLPAPER_SETTINGS] ?: SetWallpaperInfo.NONE.v }
        set(v) = runBlocking {
            context.dataStore.edit { it[SET_WALLPAPER_SETTINGS] = v.first() }
        }

    var pauseMusicOnHeadphoneDetachSettings: Flow<Boolean>
        get() = context.dataStore.data.map { it[PAUSE_MUSIC_ON_HEADPHONE_DETACH_SETTINGS] ?: true }
        set(v) = runBlocking {
            context.dataStore.edit { it[PAUSE_MUSIC_ON_HEADPHONE_DETACH_SETTINGS] = v.first() }
        }

}