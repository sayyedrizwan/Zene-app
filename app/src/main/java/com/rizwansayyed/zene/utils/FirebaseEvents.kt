package com.rizwansayyed.zene.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

object FirebaseEvents {

    enum class FirebaseEventsParams {
        CONNECT_CALL_DECLINE, PLAYED_SONG, PLAYED_PODCAST, PLAYED_RADIO, PLAYED_AI_MUSIC,
        GENERATED_CUSTOM_NOTIFICATION,
        STARTED_LOGIN_VIEW, GOOGLE_LOGIN, FACEBOOK_LOGIN, APPLE_LOGIN, EMAIL_LOGIN,
        OPENED_APP,
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