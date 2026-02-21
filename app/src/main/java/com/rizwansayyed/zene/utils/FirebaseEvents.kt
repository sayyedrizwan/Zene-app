package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

object FirebaseEvents {

    enum class FirebaseEventsParams {
        CONNECT_CALL_DECLINE, PLAYED_SONG, PLAYED_PODCAST, PLAYED_RADIO, PLAYED_AI_MUSIC,
        GENERATED_CUSTOM_NOTIFICATION,
        STARTED_LOGIN_VIEW, GOOGLE_LOGIN, FACEBOOK_LOGIN, APPLE_LOGIN, EMAIL_LOGIN,
        OPENED_APP,
        HOME_PAGE_VIEW, MUSIC_PAGE_VIEW, RADIO_PAGE_VIEW, PODCAST_PAGE_VIEW, LUX_PAGE_VIEW,
        AI_MUSIC_PAGE_VIEW, MY_LIBRARY_PAGE_VIEW, VIDEO_PAGE_VIEW,

        ENTERTAINMENT_PAGE_VIEW, ENT_DISCOVER_PAGE_VIEW, ENT_BUZZ_PAGE_VIEW, ENT_DATING_PAGE_VIEW, ENT_MOVIES_PAGE_VIEW, ENT_EVENTS_PAGE_VIEW, ENT_LIFESTYLE_PAGE_VIEW
    }


    fun registerEvents(events: FirebaseEventsParams) = CoroutineScope(Dispatchers.IO).safeLaunch {
        val email = userInfo.firstOrNull()?.email
        val name = userInfo.firstOrNull()?.name
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, email)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        firebaseAnalytics.logEvent(events.name.lowercase(), bundle)
    }
}