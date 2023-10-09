package com.rizwansayyed.zene.presenter

import android.accounts.AccountManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.MainHomePageView
import com.rizwansayyed.zene.presenter.ui.home.online.SaveArtistsButton
import com.rizwansayyed.zene.presenter.ui.splash.MainSplashView
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                val doSplashScreen by doShowSplashScreen.collectAsState(initial = false)

                Box(Modifier.fillMaxSize()) {
                    MainHomePageView(
                        navViewModel, roomViewModel, homeApiViewModel, jsoupScrapViewModel
                    )

//                    MainHomepageOnlineNew()

                    if (navViewModel.selectArtists.isNotEmpty())
                        SaveArtistsButton(Modifier.align(Alignment.BottomCenter), navViewModel)

                }

                if (doSplashScreen) MainSplashView()
            }
        }

        navViewModel.checkAndSetOnlineStatus()
        navViewModel.resetConfig()
    }


    override fun onStart() {
        super.onStart()
        homeApiViewModel.init()
        jsoupScrapViewModel.init()
        roomViewModel.init()
    }
}
