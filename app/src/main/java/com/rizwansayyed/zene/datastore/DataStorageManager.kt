package com.rizwansayyed.zene.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rizwansayyed.zene.data.model.IPResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.AUTO_PAUSE_PLAYER_SETTINGS_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.DATA_STORE_KEY
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.EMPTY_ARRAY
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.EMPTY_JSON
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.IP_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.IS_LOOP_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.IS_PREMIUM_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.IS_SHUFFLE_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.MUSIC_PLAYER_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.SEARCH_HISTORY_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.SMOOTH_SONG_TRANSITION_SETTINGS_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.SONG_SPEED_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.USER_INFO_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.VIDEO_PLAYER_CC_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.VIDEO_QUALITY_DB
import com.rizwansayyed.zene.datastore.DataStorageManager.DataStorageKey.VIDEO_SPEED_DB
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.datastore.model.VideoQualityEnum
import com.rizwansayyed.zene.datastore.model.VideoSpeedEnum
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

object DataStorageManager {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_KEY)

    object DataStorageKey {
        const val DATA_STORE_KEY = "zene_data_store"
        const val EMPTY_JSON = "{}"
        const val EMPTY_ARRAY = "[]"

        val USER_INFO_DB = stringPreferencesKey("user_info_db")
        val IP_DB = stringPreferencesKey("ip_db")
        val SEARCH_HISTORY_DB = stringPreferencesKey("search_history_db")
        val VIDEO_QUALITY_DB = stringPreferencesKey("video_quality_db")
        val VIDEO_SPEED_DB = stringPreferencesKey("video_speed_db")
        val SONG_SPEED_DB = stringPreferencesKey("song_speed_db")
        val IS_SHUFFLE_DB = booleanPreferencesKey("is_shuffle_db")
        val IS_PREMIUM_DB = booleanPreferencesKey("is_premium_db")
        val IS_LOOP_DB = booleanPreferencesKey("is_loop_db")
        val VIDEO_PLAYER_CC_DB = booleanPreferencesKey("video_player_cc_db")
        val MUSIC_PLAYER_DB = stringPreferencesKey("music_player_db")
        val AUTO_PAUSE_PLAYER_SETTINGS_DB = booleanPreferencesKey("auto_pause_player_settings_db")
        val SMOOTH_SONG_TRANSITION_SETTINGS_DB =
            booleanPreferencesKey("smooth_song_transition_settings_db")
    }

    var userInfo: Flow<UserInfoResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(UserInfoResponse::class.java).fromJson(it[USER_INFO_DB] ?: EMPTY_JSON)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(UserInfoResponse::class.java).toJson(value.first())
                it[USER_INFO_DB] = json
            }
            if (isActive) cancel()
        }

    var musicPlayerDB: Flow<MusicPlayerData?>
        get() = context.dataStore.data.map {
            moshi.adapter(MusicPlayerData::class.java).fromJson(it[MUSIC_PLAYER_DB] ?: EMPTY_JSON)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(MusicPlayerData::class.java).toJson(value.first())
                it[MUSIC_PLAYER_DB] = json
            }
            if (isActive) cancel()
        }

    var searchHistoryDB: Flow<Array<String>?>
        get() = context.dataStore.data.map {
            moshi.adapter(Array<String>::class.java).fromJson(it[SEARCH_HISTORY_DB] ?: EMPTY_ARRAY)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(Array<String>::class.java).toJson(value.first())
                it[SEARCH_HISTORY_DB] = json
            }
            if (isActive) cancel()
        }

    var ipDB: Flow<IPResponse?>
        get() = context.dataStore.data.map {
            moshi.adapter(IPResponse::class.java).fromJson(it[IP_DB] ?: EMPTY_JSON)
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                val json = moshi.adapter(IPResponse::class.java).toJson(value.first())
                it[IP_DB] = json
            }
            if (isActive) cancel()
        }


    var videoQualityDB: Flow<VideoQualityEnum>
        get() = context.dataStore.data.map {
            val q = VideoQualityEnum.entries.firstOrNull { v -> v.name == it[VIDEO_QUALITY_DB] }
            q ?: VideoQualityEnum.`1440`
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[VIDEO_QUALITY_DB] = value.first().name
            }
            if (isActive) cancel()
        }

    var videoSpeedDB: Flow<VideoSpeedEnum>
        get() = context.dataStore.data.map {
            val q = VideoSpeedEnum.entries.firstOrNull { v -> v.name == it[VIDEO_SPEED_DB] }
            q ?: VideoSpeedEnum.`1_0`
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[VIDEO_SPEED_DB] = value.first().name
            }
            if (isActive) cancel()
        }

    var songSpeedDB: Flow<VideoSpeedEnum>
        get() = context.dataStore.data.map {
            val q = VideoSpeedEnum.entries.firstOrNull { v -> v.name == it[SONG_SPEED_DB] }
            q ?: VideoSpeedEnum.`1_0`
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[SONG_SPEED_DB] = value.first().name
            }
            if (isActive) cancel()
        }

    var videoCCDB: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[VIDEO_PLAYER_CC_DB] ?: true
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[VIDEO_PLAYER_CC_DB] = value.first()
            }
            if (isActive) cancel()
        }

    var isShuffleDB: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[IS_SHUFFLE_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[IS_SHUFFLE_DB] = value.first()
            }
            if (isActive) cancel()
        }

    var isLoopDB: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[IS_LOOP_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[IS_LOOP_DB] = value.first()
            }
            if (isActive) cancel()
        }

    var isPremiumDB: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[IS_PREMIUM_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[IS_PREMIUM_DB] = value.first()
            }
            if (isActive) cancel()
        }

    var autoPausePlayerSettings: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[AUTO_PAUSE_PLAYER_SETTINGS_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[AUTO_PAUSE_PLAYER_SETTINGS_DB] = value.first()
            }
            if (isActive) cancel()
        }


    var smoothSongTransitionSettings: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[SMOOTH_SONG_TRANSITION_SETTINGS_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[SMOOTH_SONG_TRANSITION_SETTINGS_DB] = value.first()
            }
            if (isActive) cancel()
        }

    var pauseHistorySettings: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[SMOOTH_SONG_TRANSITION_SETTINGS_DB] ?: false
        }
        set(value) = runBlocking(Dispatchers.IO) {
            context.dataStore.edit {
                it[SMOOTH_SONG_TRANSITION_SETTINGS_DB] = value.first()
            }
            if (isActive) cancel()
        }

    suspend fun lockChatSettings(id: String, v: Boolean? = null): Flow<Boolean?> {
        val key = booleanPreferencesKey("${id}_lock_chat_settings_db")
        if (v == null) return context.dataStore.data.map {
            it[key] ?: false
        } else {
        context.dataStore.edit { it[key] = v }
        return flowOf(false)
    }
    }
}