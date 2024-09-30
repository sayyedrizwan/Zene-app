package com.rizwansayyed.zene.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.ZeneSponsorsItems
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.APP_REVIEW_STATUS
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.ARRAY_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.JSON_EMPTY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.LAST_ADS_TIMESTAMP
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_LOOP
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_PLAYER
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.MUSIC_SPEED
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.PINNED_ARTISTS_LIST
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.PLAYING_SONG_ON_LOCK_SCREEN
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.SEARCH_HISTORY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.SONG_QUALITY
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.SPONSORS_ADS
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.USER_INFOS
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.USER_IP_INFO
import com.rizwansayyed.zene.data.db.model.AppReviewData
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.settings.model.SongQualityTypes
import com.rizwansayyed.zene.utils.Utils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = BuildConfig.APPLICATION_ID)

    object DataStoreManagerObjects {
        const val JSON_EMPTY = "{}"
        const val ARRAY_EMPTY = "[]"
        const val TS_LAST_DATA = 1721759124000
        val USER_INFOS = stringPreferencesKey("user_info")
        val SPONSORS_ADS = stringPreferencesKey("sponsors_ads")
        val SEARCH_HISTORY = stringPreferencesKey("search_history")
        val MUSIC_PLAYER = stringPreferencesKey("music_player")
        val MUSIC_SPEED = stringPreferencesKey("music_speed")
        val MUSIC_LOOP = booleanPreferencesKey("music_loop")
        val PLAYING_SONG_ON_LOCK_SCREEN = booleanPreferencesKey("playing_song_on_lock_screen")
        val PINNED_ARTISTS_LIST = stringPreferencesKey("pinned_artists_list")
        val LAST_ADS_TIMESTAMP = longPreferencesKey("last_ads_timestamp")
        val USER_IP_INFO = stringPreferencesKey("user_ip_info")
        val APP_REVIEW_STATUS = stringPreferencesKey("app_review_status")
        val SONG_QUALITY = stringPreferencesKey("song_quality")
    }


    var userInfoDB
        get() = context.dataStore.data.map {
            moshi.adapter(UserInfoData::class.java).fromJson(it[USER_INFOS] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(UserInfoData::class.java).toJson(value.first())
            context.dataStore.edit { it[USER_INFOS] = json }
        }


    var sponsorsAdsDB
        get() = context.dataStore.data.map {
            moshi.adapter(ZeneSponsorsItems::class.java).fromJson(it[SPONSORS_ADS] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(ZeneSponsorsItems::class.java).toJson(value.first())
            context.dataStore.edit { it[SPONSORS_ADS] = json }
        }

    var searchHistoryDB
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java).fromJson(it[SEARCH_HISTORY] ?: ARRAY_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(Array<String>::class.java).toJson(value.first())
            context.dataStore.edit { it[SEARCH_HISTORY] = json }
        }

    var musicPlayerDB
        get() = context.dataStore.data.map {
            moshi.adapter(MusicPlayerData::class.java).fromJson(it[MUSIC_PLAYER] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(MusicPlayerData::class.java).toJson(value.first())
            context.dataStore.edit { it[MUSIC_PLAYER] = json }
        }

    var musicSpeedSettings
        get() = context.dataStore.data.map {
            MusicSpeed.valueOf(it[MUSIC_SPEED] ?: MusicSpeed.`1`.name)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[MUSIC_SPEED] = value.first().name }
        }

    var musicLoopSettings
        get() = context.dataStore.data.map { it[MUSIC_LOOP] ?: false }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[MUSIC_LOOP] = value.first() }
        }

    var playingSongOnLockScreen
        get() = context.dataStore.data.map { it[PLAYING_SONG_ON_LOCK_SCREEN] ?: false }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[PLAYING_SONG_ON_LOCK_SCREEN] = value.first() }
        }

    var pinnedArtistsList
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java)
                .fromJson(it[PINNED_ARTISTS_LIST] ?: ARRAY_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(Array<String>::class.java).toJson(value.first())
            context.dataStore.edit { it[PINNED_ARTISTS_LIST] = json }
        }

    var lastAdsTimestamp
        get() = context.dataStore.data.map {
            it[LAST_ADS_TIMESTAMP] ?: TS_LAST_DATA
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[LAST_ADS_TIMESTAMP] = value.first() }
        }


    var ipDB
        get() = context.dataStore.data.map {
            moshi.adapter(IpJsonResponse::class.java).fromJson(it[USER_IP_INFO] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(IpJsonResponse::class.java).toJson(value.first())
            context.dataStore.edit { it[USER_IP_INFO] = json }
        }

    var appReviewStatusDB
        get() = context.dataStore.data.map {
            moshi.adapter(AppReviewData::class.java).fromJson(it[APP_REVIEW_STATUS] ?: JSON_EMPTY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            val json = moshi.adapter(AppReviewData::class.java).toJson(value.first())
            context.dataStore.edit { it[APP_REVIEW_STATUS] = json }
        }

    var songQualityDB
        get() = context.dataStore.data.map {
            SongQualityTypes.valueOf(it[SONG_QUALITY] ?: SongQualityTypes.HIGH_QUALITY.name)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit { it[SONG_QUALITY] = value.first().name }
        }
}