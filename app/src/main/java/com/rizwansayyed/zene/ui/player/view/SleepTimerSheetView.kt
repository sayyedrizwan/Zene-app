package com.rizwansayyed.zene.ui.player.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold

val sleepMinutes = listOf(5, 10, 15, 30, 45, 60, -1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimerSheet(close: () -> Unit) {
    ModalBottomSheet(close, containerColor = MainColor, contentColor = MainColor) {
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
            TextPoppins("4 min 5 sec left", false, Color.White, 14)
            Spacer(Modifier.height(60.dp))

            sleepMinutes.forEach {
                if (it == -1) {
                    TextPoppins(stringResource(R.string.end_of_track), true, size = 14)
                } else {
                    TextPoppinsSemiBold(
                        "$it ${stringResource(R.string.minutes)}", true, size = 14
                    )
                }
                Spacer(Modifier.height(15.dp))
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}