package com.rizwansayyed.zene.presenter.ui.extra.standby

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.extra.standby.view.StandbySongInfoView
import com.rizwansayyed.zene.presenter.ui.extra.standby.view.StandbyViewTime
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class StandByModeActivity : ComponentActivity(), StandByInterface {

    companion object {
        lateinit var standByInterface: StandByInterface
    }

    @Inject
    lateinit var player: ExoPlayer

    @Suppress("DEPRECATION")
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        transparentStatusAndNavigation()
        super.onCreate(savedInstanceState)

        standByInterface = this

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) setShowWhenLocked(true)

        screenBrightness(0.05f)

        setContent {
            ZeneTheme {
                val keyboard = LocalSoftwareKeyboardController.current

                Row(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Box(Modifier.weight(7f)) {
                        Column(
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
                            StandbyViewTime()
                        }

                        Spacer(
                            Modifier
                                .align(Alignment.BottomEnd)
                                .width(30.dp)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(listOf(Color.Transparent, Color.Black))
                                )
                        )
                    }
                    Box(Modifier.weight(8f)) {
                        Column(
                            Modifier
                                .align(Alignment.Center)
                                .fillMaxSize()
                        ) {
                            StandbySongInfoView(player)
                        }

                        Spacer(
                            Modifier
                                .align(Alignment.BottomStart)
                                .width(30.dp)
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(listOf(Color.Black, Color.Transparent))
                                )
                        )
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
        registerEvent(FirebaseEvents.FirebaseEvent.STAND_BY_MODE)
    }

    override fun close() {
        finishAffinity()
    }

    override fun screenBrightness(d: Float) {
        val layout = window.attributes
        layout.screenBrightness = d
        window.attributes = layout
    }
}
