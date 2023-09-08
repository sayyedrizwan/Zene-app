package com.rizwansayyed.zene.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.rizwansayyed.zene.NetworkCallbackStatus
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.SongsViewModel
import com.rizwansayyed.zene.presenter.music.ReadAllMusics
import com.rizwansayyed.zene.service.ads.OpenAdManager
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import com.rizwansayyed.zene.service.workmanager.startDownloadSongsWorkManager
import com.rizwansayyed.zene.ui.artists.ArtistsInfo
import com.rizwansayyed.zene.ui.artists.artistviewmodel.ArtistsViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavigationStatus.*
import com.rizwansayyed.zene.ui.home.homeui.BetaTestDialog
import com.rizwansayyed.zene.ui.home.homeui.HomeNavBar
import com.rizwansayyed.zene.ui.home.homeui.HomepageView
import com.rizwansayyed.zene.ui.home.homeui.PlaylistDetailsView
import com.rizwansayyed.zene.ui.musicplay.MusicPlayerView
import com.rizwansayyed.zene.ui.search.SearchMusicArtistView
import com.rizwansayyed.zene.ui.settings.SettingsView
import com.rizwansayyed.zene.ui.settings.view.requestCodeSpotify
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.windowManagerNoLimit
import com.rizwansayyed.zene.utils.Utils.showToast
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.AuthorizationResponse.Type.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkCallbackStatus {

    companion object {
        lateinit var networkCallbackStatus: NetworkCallbackStatus
    }

    private val homeNavViewModel: HomeNavViewModel by viewModels()
    private val songsViewModel: SongsViewModel by viewModels()
    private val artistsViewModel: ArtistsViewModel by viewModels()

    private val openAdManager by lazy { OpenAdManager(this) }

    @Inject
    lateinit var mediaPlayerObjects: MediaPlayerObjects

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
                            SELECT_ARTISTS ->
                                ArtistsInfo(artistsViewModel, homeNavViewModel, songsViewModel)

                            SETTINGS -> SettingsView(homeNavViewModel)
                            SEARCH -> SearchMusicArtistView(
                                songsViewModel, homeNavViewModel, artistsViewModel
                            )

                            PLAYLIST -> PlaylistDetailsView(songsViewModel, homeNavViewModel)
                        }
                    }

                    MusicPlayerView(
                        Modifier.align(Alignment.BottomCenter), homeNavViewModel, songsViewModel
                    ) {
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

                    if (currentScreen == SELECT_ARTISTS || currentScreen == SEARCH || currentScreen == PLAYLIST) {
                        homeNavViewModel.homeNavigationView(MAIN)
                        return@BackHandler
                    }
                    finish()
                }
            }
        }
    }

    private val clientId = "07cca9af3ee4411baaf2355a8ea61d3f"
    private val redirectUri = "http://localhost/"
    val REQUEST_CODE = 1337

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun onStart() {
        super.onStart()
        startDownloadSongsWorkManager()
        songsViewModel.run()
        openAdManager.loadAd()

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2.seconds)

//            ReadAllMusics().allMusic()
        }
    }


    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeSpotify) {
            val response = AuthorizationClient.getResponse(resultCode, data);
            when (response.type!!) {
                TOKEN -> {
                    resources.getString(R.string.sync_is_started).showToast()
                    songsViewModel.spotifyLists(response.accessToken) {
                        if (!it)
                            resources.getString(R.string.error_spotify_playlist_sync).showToast()
                    }
                }
                ERROR -> resources.getString(R.string.error_spotify).showToast()
                else -> {}
            }
        }
    }

    override fun internetConnected() {
        startDownloadSongsWorkManager()
        songsViewModel.run()
    }

}