package com.rizwansayyed.zene.presenter.ui.extra.standby

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.presenter.theme.ZeneTheme
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.extra.view.StandbyViewTime
import com.rizwansayyed.zene.presenter.util.UiUtils.transparentStatusAndNavigation
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

        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) setShowWhenLocked(true)


        setContent {
            ZeneTheme {
                val keyboard = LocalSoftwareKeyboardController.current


                Row(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    Column(Modifier.weight(7f)) {
                        StandbyViewTime()
                    }
                    Column(Modifier.weight(5f)) {
                        Spacer(Modifier.fillMaxSize().background(Color.Green))
                    }
                }


                LaunchedEffect(Unit) {
                    delay(1.seconds)
                    keyboard?.hide()
                }
            }
        }
    }

    override fun close() {
        finishAffinity()
    }
}
