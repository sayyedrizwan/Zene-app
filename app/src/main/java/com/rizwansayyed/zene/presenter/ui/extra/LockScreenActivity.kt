package com.rizwansayyed.zene.presenter.ui.extra

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.domain.HomeNavigation.*
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicDialogSheet
import com.rizwansayyed.zene.presenter.ui.musicplayer.MusicPlayerView
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class LockScreenActivity : ComponentActivity() {

    @Inject
    lateinit var player: ExoPlayer

    private val navViewModel: HomeNavViewModel by viewModels()

    @Suppress("DEPRECATION")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) setShowWhenLocked(true)


        setContent {
            ZeneTheme {
                val keyboard = LocalSoftwareKeyboardController.current

                MusicPlayerView(player, true)

                AnimatedVisibility(navViewModel.songDetailDialog != null) {
                    MusicDialogSheet(navViewModel) {
                        navViewModel.setSongDetailsDialog(null)
                    }
                }

                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    keyboard?.hide()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseEvents.registerEvent(FirebaseEvents.FirebaseEvent.MUSIC_ON_LOCK_SCREEN)
    }
}
