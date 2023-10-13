package com.rizwansayyed.zene.presenter

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.lastAPISyncTime
import com.rizwansayyed.zene.domain.HomeNavigation
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.BottomNavBar
import com.rizwansayyed.zene.presenter.ui.MainHomePageView
import com.rizwansayyed.zene.presenter.ui.MainHomepageOnlineNew
import com.rizwansayyed.zene.presenter.ui.home.online.SaveArtistsButton
import com.rizwansayyed.zene.presenter.ui.splash.MainSplashView
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


// not all images are laoding properly on selecting users on first times


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navViewModel: HomeNavViewModel by viewModels()
    private val roomViewModel: RoomDbViewModel by viewModels()
    private val homeApiViewModel: HomeApiViewModel by viewModels()
    private val jsoupScrapViewModel: JsoupScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)
        setContent {
            ZeneTheme {
                val activity = LocalContext.current as Activity
                val doSplashScreen by doShowSplashScreen.collectAsState(initial = false)

                Box(Modifier.fillMaxSize()) {
                    MainHomePageView(
                        navViewModel, roomViewModel, homeApiViewModel, jsoupScrapViewModel
                    )

                    MainHomepageOnlineNew()

                    BottomNavBar(Modifier.align(Alignment.BottomCenter))

                    if (navViewModel.selectArtists.isNotEmpty())
                        SaveArtistsButton(Modifier.align(Alignment.BottomCenter), navViewModel)

                }

                if (doSplashScreen) MainSplashView()


                BackHandler {
                    if (navViewModel.homeNav.value != HomeNavigation.HOME) {
                        navViewModel.setHomeNav(HomeNavigation.HOME)
                        return@BackHandler
                    }

                    activity.finish()
                }
            }
        }

        navViewModel.checkAndSetOnlineStatus()
        navViewModel.resetConfig()

        apis()
    }


    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            delay(1.seconds)
            if (timestampDifference(lastAPISyncTime.first()) >= 20) apis()
        }
    }

    private fun apis() {
        homeApiViewModel.init()
        jsoupScrapViewModel.init()
        roomViewModel.init()
        lastAPISyncTime = flowOf(System.currentTimeMillis())
    }
}
