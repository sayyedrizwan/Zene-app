package com.rizwansayyed.zene.presenter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.lastAPISyncTime
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.domain.HomeNavigation.*
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.home.views.BottomNavBar
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.home.views.HomeView
import com.rizwansayyed.zene.presenter.ui.home.views.SearchView
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicDialogSheet
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicPlayerView
import com.rizwansayyed.zene.presenter.ui.splash.MainSplashView
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.OPEN_MUSIC_PLAYER
import com.rizwansayyed.zene.utils.Utils.checkAndClearCache
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navViewModel: HomeNavViewModel by viewModels()
    private val roomViewModel: RoomDbViewModel by viewModels()
    private val homeApiViewModel: HomeApiViewModel by viewModels()
    private val jsoupScrapViewModel: JsoupScrapViewModel by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)
        setContent {
            ZeneTheme {
                val activity = LocalContext.current as Activity
                val keyboard = LocalSoftwareKeyboardController.current
                val doSplashScreen by doShowSplashScreen.collectAsState(initial = false)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkGreyColor)
                ) {
                    when (navViewModel.homeNavV) {
                        HOME -> HomeView()
                        FEED -> TextBold(v = "feed")
                        SEARCH -> SearchView()
                        MY_MUSIC -> TextBold(v = "music")
                    }

                    BottomNavBar(Modifier.align(Alignment.BottomCenter))
                }

                if (navViewModel.songDetailDialog != null) MusicDialogSheet()
                if (navViewModel.songDetailDialog != null) MusicPlayerView()
                if (doSplashScreen) MainSplashView()


                BackHandler {
                    if (navViewModel.songDetailDialog != null) {
                        navViewModel.setSongDetailsDialog(null)
                        return@BackHandler
                    }
                    if (navViewModel.homeNavV != HOME) {
                        navViewModel.setHomeNav(HOME)
                        return@BackHandler
                    }

                    activity.finish()
                }

                LaunchedEffect(Unit) {
                    keyboard?.hide()
                }
            }
        }

        navViewModel.checkAndSetOnlineStatus()
        navViewModel.resetConfig()

        doOpenMusicPlayer(intent)
        apis()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { doOpenMusicPlayer(it) }
    }


    @UnstableApi
    override fun onStart() {
        super.onStart()
        checkAndClearCache()

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

    private fun doOpenMusicPlayer(i: Intent) = lifecycleScope.launch(Dispatchers.IO) {
        val doOpenPlayer = i.getBooleanExtra(OPEN_MUSIC_PLAYER, false)
        if (doOpenPlayer) {
            val musicPlayerDataLocal = musicPlayerData.first()
            musicPlayerDataLocal?.show = true
            musicPlayerData = flowOf(musicPlayerDataLocal)
        }
    }
}
