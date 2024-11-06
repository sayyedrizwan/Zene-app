package com.rizwansayyed.zene.ui.earphonetracker

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.data.api.APIHttpService
import com.rizwansayyed.zene.data.api.APIHttpService.youtubeSearchVideoRegion
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.DataStoreManager.timerDataDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpDataDB
import com.rizwansayyed.zene.service.musicplayer.MusicPlayService
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.service.musicplayer.isMusicServiceRunning
import com.rizwansayyed.zene.ui.artists.ArtistsView
import com.rizwansayyed.zene.ui.mymusic.MyMusicView
import com.rizwansayyed.zene.ui.feed.FeedView
import com.rizwansayyed.zene.ui.home.HomeView
import com.rizwansayyed.zene.ui.home.checkNotificationPermissionAndAsk
import com.rizwansayyed.zene.ui.login.GlobalEmailEventProvider
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.login.flow.LoginFlowType
import com.rizwansayyed.zene.ui.mood.MoodView
import com.rizwansayyed.zene.ui.player.MusicPlayerView
import com.rizwansayyed.zene.ui.playlists.PlaylistsView
import com.rizwansayyed.zene.ui.playlists.UserPlaylistsView
import com.rizwansayyed.zene.ui.radio.RadioView
import com.rizwansayyed.zene.ui.search.SearchView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.NavHomeMenu
import com.rizwansayyed.zene.ui.view.NavHomeView
import com.rizwansayyed.zene.utils.AlarmTimerType
import com.rizwansayyed.zene.utils.AlarmTimerUtils
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.decryptData
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_ARTISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_FEED
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MOOD
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MY_MUSIC
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_RADIO
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_USER_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.SYNC_DATA
import com.rizwansayyed.zene.utils.NavigationUtils.registerNavCommand
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.Share.ARTISTS_INNER
import com.rizwansayyed.zene.utils.Utils.Share.PLAYLIST_ALBUM_INNER
import com.rizwansayyed.zene.utils.Utils.Share.RADIO_INNER
import com.rizwansayyed.zene.utils.Utils.Share.SONG_INNER
import com.rizwansayyed.zene.utils.Utils.Share.VIDEO_INNER
import com.rizwansayyed.zene.utils.Utils.startAppSettings
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInSeconds
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import com.rizwansayyed.zene.viewmodel.RadioViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class EarphoneTrackerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal)
                ) {

                }
            }
        }
    }
}
