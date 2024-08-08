package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object FirebaseLogEvents {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    enum class FirebaseEvents {
        OPEN_APP, LOADED_INTERSTITIAL_ADS, LOADED_APP_OPEN_ADS, UPDATED_USER_INFO,
        RECEIVED_NOTIFICATION, STARTED_PLAYING_SONG, UPDATED_PLAYBACK_SPEED, TO_NEXT_SONG,
        TO_PREVIOUS_SONG, TAP_PLAYING, TAP_PAUSE, VIEWED_ARTISTS, MY_MUSIC_PERSONAL_PLAYLISTS,
        MY_MUSIC_SONG_HISTORY, STARTED_FOLLOWING, STOP_FOLLOWING, STARTED_ARTISTS_RADIO,
        CREATING_NEW_PLAYLISTS, OPEN_APP_FEED
    }

    fun logEvents(e: FirebaseEvents) = CoroutineScope(Dispatchers.IO).launch {
        val userInfo = DataStoreManager.userInfoDB.firstOrNull()
        val parameters = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, userInfo?.email)
            putString(FirebaseAnalytics.Param.ITEM_NAME, userInfo?.name)
        }
        firebaseAnalytics.logEvent(e.name, parameters)
    }
}