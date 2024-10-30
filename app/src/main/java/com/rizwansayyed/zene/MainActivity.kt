package com.rizwansayyed.zene

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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
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
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.mood.MoodView
import com.rizwansayyed.zene.ui.player.MusicPlayerView
import com.rizwansayyed.zene.ui.playlists.PlaylistsView
import com.rizwansayyed.zene.ui.playlists.UserPlaylistsView
import com.rizwansayyed.zene.ui.radio.RadioView
import com.rizwansayyed.zene.ui.search.SearchView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.AdsClickWebView
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
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
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
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val homeNavModel: HomeNavModel by viewModels()
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()
    private val zeneViewModel: ZeneViewModel by viewModels()
    private val radioViewModel: RadioViewModel by viewModels()

    private var jobCurrent: Job? = null

    @OptIn(UnstableApi::class)
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
                    NavHost(navController, NAV_HOME) {
                        composable(NAV_HOME) {
                            HomeView(notificationPermission, homeViewModel) {
                                notificationPermissionDialog = true
                            }

                            LaunchedEffect(Unit) {
                                homeNavModel.selectedMenuItems(NavHomeMenu.HOME)
                            }
                        }
                        composable(NAV_MY_MUSIC) {
                            MyMusicView(zeneViewModel)

                            LaunchedEffect(Unit) {
                                homeNavModel.selectedMenuItems(NavHomeMenu.MY_MUSIC)
                            }
                        }
                        composable(NAV_FEED) {
                            FeedView(zeneViewModel)
                        }
                        composable(NAV_RADIO) {
                            RadioView {
                                navController.popBackStack()
                            }

                            LaunchedEffect(Unit) {
                                homeNavModel.selectedMenuItems(NavHomeMenu.RADIO)
                            }
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

                            LaunchedEffect(Unit) {
                                homeNavModel.selectedMenuItems(NavHomeMenu.SEARCH)
                            }
                        }
                        composable(NAV_MOOD) {
                            MoodView(homeViewModel, it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_ARTISTS) {
                            ArtistsView(it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                    }

                    NavHomeView(Modifier.align(Alignment.BottomEnd), playerInfo, homeNavModel)

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
                            homeViewModel.init(true)
                            checkNotificationPermissionAndAsk(notificationPermission) {
                                notificationPermissionDialog = true
                            }
                        } else {
                            logEvents(FirebaseLogEvents.FirebaseEvents.NAVIGATION_DATA)
                            navController.navigate(data)
                        }
                    }
                }

                registerNavCommand(this@MainActivity, listener)

                onDispose {
                    unregisterReceiver(listener)
                }
            }

            LaunchedEffect(Unit) {
                MobileAds.initialize(this@MainActivity) { }

                val networkRequest = NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build()

                val connectivityManager =
                    getSystemService(ConnectivityManager::class.java) as ConnectivityManager
                connectivityManager.requestNetwork(networkRequest, networkCallback)
            }
        }

        checkAndRunWeb(intent)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            homeViewModel.init(true)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        checkAndRunWeb(intent)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(2.seconds)
            if (userInfoDB.firstOrNull()?.isLoggedIn() == true)
                ShowAdsOnAppOpen(this@MainActivity).showAds()
        }
        homeViewModel.userArtistsList()

        startMusicService()

        logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_APP)
        logEvents(
            FirebaseLogEvents.FirebaseEvents.APP_LANGUAGE, Locale.getDefault().displayLanguage
        )
        homeNavModel.clearAdsTs()
        homeNavModel.webViewStatus()

        AlarmTimerUtils(timerDataDB, AlarmTimerType.SLEEP_TIMER).setAnAlarm()
        AlarmTimerUtils(wakeUpDataDB, AlarmTimerType.WAKE_TIMER).setAnAlarm()

        jobCurrent?.cancel()
        jobCurrent = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(4.seconds)
                if (timeDifferenceInSeconds(homeNavModel.lastAdRunTimestamp) >= 7) {
                    homeNavModel.webViewStatus()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        jobCurrent?.cancel()
    }

    private fun startMusicService() = lifecycleScope.launch(Dispatchers.Main) {
        delay(2.seconds)
        if (isMusicServiceRunning(this@MainActivity)) return@launch
        Intent(this@MainActivity, MusicPlayService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                startForegroundService(this)
            } catch (e: Exception) {
                e.message
            }
        }
    }

    private fun checkAndRunWeb(intent: Intent) {
        if ((intent.getStringExtra(Intent.ACTION_MAIN) ?: "") == "PLAYER") {
            homeNavModel.showMusicPlayer(true)
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(1.seconds)
            val appLinkData: Uri = intent.data ?: return@launch

            if (appLinkData.toString().contains(SONG_INNER)) {
                val songLink = decryptData(appLinkData.toString().substringAfterLast(SONG_INNER))
                val info = homeViewModel.getSongInfo(songLink)
                if (info?.id == null) {
                    resources.getString(R.string.sorry_no_song_found).toast()
                    return@launch
                }

                sendWebViewCommand(info, listOf(info))
                delay(1.seconds)
                homeNavModel.showMusicPlayer(true)
            } else if (appLinkData.toString().contains(RADIO_INNER)) {
                val radioID = appLinkData.toString().substringAfterLast(RADIO_INNER)
                val info = radioViewModel.getRadioInfo(radioID)
                if (info?.id == null) {
                    resources.getString(R.string.no_radio_found).toast()
                    return@launch
                }

                sendWebViewCommand(info, listOf(info))
                delay(1.seconds)
                homeNavModel.showMusicPlayer(true)
            } else if (appLinkData.toString().contains(ARTISTS_INNER)) {
                val artistsName =
                    decryptData(appLinkData.toString().substringAfterLast(ARTISTS_INNER))
                sendNavCommand(NAV_ARTISTS.replace("{id}", artistsName.trim()))
            } else if (appLinkData.toString().contains(PLAYLIST_ALBUM_INNER)) {
                val playlistID =
                    decryptData(appLinkData.toString().substringAfterLast(PLAYLIST_ALBUM_INNER))

                if (playlistID.contains("zene_p_"))
                    sendNavCommand(NAV_USER_PLAYLISTS.replace("{id}", playlistID.trim()))
                else
                    sendNavCommand(NAV_PLAYLISTS.replace("{id}", playlistID.trim()))

            } else if (appLinkData.toString().contains(VIDEO_INNER)) {
                val videoLink = decryptData(appLinkData.toString().substringAfterLast(VIDEO_INNER))
                openVideoPlayer(videoLink)
            }
        }
    }
}
