package com.rizwansayyed.zene.presenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.admobCacheTimestamp
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.lastAPISyncTime
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.spotifyToken
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.domain.HomeNavigation.ALL_RADIO
import com.rizwansayyed.zene.domain.HomeNavigation.FEED
import com.rizwansayyed.zene.domain.HomeNavigation.HOME
import com.rizwansayyed.zene.domain.HomeNavigation.MY_MUSIC
import com.rizwansayyed.zene.domain.HomeNavigation.SEARCH
import com.rizwansayyed.zene.domain.HomeNavigation.SETTINGS
import com.rizwansayyed.zene.domain.MusicType.*
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.dialog.IntentsDialogView
import com.rizwansayyed.zene.presenter.ui.dialog.RatingDialogView
import com.rizwansayyed.zene.presenter.ui.home.feed.ArtistsFeedView
import com.rizwansayyed.zene.presenter.ui.home.mood.MoodMusic
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportActivity
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType
import com.rizwansayyed.zene.presenter.ui.home.online.radio.OnlineRadioViewAllView
import com.rizwansayyed.zene.presenter.ui.home.views.AlbumView
import com.rizwansayyed.zene.presenter.ui.home.views.AppUpdateView
import com.rizwansayyed.zene.presenter.ui.home.views.ArtistsView
import com.rizwansayyed.zene.presenter.ui.home.views.BottomNavBar
import com.rizwansayyed.zene.presenter.ui.home.views.HomeOfflineView
import com.rizwansayyed.zene.presenter.ui.home.views.HomeView
import com.rizwansayyed.zene.presenter.ui.home.views.MyMusicView
import com.rizwansayyed.zene.presenter.ui.home.views.SearchView
import com.rizwansayyed.zene.presenter.ui.home.views.SettingsView
import com.rizwansayyed.zene.presenter.ui.home.views.WallpaperSetView
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicDialogSheet
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicPlayerView
import com.rizwansayyed.zene.presenter.ui.splash.MainSplashView
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.service.alarm.AlarmManagerToPlaySong
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.OPEN_MUSIC_PLAYER
import com.rizwansayyed.zene.service.player.utils.Utils.openSettingsPermission
import com.rizwansayyed.zene.service.workmanager.ArtistsInfoWorkManager.Companion.startArtistsInfoWorkManager
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.decryptData
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.simpleDecode
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.AppUrl.ALBUMS_URL_DIFFERENTIATE
import com.rizwansayyed.zene.utils.Utils.AppUrl.ARTIST_URL_DIFFERENTIATE
import com.rizwansayyed.zene.utils.Utils.AppUrl.urlUriType
import com.rizwansayyed.zene.utils.Utils.checkAndClearCache
import com.rizwansayyed.zene.utils.Utils.loadOpenAppAds
import com.rizwansayyed.zene.utils.Utils.printStack
import com.rizwansayyed.zene.utils.Utils.tempApiTests
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


// if going ahead or back on song multiple time on via via notification is causing problem. fix it....

// make smooth music player view opener
// privacy policy viewer
// all edittext are hiding behind keyboard


// can you replicate blur image as in-build
// make fcm better

// in future
// faster sync songs you may like and songs to explore.
// add insta shop items on artists page.
// group music listeners via wifi and bluetooth
// song info in song menu
// song recognization
// backup and sync
// add stories, fb posts to show on feed

