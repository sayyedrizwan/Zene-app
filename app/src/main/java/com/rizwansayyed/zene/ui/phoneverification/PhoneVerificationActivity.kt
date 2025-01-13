package com.rizwansayyed.zene.ui.phoneverification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.service.location.BackgroundLocationTracking
import com.rizwansayyed.zene.ui.login.LoginView
import com.rizwansayyed.zene.ui.main.connect.HomeConnectView
import com.rizwansayyed.zene.ui.main.ent.EntertainmentNewsView
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.CONNECT
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.ENT
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.HOME
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector.SETTINGS
import com.rizwansayyed.zene.ui.main.home.HomeView
import com.rizwansayyed.zene.ui.main.view.HomeBottomNavigationView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.utils.MainUtils.startAppService
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import com.rizwansayyed.zene.viewmodel.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PhoneVerificationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {

                }
            }
        }
    }
}