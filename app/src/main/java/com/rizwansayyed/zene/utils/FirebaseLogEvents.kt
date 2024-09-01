package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object FirebaseLogEvents {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    enum class FirebaseEvents {
        OPEN_APP, LOADED_INTERSTITIAL_ADS, LOADED_APP_OPEN_ADS, LOADED_REWARDS_ADS, UPDATED_USER_INFO,
        RECEIVED_NOTIFICATION, STARTED_PLAYING_SONG, UPDATED_PLAYBACK_SPEED, TO_NEXT_SONG,
        TO_PREVIOUS_SONG, TAP_PLAYING, TAP_PAUSE, VIEWED_ARTISTS, MY_MUSIC_PERSONAL_PLAYLISTS,
        MY_MUSIC_SONG_HISTORY, STARTED_FOLLOWING, STOP_FOLLOWING, STARTED_ARTISTS_RADIO,
        CREATING_NEW_PLAYLISTS, OPEN_APP_FEED, VISIBLE_REVIEW_DIALOG,
        STARTED_SONG_DETECT_VIEW, FOUND_SONG_DETECT_VIEW, NOT_FOUND_SONG_DETECT_VIEW,
        SONG_PLAYING_VIEW_ON_LOCK_SCREEN, OPEN_LOGIN_VIEW, LOGIN_WITH_GOOGLE, LOGIN_WITH_FB,
        LOGIN_WITH_APPLE, OPEN_MOOD_VIEW, MUSIC_PLAYER_STARTED, OPEN_VIDEO_VIEW,
        OPEN_SONG_VIDEO, OPEN_SONG_LYRICS_VIDEO, MUSIC_LOOP_SETTINGS, AUTOPLAY_NEXT_SONG_SETTINGS,
        PLAYBACK_SONG_RATE_SETTINGS, SHARE_SONG_URL, SHARE_PLAYLISTS_URL, SHARE_ARTISTS_URL, SHARE_VIDEO_URL,
        ADDING_TO_PLAYLISTS, ALBUM_PLAYLIST_VIEW, SAVED_CURRENT_PLAYLIST, REMOVED_CURRENT_PLAYLIST,
        STARTED_CREATING_USER_PLAYLISTS_VIEW, STARTED_SEARCHING, OPEN_EQUALIZER, BANNER_AD_VIEW,
        OPEN_SONG_SHEET, NAVIGATION_DATA, OPEN_SHARE_APP, APP_LANGUAGE, EXO_CLICK_ADS_VIEWED
    }

    fun logEvents(e: FirebaseEvents, n: String? = null) = CoroutineScope(Dispatchers.IO).launch {
        val userInfo = DataStoreManager.userInfoDB.catch { }.firstOrNull()
        val parameters = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, userInfo?.email)
            putString(FirebaseAnalytics.Param.ITEM_NAME, userInfo?.name)
        }
        if (n != null) {
            firebaseAnalytics.logEvent("${e.name.lowercase()}_${n}", parameters)
            return@launch
        }
        firebaseAnalytics.logEvent(e.name.lowercase(), parameters)
    }
}