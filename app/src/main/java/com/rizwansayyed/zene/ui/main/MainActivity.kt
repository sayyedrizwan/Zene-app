package com.rizwansayyed.zene.ui.main

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.service.notification.HomeNavigationListener
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_ARTIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_CONNECT_PROFILE_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_GO_BACK
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MOVIES_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MY_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PLAYLIST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_PODCAST_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_SETTINGS_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.setNavigationCallback
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.main.connect.HomeConnectView
import com.rizwansayyed.zene.ui.main.connect.profile.ConnectUserProfileView
import com.rizwansayyed.zene.ui.main.ent.EntertainmentNewsView
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.CONNECT
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.ENT
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.HOME
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.NONE
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.NOTIFICATION
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.SEARCH
import com.rizwansayyed.zene.ui.main.home.HomeView
import com.rizwansayyed.zene.ui.main.search.SearchView
import com.rizwansayyed.zene.ui.main.view.HomeBottomNavigationView
import com.rizwansayyed.zene.ui.main.view.LongPressSheetView
import com.rizwansayyed.zene.ui.main.view.NotificationConnectLocationShare
import com.rizwansayyed.zene.ui.main.view.NotificationViewScreenView
import com.rizwansayyed.zene.ui.musicplayer.MusicPlayerView
import com.rizwansayyed.zene.ui.partycall.PartyCallActivity
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.ArtistsView
import com.rizwansayyed.zene.ui.view.LockScreenOrientation
import com.rizwansayyed.zene.ui.view.movies.MoviesView
import com.rizwansayyed.zene.ui.view.myplaylist.MyPlaylistView
import com.rizwansayyed.zene.ui.view.playlist.PlaylistView
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.MainUtils.isNotificationEnabled
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.share.GenerateShortcuts.generateMainShortcuts
import com.rizwansayyed.zene.utils.share.IntentCheckUtils
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                val userInfo by userInfo.collectAsState(initial = null)
                var showLogin by remember { mutableStateOf(false) }
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) { SnackBarManager.setHostState(snackBarHostState) }
                LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT, false)

                Scaffold(snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    }
                }) { padding ->
                    Box(
                        Modifier
                            .consumeWindowInsets(padding)
                            .fillMaxSize()
                            .background(DarkCharcoal)
                    ) {
                        if ((userInfo?.email ?: "").contains("@")) {
                            NavHost(navController, NAV_MAIN_PAGE) {
                                composable(NAV_MAIN_PAGE) {
                                    when (navigationViewModel.homeNavSection) {
                                        HOME -> HomeView(navigationViewModel, userInfo)
                                        CONNECT -> HomeConnectView()
                                        ENT -> EntertainmentNewsView()
                                        NOTIFICATION -> NotificationViewScreenView(
                                            navigationViewModel
                                        )

                                        SEARCH -> SearchView(homeViewModel)
                                        NONE -> {}
                                    }
                                }
                                composable(NAV_SETTINGS_PAGE) {
                                    SettingsView()
                                }

                                composable("$NAV_PODCAST_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) PlaylistView(id, PlaylistsType.PODCAST)
                                }

                                composable("$NAV_PLAYLIST_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) PlaylistView(id, PlaylistsType.PLAYLIST_ALBUMS)
                                }

                                composable("$NAV_CONNECT_PROFILE_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) ConnectUserProfileView(id)
                                }
                                composable("$NAV_ARTIST_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) ArtistsView(id)
                                }
                                composable("$NAV_MY_PLAYLIST_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) MyPlaylistView(id)
                                }
                                composable("$NAV_MOVIES_PAGE{id}") { backStackEntry ->
                                    val id = backStackEntry.arguments?.getString("id")
                                    if (id != null) MoviesView(id)
                                }
                            }

                            HomeBottomNavigationView(
                                Modifier.align(Alignment.BottomCenter), navigationViewModel
                            )

                            if (navigationViewModel.homeNotificationSection != null) {
                                NotificationConnectLocationShare(navigationViewModel)
                            }

                            MusicPlayerView(navigationViewModel)
                            LongPressSheetView(navigationViewModel)
                            BackHandler {
                                if (!navController.popBackStack()) {
                                    finish()
                                }
                            }
                        } else if (showLogin) LoginView()
                    }


                    LaunchedEffect(userInfo?.email) {
                        delay(500)
                        setNavigationCallback(object : HomeNavigationListener {
                            override fun navigate(path: String) {
                                if (path == NAV_GO_BACK) {
                                    navController.popBackStack()
                                    return
                                }
                                navigationViewModel.setMusicPlayer(false)
                                navController.navigate(path) {
                                    if (path == NAV_MAIN_PAGE) {
                                        popUpTo(NAV_MAIN_PAGE) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                        restoreState = false
                                    }
                                }
                            }

                            override fun longPress(value: ZeneMusicData?) {
                                navigationViewModel.setShowMediaInfo(value)
                            }
                        })

                        IntentCheckUtils(intent, navigationViewModel)
                        showLogin = true


                        delay(4.seconds)
                        if (!isNotificationEnabled() && userInfo?.isLoggedIn() == true) {
                            navigationViewModel.setHomeNavSections(NOTIFICATION)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        IntentCheckUtils(intent, navigationViewModel)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.userInfo()
        BackgroundLocationTracking.backgroundTracking?.onDataReceived()
        generateMainShortcuts(this)

        lifecycleScope.launch {
            delay(2.seconds)
//            Intent(this@MainActivity, PartyCallActivity::class.java).apply {
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                putExtra(Intent.EXTRA_EMAIL, "sayyedzafreen4@gmail.com")
//                putExtra(
//                    Intent.EXTRA_USER,
//                    "https://zenemusic.b-cdn.net/ZENE_USER/sayyedzafreen4@gmail.com6442.png"
//                )
//                putExtra(Intent.EXTRA_PACKAGE_NAME, "Zafreen Sayyed")
//                putExtra(Intent.EXTRA_MIME_TYPES, -1)
//                this@MainActivity.startActivity(this)
//            }
        }
    }

}