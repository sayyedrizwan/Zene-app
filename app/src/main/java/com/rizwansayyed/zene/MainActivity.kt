package com.rizwansayyed.zene

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.service.MusicPlayService
import com.rizwansayyed.zene.service.isMusicServiceRunning
import com.rizwansayyed.zene.ui.artists.ArtistsView
import com.rizwansayyed.zene.ui.extra.MyMusicView
import com.rizwansayyed.zene.ui.feed.FeedView
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
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_FEED
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
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.startAppSettings
import com.rizwansayyed.zene.utils.Utils.vibratePhone
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val homeNavModel: HomeNavModel by viewModels()
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    private val zeneViewModel: ZeneViewModel by viewModels()

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var listener by remember { mutableStateOf<BroadcastReceiver?>(null) }
            var notificationPermissionDialog by remember { mutableStateOf(false) }
            val navController = rememberNavController()

            val playerInfo by musicPlayerDB.collectAsState(initial = null)

            val notificationPermission =
                rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (!it) notificationPermissionDialog = true
                }

            ZeneTheme {
                Box(Modifier.fillMaxSize()) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    NavHost(navController, NAV_FEED) {
                        composable(NAV_HOME) {
                            HomeView(notificationPermission, homeViewModel)
                        }
                        composable(NAV_MY_MUSIC) {
                            MyMusicView(zeneViewModel)
                        }
                        composable(NAV_FEED) {
                            FeedView(zeneViewModel)
                        }
                        composable(NAV_SETTINGS) {
                            SettingsView()
                        }
                        composable(NAV_PLAYLISTS) {
                            PlaylistsView(it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_USER_PLAYLISTS) {
                            UserPlaylistsView(it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_SEARCH) {
                            SearchView(homeViewModel) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_MOOD) {
                            MoodView(homeViewModel, it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_ARTISTS) {
                            ArtistsView(zeneViewModel, it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_SUBSCRIPTION) {
                            SubscriptionView()
                        }
                    }

                    if (playerInfo?.player?.id != null)
                        PlayerThumbnail(Modifier.align(Alignment.BottomEnd), playerInfo) {
                            homeNavModel.showMusicPlayer(true)
                            vibratePhone()
                        }

                    AnimatedVisibility(
                        visible = homeNavModel.showMusicPlayer,
                        enter = slideInVertically(initialOffsetY = { it / 2 }),
                        exit = slideOutVertically(targetOffsetY = { it / 2 }),
                    ) {
                        MusicPlayerView(playerInfo, musicPlayerViewModel) {
                            homeNavModel.showMusicPlayer(false)
                        }
                    }

                    LoginView()

                    if (notificationPermissionDialog) AlertDialogView(
                        R.string.need_notification_permission,
                        R.string.need_notification_permission_desc,
                        R.string.grant
                    ) {
                        if (it) startAppSettings()
                        notificationPermissionDialog = false
                    }
                }
            }

            DisposableEffect(Unit) {
                listener = object : BroadcastReceiver() {
                    override fun onReceive(c: Context?, i: Intent?) {
                        c ?: return
                        i ?: return
                        val data = i.getStringExtra(Intent.ACTION_MAIN) ?: return

                        if (data == SYNC_DATA) {
                            homeViewModel.init()
                            checkNotificationPermissionAndAsk(notificationPermission)
                        } else
                            navController.navigate(data)
                    }
                }
                registerNavCommand(this@MainActivity, listener)

                onDispose {
                    unregisterReceiver(listener)
                }
            }

            LaunchedEffect(Unit) {
                MobileAds.initialize(this@MainActivity) { }
            }
        }

        checkAndRunWeb(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAndRunWeb(intent)
    }

    override fun onStart() {
        super.onStart()
        customPlayerNotification(this@MainActivity)
        ShowAdsOnAppOpen(this).showAds()

        homeViewModel.userArtistsList()
        startMusicService()
    }


    private fun startMusicService() {
        if (isMusicServiceRunning(this)) return
        Intent(this, MusicPlayService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startService(this)
        }
    }

    private fun checkAndRunWeb(intent: Intent) {
        val appLinkAction: String? = intent.action
        val appLinkData: Uri? = intent.data
    }
}
