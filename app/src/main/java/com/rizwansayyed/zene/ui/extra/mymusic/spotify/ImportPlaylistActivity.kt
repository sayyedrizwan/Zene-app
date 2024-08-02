package com.rizwansayyed.zene.ui.extra.mymusic.spotify

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.service.MusicPlayService
import com.rizwansayyed.zene.service.isMusicServiceRunning
import com.rizwansayyed.zene.ui.artists.ArtistsView
import com.rizwansayyed.zene.ui.extra.MyMusicView
import com.rizwansayyed.zene.ui.home.HomeView
import com.rizwansayyed.zene.ui.home.checkNotificationPermissionAndAsk
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.mood.MoodView
import com.rizwansayyed.zene.ui.player.MusicPlayerView
import com.rizwansayyed.zene.ui.player.PlayerThumbnail
import com.rizwansayyed.zene.ui.player.customPlayerNotification
import com.rizwansayyed.zene.ui.playlists.PlaylistsView
import com.rizwansayyed.zene.ui.playlists.UserPlaylistsView
import com.rizwansayyed.zene.ui.search.SearchView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.subscription.SubscriptionView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_ARTISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MOOD
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MY_MUSIC
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SUBSCRIPTION
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_USER_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.SYNC_DATA
import com.rizwansayyed.zene.utils.NavigationUtils.registerNavCommand
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.utils.Utils.vibratePhone
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class ImportPlaylistActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkAndRunWeb(intent)
        setContent {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAndRunWeb(intent)
    }

    private fun checkAndRunWeb(intent: Intent) {
        val appLinkAction: String? = intent.action
        val appLinkData: Uri? = intent.data

        appLinkData?.toast()
    }
}
