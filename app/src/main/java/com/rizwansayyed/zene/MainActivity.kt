package com.rizwansayyed.zene

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.DisposableEffect
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
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.ui.home.HomeView
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.player.MusicPlayerView
import com.rizwansayyed.zene.ui.player.PlayerThumbnail
import com.rizwansayyed.zene.ui.player.customPlayerNotification
import com.rizwansayyed.zene.ui.playlists.PlaylistsView
import com.rizwansayyed.zene.ui.search.SearchView
import com.rizwansayyed.zene.ui.subscription.SubscriptionView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SUBSCRIPTION
import com.rizwansayyed.zene.utils.NavigationUtils.registerNavCommand
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils.vibratePhone
import com.rizwansayyed.zene.viewmodel.HomeNavModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.MusicPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var showAdsOnAppOpen: ShowAdsOnAppOpen

    private val homeViewModel: HomeViewModel by viewModels()
    private val homeNavModel: HomeNavModel by viewModels()
    private val musicPlayerViewModel: MusicPlayerViewModel by viewModels()

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var listener by remember { mutableStateOf<BroadcastReceiver?>(null) }
            val navController = rememberNavController()

            val playerInfo by musicPlayerDB.collectAsState(initial = null)

            ZeneTheme {
                Box {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    NavHost(navController, NAV_HOME) {
                        composable(NAV_HOME) {
                            HomeView(homeViewModel)
                        }
                        composable(NAV_PLAYLISTS) {
                            PlaylistsView(homeViewModel, it.arguments?.getString("id")) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_SEARCH) {
                            SearchView(homeViewModel) {
                                navController.popBackStack()
                            }
                        }
                        composable(NAV_SUBSCRIPTION) {
                            SubscriptionView()
                        }
                    }

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
                }
            }

            DisposableEffect(Unit) {
                listener = object : BroadcastReceiver() {
                    override fun onReceive(c: Context?, i: Intent?) {
                        c ?: return
                        i ?: return
                        val data = i.getStringExtra(Intent.ACTION_MAIN) ?: return
                        navController.navigate(data)
                    }
                }
                registerNavCommand(this@MainActivity, listener)

                onDispose {
                    unregisterReceiver(listener)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        customPlayerNotification(this@MainActivity)
        showAdsOnAppOpen.showAds()
    }
}
