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
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizwansayyed.zene.service.MusicPlayService
import com.rizwansayyed.zene.ui.home.HomeView
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.player.PlayerThumbnail
import com.rizwansayyed.zene.ui.search.SearchView
import com.rizwansayyed.zene.ui.subscription.SubscriptionView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SUBSCRIPTION
import com.rizwansayyed.zene.utils.NavigationUtils.registerNavCommand
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var showAdsOnAppOpen: ShowAdsOnAppOpen

    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var listener by remember { mutableStateOf<BroadcastReceiver?>(null) }
            val navController = rememberNavController()
            ZeneTheme {
                Box {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    NavHost(navController, NAV_HOME) {
                        composable(NAV_HOME) {
                            HomeView(homeViewModel)
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

                    PlayerThumbnail(Modifier.align(Alignment.BottomEnd))

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
                startMusicActivity()

                onDispose {
                    unregisterReceiver(listener)
                }
            }
        }
    }

    private fun startMusicActivity() {
        Intent(this, MusicPlayService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startService(this)
        }
    }

    override fun onStart() {
        super.onStart()
        showAdsOnAppOpen.showAds()
    }
}
