package com.rizwansayyed.zene

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rizwansayyed.zene.ui.home.HomeView
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.subscription.SubscriptionView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_ACTION
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
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

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var listener by remember { mutableStateOf<BroadcastReceiver?>(null) }
            val navController = rememberNavController()
            ZeneTheme {
                NavHost(navController, NAV_SUBSCRIPTION) {
                    composable(NAV_HOME) {
                        HomeView(homeViewModel)
                    }
                    composable(NAV_SUBSCRIPTION) {
                        SubscriptionView()
                    }
                }

                LoginView()
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
        showAdsOnAppOpen.showAds()
    }
}
