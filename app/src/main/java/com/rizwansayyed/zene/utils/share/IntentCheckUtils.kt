package com.rizwansayyed.zene.utils.share

import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Base64
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_LOCATION_SHARING_TYPE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_OPEN_PROFILE_PLAYLIST_TYPE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_OPEN_PROFILE_TYPE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.CONNECT_SEND_CHAT_MESSAGE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_BODY
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_EMAIL
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_ID
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LAT
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_LON
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TITLE
import com.rizwansayyed.zene.service.FirebaseAppMessagingService.Companion.FCM_TYPE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_ARTIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_CONNECT_PROFILE_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MOVIES_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MY_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_SETTINGS_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.ui.videoplayer.VideoPlayerActivity
import com.rizwansayyed.zene.utils.ChatTempDataUtils.doOpenChatOnConnect
import com.rizwansayyed.zene.utils.ChatTempDataUtils.doOpenPlaylistOnConnect
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ARTIST
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_M
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_MIX
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_NEWS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_SERIES
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SONG
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_CONNECT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_ENTERTAINMENT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_MY_LIBRARY
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_PODCASTS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_PRIVACY_POLICY
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SEARCH
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SETTINGS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_VIDEO
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

data class IntentFCMNotification(
    val title: String, val body: String, val type: String, val lat: String?, val long: String?
)

class IntentCheckUtils(
    private val intent: Intent,
    private val navViewModel: NavigationViewModel,
    private val viewModel: PlayerViewModel
) {

    fun call() = CoroutineScope(Dispatchers.IO).launch {
        delay(1.seconds)

        if (intent.data.toString().contains("/email-login")
            && Firebase.auth.isSignInWithEmailLink(intent.data.toString())
        ) {
            intent.data?.let { navViewModel.loginUtils.startAuthEmailLogin(it) }
        }


        val userInfo = userInfo.firstOrNull()
        if (userInfo?.isLoggedIn() == false) return@launch

        withContext(Dispatchers.Main) {
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

            if (intent.getStringExtra(Intent.ACTION_SENDTO) == CONNECT_OPEN_PROFILE_TYPE) {
                navViewModel.setHomeNavSections(HomeNavSelector.CONNECT)
                intent.getStringExtra(FCM_EMAIL)?.let {
                    triggerHomeNav("$NAV_CONNECT_PROFILE_PAGE$it")
                }
            }

            if (intent.getStringExtra(Intent.ACTION_SENDTO) == CONNECT_OPEN_PROFILE_PLAYLIST_TYPE) {
                doOpenPlaylistOnConnect = true
                navViewModel.setHomeNavSections(HomeNavSelector.CONNECT)
                intent.getStringExtra(FCM_EMAIL)?.let {
                    triggerHomeNav("$NAV_CONNECT_PROFILE_PAGE$it")
                }
                CoroutineScope(Dispatchers.IO).launch {
                    delay(1.seconds)
                    intent.getStringExtra(FCM_ID)?.let {
                        withContext(Dispatchers.Main) {
                            triggerHomeNav("$NAV_MY_PLAYLIST_PAGE$it")
                        }
                    }
                    if (isActive) cancel()
                }
            }

            if (intent.getStringExtra(Intent.ACTION_SENDTO) == CONNECT_SEND_CHAT_MESSAGE) {
                doOpenChatOnConnect = true
                navViewModel.setHomeNavSections(HomeNavSelector.CONNECT)
                intent.getStringExtra(FCM_EMAIL)?.let {
                    triggerHomeNav("$NAV_CONNECT_PROFILE_PAGE$it")
                }
            }

            if (intent.getStringExtra(Intent.ACTION_SENDTO) == CATEGORY_APP_MUSIC) {
                navViewModel.setMusicPlayer(true)
            }
        }

        withContext(Dispatchers.Main) {
            val data = intent.data ?: return@withContext

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
            } else if (data.toString() == "$ZENE_URL$ZENE_URL_PRIVACY_POLICY") {
                delay(500)
                MediaContentUtils.openCustomBrowser(data.toString())
            }


            if (data.toString().contains("$ZENE_URL$ZENE_SONG")) {
                val id = data.toString().substringAfterLast(ZENE_SONG)
                viewModel.songInfoPlay(id)
            } else if (data.toString().contains("$ZENE_URL$ZENE_RADIO")) {
                val id = data.toString().substringAfterLast(ZENE_RADIO)
                viewModel.radioInfoPlay(id)
            } else if (data.toString().contains("$ZENE_URL$ZENE_VIDEO")) {
                val id = data.toString().substringAfterLast(ZENE_VIDEO)
                Intent(context, VideoPlayerActivity::class.java).apply {
                    flags = FLAG_ACTIVITY_NEW_TASK
                    putExtra(Intent.ACTION_VIEW, id)
                    context.startActivity(this)
                }
            } else if (data.toString().contains("$ZENE_URL$ZENE_MIX")) {
                val id = data.toString().substringAfterLast(ZENE_MIX)
                triggerHomeNav("$NAV_PLAYLIST_PAGE${id}")
            } else if (data.toString().contains("$ZENE_URL$ZENE_ARTIST")) {
                val id = data.toString().substringAfterLast(ZENE_ARTIST)
                triggerHomeNav("$NAV_ARTIST_PAGE${id}")
            } else if (data.toString().contains("$ZENE_URL$ZENE_PODCAST_SERIES")) {
                val id = data.toString().substringAfterLast(ZENE_PODCAST_SERIES)
                triggerHomeNav("$NAV_PODCAST_PAGE${id}")
            } else if (data.toString().contains("$ZENE_URL$ZENE_PODCAST")) {
                val id = data.toString().substringAfterLast(ZENE_PODCAST).replace("_", "/")
                viewModel.podcastInfoPlay(id)
            } else if (data.toString().contains("$ZENE_URL$ZENE_NEWS")) {
                val id = data.toString().substringAfterLast(ZENE_NEWS)
                val url = String(Base64.decode(id, Base64.NO_WRAP), Charsets.UTF_8)
                MediaContentUtils.openCustomBrowser(url)
            } else if (data.toString().contains("$ZENE_URL$ZENE_M")) {
                val id = data.toString().substringAfterLast(ZENE_M)
                triggerHomeNav("$NAV_MOVIES_PAGE${id}")
            } else if (data.toString().contains("$ZENE_URL$ZENE_AI_MUSIC")) {
                val id = data.toString().substringAfterLast(ZENE_AI_MUSIC)
                viewModel.aiMusicInfoPlay(id)
            }
        }

        intent.data = null
    }
}