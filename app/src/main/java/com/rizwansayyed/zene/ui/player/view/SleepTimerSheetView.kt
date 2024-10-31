package com.rizwansayyed.zene.ui.player.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SLEEP_TIMER_BG
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.Utils.getTimeAfterMinutesInMillis
import com.rizwansayyed.zene.utils.Utils.getTimeDifference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

val sleepMinutes = listOf(5, 10, 15, 30, 45, 60, 0, -1)
var sleepTime: Long = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerSheet(close: () -> Unit) {
    ModalBottomSheet(close, containerColor = MainColor, contentColor = MainColor) {
        val coroutine = rememberCoroutineScope()
        var timeText by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            TextPoppinsSemiBold(stringResource(R.string.sleep_timer), false, size = 19)
            Spacer(Modifier.height(5.dp))
            if (timeText.length > 2) TextPoppins(
                "$timeText ${stringResource(R.string.left)}", false, Color.White, 14
            ) else if (sleepTime == -1L) TextPoppins(
                stringResource(R.string.song_will_stop_when_song_ends), false, Color.White, 14
            )

            Spacer(Modifier.height(60.dp))

            sleepMinutes.forEach {
                Box(Modifier.clickable {
                    close()
                    sendWebViewCommand(SLEEP_TIMER_BG, it)
                }) {
                    if (it == -1) TextPoppinsSemiBold(
                        stringResource(R.string.end_of_track), true, size = 14
                    )
                    else if (it <= 0) {
                        if (sleepTime > 0 || sleepTime == -1L) TextPoppinsSemiBold(
                            stringResource(R.string.turn_off_timer), true, size = 14
                        )
                    } else TextPoppinsSemiBold(
                        "$it ${stringResource(R.string.minutes)}", true, size = 14
                    )
                }

                Spacer(Modifier.height(15.dp))
            }

            Spacer(Modifier.height(40.dp))

            LaunchedEffect(Unit) {
                coroutine.launch {
                    while (true) {
                        timeText = when (sleepTime) {
                            0L -> ""
                            (-1).toLong() -> ""
                            else -> getTimeDifference(end = sleepTime)
                        }
                        delay(1.seconds)
                    }
                }
            }
        }
    }
}