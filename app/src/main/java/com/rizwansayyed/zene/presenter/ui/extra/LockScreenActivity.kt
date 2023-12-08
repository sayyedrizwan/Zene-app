package com.rizwansayyed.zene.presenter.ui.extra

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.doShowSplashScreen
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.HomeNavigation.*
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicDialogSheet
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicPlayerView
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class LockScreenActivity : ComponentActivity() {

    @Inject
    lateinit var player: ExoPlayer
    private val navViewModel: HomeNavViewModel by viewModels()

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)

        setContent {
            ZeneTheme {
                val keyboard = LocalSoftwareKeyboardController.current

                MusicPlayerView(player, true)

                AnimatedVisibility(navViewModel.songDetailDialog != null) {
                    MusicDialogSheet()
                }

                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    keyboard?.hide()
                }
            }
        }
    }
}
