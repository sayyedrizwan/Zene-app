package com.rizwansayyed.zene.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus.*
import com.rizwansayyed.zene.ui.home.homeui.HomeNavBar
import com.rizwansayyed.zene.ui.home.homeui.HomepageView
import com.rizwansayyed.zene.ui.home.musicplay.MusicPlayerView
import com.rizwansayyed.zene.ui.home.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import com.rizwansayyed.zene.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val homeNavViewModel: HomeNavViewModel by viewModels()
    private val songsViewModel: SongsViewModel by viewModels()


    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkCallbackStatus = this

        setContent {
            window.setFlags(windowManagerNoLimit, windowManagerNoLimit)
            val currentScreen = homeNavViewModel.homeNavigationView.value

            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    AnimatedContent(targetState = currentScreen, label = "") {
                        when (it) {
                            MAIN -> HomepageView(songsViewModel, homeNavViewModel)
                            SELECT_ARTISTS -> HomepageView(songsViewModel, homeNavViewModel)
                            SETTINGS -> SettingsView(songsViewModel)
                        }
                    }

                    MusicPlayerView(Modifier.align(Alignment.BottomCenter), homeNavViewModel)

                    HomeNavBar(Modifier.align(Alignment.BottomCenter), homeNavViewModel)
                }

                BackHandler {
                    if (homeNavViewModel.playMusicVideo.value.isNotEmpty()) {
                        homeNavViewModel.playingVideo("")
                        return@BackHandler
                    }

                    if (homeNavViewModel.showMusicPlayerView.value) {
                        homeNavViewModel.hideMusicPlayer()
                        return@BackHandler
                    }

                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startDownloadSongsWorkManager()
        songsViewModel.run()
    }

    override fun internetConnected() {
        startDownloadSongsWorkManager()
        songsViewModel.run()
    }
}