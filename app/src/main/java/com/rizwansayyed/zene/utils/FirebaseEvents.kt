package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object FirebaseEvents {

    enum class FirebaseEvent {
        OPEN_APP, STAND_BY_MODE,
        MUSIC_ON_LOCK_SCREEN,
        ALBUM_URL_SHARE,
        ALBUM_SAVE_SHARE,
       DOWNLOAD_OFFLINE_ALBUMS_SONG,
       PLAY_ALL_ALBUMS_SONG,
       OPEN_ARTISTS_EVENTS,
       TAP_ARTISTS_IMAGE_TO_SET_AS_WALLPAPER
    }


    fun registerEvent(t: FirebaseEvent) = CoroutineScope(Dispatchers.IO).launch {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        firebaseAnalytics.logEvent(t.name.lowercase()) {}
        if (isActive) cancel()
    }

}