// do something like rewind -> https://play.google.com/store/apps/details?id=com.zh.musictimetravel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @UnstableApi
    @Inject
    lateinit var artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var alarmManagerToPlaySong: AlarmManagerToPlaySong

    private val connectivityManager by lazy { getSystemService(ConnectivityManager::class.java) }

    private val navViewModel: HomeNavViewModel by viewModels()
    private val roomViewModel: RoomDbViewModel by viewModels()
    private val homeApiViewModel: HomeApiViewModel by viewModels()
    private val jsoupScrapViewModel: JsoupScrapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) { }
        MobileAds.setAppMuted(true)

        setContent {
            ZeneTheme {
                val activity = LocalContext.current as Activity
                val keyboard = LocalSoftwareKeyboardController.current
                val doSplashScreen by doShowSplashScreen.collectAsState(initial = false)
                val songPlayerView by musicPlayerData.collectAsState(initial = null)
                val coroutine = rememberCoroutineScope()

                var showBottomNav by remember { mutableStateOf(false) }

                val notificationValue = stringResource(id = R.string.need_notification_p)
                val notificationP =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                        if (!it) openSettingsPermission(notificationValue)
                    }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkGreyColor)
                ) {
                    when (navViewModel.homeNavV) {
                        HOME -> if (navViewModel.isOnline) HomeView() else HomeOfflineView()
                        ALL_RADIO -> OnlineRadioViewAllView()
                        FEED -> ArtistsFeedView()
                        SEARCH -> SearchView()
                        MY_MUSIC -> MyMusicView()
                        SETTINGS -> SettingsView(player, alarmManagerToPlaySong)
                    }

                    AnimatedVisibility(navViewModel.selectedArtists.isNotEmpty()) {
                        ArtistsView(artistsThumbnailPlayer)
                    }
                    AnimatedVisibility(navViewModel.selectedAlbum.length > 3) {
                        AlbumView()
                    }
                    AnimatedVisibility(navViewModel.selectedMood != null) {
                        MoodMusic()
                    }

                    if (showBottomNav) BottomNavBar(Modifier.align(Alignment.BottomCenter), player)
                }

                AnimatedVisibility(
                    songPlayerView?.show == true, Modifier,
                    slideInVertically(initialOffsetY = { it / 2 }),
                    slideOutVertically(targetOffsetY = { it / 2 }),
                ) {
                    MusicPlayerView(player, false)
                }

                AnimatedVisibility(navViewModel.songDetailDialog != null) {
                    MusicDialogSheet(navViewModel) {
                        navViewModel.setSongDetailsDialog(null)
                    }
                }

                AnimatedVisibility(navViewModel.setImageAsWallpaper.isNotEmpty()) {
                    WallpaperSetView(navViewModel.setImageAsWallpaper)
                }
                AppUpdateView()

                IntentsDialogView()

                RatingDialogView()

                if (doSplashScreen) MainSplashView()

                BackHandler {
                    coroutine.launch(Dispatchers.IO) {
                        if (musicPlayerData.first()?.show == true) {
                            musicPlayerData =
                                flowOf(musicPlayerData.first()?.apply { show = false })
                            return@launch
                        }
                        if (navViewModel.selectedMood != null) {
                            navViewModel.setTheMood(null)
                            return@launch
                        }
                        if (navViewModel.selectMyMusicPlaylists.value != null) {
                            navViewModel.setSelectMyMusicPlaylists(null)
                            return@launch
                        }
                        if (navViewModel.setImageAsWallpaper.isNotEmpty()) {
                            navViewModel.setImageAsWallpaper("")
                            return@launch
                        }
                        if (navViewModel.selectedArtists.isNotEmpty()) {
                            navViewModel.setArtists("")
                            return@launch
                        }
                        if (navViewModel.selectedAlbum.isNotEmpty()) {
                            navViewModel.setAlbum("")
                            return@launch
                        }
                        if (navViewModel.homeNavV != HOME) {
                            navViewModel.setHomeNav(HOME)
                            return@launch
                        }

                        activity.finish()
                    }
                }

                DisposableEffect(Unit) {
                    try {
                        connectivityManager.registerDefaultNetworkCallback(networkChangeListener)
                    } catch (e: Exception) {
                        e.printStack()
                    }
                    onDispose {
                        try {
                            connectivityManager.unregisterNetworkCallback(networkChangeListener)
                        } catch (e: Exception) {
                            e.printStack()
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    delay(500)
                    showBottomNav = true
                    tempApiTests()
                    delay(1.seconds)
                    keyboard?.hide()
                }
                LaunchedEffect(navViewModel.homeNavV) {
                    navViewModel.setArtists("")
                    navViewModel.setSelectMyMusicPlaylists(null)
                    navViewModel.setAlbum("")
                    navViewModel.setSongDetailsDialog(null)

                    val mpd = musicPlayerData.first()?.apply { show = false }
                    musicPlayerData = flowOf(mpd)
                }
                LaunchedEffect(doSplashScreen) {
                    if (!doSplashScreen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        notificationP.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        doOpenMusicPlayer(intent)
        captureUrlInfo(intent)

    }

    private val networkChangeListener = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            navViewModel.checkAndSetOnlineStatus()
            lifecycleScope.launch(Dispatchers.IO) {
                apis()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        doOpenMusicPlayer(intent)
        captureUrlInfo(intent)
    }

    private val adLoadedCallback = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(app: AppOpenAd) {
            super.onAdLoaded(app)
            registerEvent(FirebaseEvents.FirebaseEvent.OPEN_APP_ADS)
            app.show(this@MainActivity)
            admobCacheTimestamp = flowOf(System.currentTimeMillis())
        }
    }

    @UnstableApi
    override fun onStart() {
        super.onStart()
        registerEvent(FirebaseEvents.FirebaseEvent.OPEN_APP)
        if (!BuildConfig.DEBUG) loadOpenAppAds(this, adLoadedCallback)

        lifecycleScope.launch {
            delay(2.seconds)
            checkAndClearCache()
            roomViewModel.downloadCheckAndLyrics()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(1.seconds)
            if (timestampDifference(lastAPISyncTime.first()) >= 10.minutes.inWholeSeconds) apis()
        }

        lifecycleScope.launch {
            delay(2.seconds)
            alarmManagerToPlaySong.startAlarmIfThere()
        }
    }

    private fun apis() {
        homeApiViewModel.init()
        jsoupScrapViewModel.init()
        roomViewModel.init()
        lastAPISyncTime = flowOf(System.currentTimeMillis())
        startArtistsInfoWorkManager()
    }

    private fun captureUrlInfo(i: Intent?) = lifecycleScope.launch(Dispatchers.IO) {
        val data = i?.data.toString()
        when (urlUriType(data)) {
            MUSIC -> navViewModel.setSongShareDialog(data)
            ALBUMS -> lifecycleScope.launch {
                delay(2.seconds)
                val id = data.substringAfter(ALBUMS_URL_DIFFERENTIATE).trim()
                navViewModel.setAlbum(simpleDecode(id))
            }

            ARTISTS -> lifecycleScope.launch {
                delay(2.seconds)
                val id = data.substringAfter(ARTIST_URL_DIFFERENTIATE).trim()
                navViewModel.setArtists(decryptData(id))
            }

            PARTY -> navViewModel.setSongPartyDialog(data)
            TEXT -> {}
            RADIO -> navViewModel.setRadioShareDialog(data)
            VIDEO -> {}
            null -> {}
        }
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
