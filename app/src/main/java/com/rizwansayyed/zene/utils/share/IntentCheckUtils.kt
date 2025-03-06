package com.rizwansayyed.zene.utils.share

import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_LOCATION_SHARING_TYPE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_BODY
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LAT
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LON
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TITLE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TYPE
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_CONNECT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_ENTERTAINMENT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_MY_LIBRARY
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_PODCASTS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SEARCH
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SETTINGS
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        CoroutineScope(Dispatchers.Main).launch {
            val data = intent.data ?: return@launch

            if (data.toString() == "$ZENE_URL$ZENE_URL_CONNECT") {
                navViewModel.setHomeNavSections(HomeNavSelector.CONNECT)
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_SEARCH") {
                navViewModel.setHomeNavSections(HomeNavSelector.SEARCH)
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_ENTERTAINMENT") {
                navViewModel.setHomeNavSections(HomeNavSelector.ENT)
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_SETTINGS") {
                triggerHomeNav(NAV_SETTINGS_PAGE)
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_PODCASTS") {
                triggerHomeNav(NAV_MAIN_PAGE)
                delay(500)
                navViewModel.setHomeSections(HomeSectionSelector.PODCAST)
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_MY_LIBRARY") {
                triggerHomeNav(NAV_MAIN_PAGE)
                delay(500)
                navViewModel.setHomeSections(HomeSectionSelector.MY_LIBRARY)
            }
        }
    }
}