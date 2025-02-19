package com.rizwansayyed.zene.utils

import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_LOCATION_SHARING_TYPE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_BODY
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LAT
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LON
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TITLE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TYPE
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

data class IntentFCMNotification(
    val title: String, val body: String, val type: String, val lat: String?, val long: String?
)

class IntentCheckUtils(intent: Intent, navViewModel: NavigationViewModel) {

    init {
        if (intent.getStringExtra(FCM_TYPE) == CONNECT_LOCATION_SHARING_TYPE) {
            if (intent.getStringExtra(FCM_TITLE) != null && intent.getStringExtra(FCM_BODY) != null) {
                val n = IntentFCMNotification(
                    intent.getStringExtra(FCM_TITLE)!!,
                    intent.getStringExtra(FCM_BODY)!!,
                    CONNECT_LOCATION_SHARING_TYPE,
                    intent.getStringExtra(FCM_LAT) ?: "",
                    intent.getStringExtra(FCM_LON) ?: "",
                )
                navViewModel.setHomeInfoNavigation(n)
            }
        }

        if (intent.getStringExtra(Intent.ACTION_SENDTO) == CATEGORY_APP_MUSIC) {
            navViewModel.setMusicPlayer(true)
        }
    }
}