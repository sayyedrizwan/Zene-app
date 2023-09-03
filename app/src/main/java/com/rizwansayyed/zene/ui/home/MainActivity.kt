package com.rizwansayyed.zene.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.ads.MobileAds
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.service.ads.OpenAdManager
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.artists.ArtistsInfo
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus.*
import com.rizwansayyed.zene.ui.home.homeui.BetaTestDialog
import com.rizwansayyed.zene.ui.home.homeui.HomeNavBar
import com.rizwansayyed.zene.ui.home.homeui.HomepageView
import com.rizwansayyed.zene.ui.musicplay.MusicPlayerView
import com.rizwansayyed.zene.ui.search.SearchMusicArtistView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import dagger.hilt.android.AndroidEntryPoint


// wider team

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val homeNavViewModel: HomeNavViewModel by viewModels()
    private val songsViewModel: SongsViewModel by viewModels()
    private val artistsViewModel: ArtistsViewModel by viewModels()

    private val openAdManager by lazy {
        OpenAdManager(this)
    }


    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this) {}
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
                            SEARCH -> SearchMusicArtistView(songsViewModel, homeNavViewModel, artistsViewModel)
                        }
                    }

                    MusicPlayerView(Modifier.align(Alignment.BottomCenter), homeNavViewModel) {
                        homeNavViewModel.homeNavigationView(SELECT_ARTISTS)
                        artistsViewModel.searchArtists(it.trim().lowercase())
                    }
                    HomeNavBar(Modifier.align(Alignment.BottomCenter), homeNavViewModel)

                    BetaTestDialog()
                }


                BackHandler {
                    if (homeNavViewModel.showMusicPlayerView.value) {
                        homeNavViewModel.hideMusicPlayer()
                        return@BackHandler
                    }

                    if (currentScreen == SELECT_ARTISTS || currentScreen == SEARCH) {
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
        openAdManager.loadAd()
    }

    override fun internetConnected() {
        startDownloadSongsWorkManager()
        songsViewModel.run()
    }
}