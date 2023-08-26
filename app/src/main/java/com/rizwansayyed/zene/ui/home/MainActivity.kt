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
import androidx.lifecycle.coroutineScope
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.artists.ArtistsInfo
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus.*
import com.rizwansayyed.zene.ui.home.homeui.HomeNavBar
import com.rizwansayyed.zene.ui.home.homeui.HomepageView
import com.rizwansayyed.zene.ui.musicplay.MusicPlayerView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


// wider team

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val homeNavViewModel: HomeNavViewModel by viewModels()
    private val songsViewModel: SongsViewModel by viewModels()
    private val artistsViewModel: ArtistsViewModel by viewModels()


    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
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
                            MAIN -> HomepageView(songsViewModel, homeNavViewModel, artistsViewModel)
                            SELECT_ARTISTS -> ArtistsInfo(artistsViewModel, homeNavViewModel, songsViewModel)
                            SETTINGS -> SettingsView(songsViewModel)
                        }
                    }

                    MusicPlayerView(Modifier.align(Alignment.BottomCenter), homeNavViewModel)

                    HomeNavBar(Modifier.align(Alignment.BottomCenter), homeNavViewModel)
                }


                BackHandler {
                    if (homeNavViewModel.showMusicPlayerView.value) {
                        homeNavViewModel.hideMusicPlayer()
                        return@BackHandler
                    }

                    if (currentScreen == SELECT_ARTISTS) {
                        homeNavViewModel.homeNavigationView(MAIN)
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

        lifecycle.coroutineScope.launch {
            delay(2.seconds)
            homeNavViewModel.homeNavigationView(HomeNavigationStatus.SELECT_ARTISTS)
            artistsViewModel.searchArtists("Taylor Swift")
        }
    }

    override fun internetConnected() {
        startDownloadSongsWorkManager()
        songsViewModel.run()
    }
}