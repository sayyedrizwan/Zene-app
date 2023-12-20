package com.rizwansayyed.zene.presenter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.lastAPISyncTime
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.domain.HomeNavigation.*
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.home.online.radio.OnlineRadioViewAllView
import com.rizwansayyed.zene.presenter.ui.home.views.AlbumView
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
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.service.alarm.AlarmManagerToPlaySong
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.OPEN_MUSIC_PLAYER
import com.rizwansayyed.zene.service.player.utils.Utils.openSettingsPermission
import com.rizwansayyed.zene.service.workmanager.ArtistsInfoWorkManager.Companion.startArtistsInfoWorkManager
import com.rizwansayyed.zene.utils.Utils.checkAndClearCache
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


// if going ahead or back on song multiple time on via via notification is causing problem. fix it....
// add insta shop items on artists page.
// make smooth music player view opener
// privacy policy viewer
// all edittext are hiding behind keyboard

// options menu - set ringtone, set wallpaper,
// can you replicate blur image as in-build
// show feeds
// my music playlists
// make app stanbymode
// group music listeners via wifi and bluetooth
// song recognization
// import songs from spotify and youtube music.
// make artists info cache

// in view all radio is not saving


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var bingScrapsImpl : BingScrapsInterface

    @Inject
    lateinit var alarmManagerToPlaySong: AlarmManagerToPlaySong

    private val connectivityManager by lazy { getSystemService(ConnectivityManager::class.java) }

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
                val songPlayerView by musicPlayerData.collectAsState(initial = null)
                val coroutine = rememberCoroutineScope()

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
                        FEED -> TextBold(v = "feed")
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

                    BottomNavBar(Modifier.align(Alignment.BottomCenter), player)
                }

                AnimatedVisibility(
                    songPlayerView?.show == true, Modifier,
                    slideInVertically(initialOffsetY = { it / 2 }),
                    slideOutVertically(targetOffsetY = { it / 2 }),
                ) {
                    MusicPlayerView(player, false)
                }

                AnimatedVisibility(navViewModel.songDetailDialog != null) {
                    MusicDialogSheet()
                }


                AnimatedVisibility(navViewModel.setImageAsWallpaper.isNotEmpty()) {
                    WallpaperSetView(navViewModel.setImageAsWallpaper)
                }


                if (doSplashScreen) MainSplashView()


                BackHandler {
                    coroutine.launch(Dispatchers.IO) {
                        if (musicPlayerData.first()?.show == true) {
                            musicPlayerData =
                                flowOf(musicPlayerData.first()?.apply { show = false })
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

                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    keyboard?.hide()
                }
                LaunchedEffect(navViewModel.homeNavV) {
                    navViewModel.setArtists("")
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

        connectivityManager.registerDefaultNetworkCallback(networkChangeListener)
    }

    private val networkChangeListener = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            navViewModel.checkAndSetOnlineStatus()
            navViewModel.resetConfig()
            lifecycleScope.launch(Dispatchers.IO) {
                apis()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { doOpenMusicPlayer(it) }
    }


    @UnstableApi
    override fun onStart() {
        super.onStart()
        checkAndClearCache()
        roomViewModel.downloadIfNotDownloaded()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1.seconds)
            if (timestampDifference(lastAPISyncTime.first()) >= 10.minutes.inWholeSeconds) apis()
        }

        lifecycleScope.launch {
            delay(2.seconds)
            alarmManagerToPlaySong.startAlarmIfThere()
        }
    }

    override fun onDestroy() {
        connectivityManager.unregisterNetworkCallback(networkChangeListener)
        super.onDestroy()
    }

    private suspend fun apis() {
        delay(2.seconds)
        homeApiViewModel.init()
        jsoupScrapViewModel.init()
        roomViewModel.init()
        lastAPISyncTime = flowOf(System.currentTimeMillis())
        startArtistsInfoWorkManager()
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
