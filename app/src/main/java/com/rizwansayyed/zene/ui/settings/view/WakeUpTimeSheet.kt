package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpDataDB
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpMusicDataDB
import com.rizwansayyed.zene.data.db.model.TimerData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.utils.AlarmTimerType
import com.rizwansayyed.zene.utils.AlarmTimerUtils
import com.rizwansayyed.zene.utils.Utils.checkAlarmPermission
import com.rizwansayyed.zene.utils.Utils.openAlarmPermission
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WakeUpTimeSheet(hour: Int, minutes: Int, close: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isTimerSet by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(hour, minutes, false)

    fun setTimers() {
        if (!checkAlarmPermission()) {
            openAlarmPermission()
            return
        }
        wakeUpDataDB = flowOf(TimerData(timePickerState.hour, timePickerState.minute))

        AlarmTimerUtils(wakeUpDataDB, AlarmTimerType.WAKE_TIMER).setAnAlarm()
        close()
    }

    fun clearTimers() {
        wakeUpDataDB = flowOf(TimerData(null, null))
        AlarmTimerUtils(wakeUpDataDB, AlarmTimerType.WAKE_TIMER).setAnAlarm()
        close()
    }

    ModalBottomSheet(close, containerColor = MainColor) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 7.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            TextPoppinsSemiBold(stringResource(R.string.wake_up_timer), false, Color.White, 19)
            Spacer(Modifier.height(5.dp))
            TextPoppins(stringResource(R.string.wake_up_timer_desc), false, Color.White, 14)
            Spacer(Modifier.height(30.dp))

            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                TimeInput(timePickerState)
            }

            Spacer(Modifier.height(30.dp))

            WakeUpSongInfo()

            Spacer(Modifier.height(30.dp))

            Row {
                if (isTimerSet) Column(
                    Modifier
                        .padding(horizontal = 7.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black)
                        .clickable { clearTimers() }
                        .padding(horizontal = 7.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(10.dp))
                    TextPoppins(stringResource(R.string.cancel_timer), false, Color.White, 14)
                    Spacer(Modifier.height(10.dp))
                }


                Column(
                    Modifier
                        .padding(horizontal = 7.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black)
                        .clickable { setTimers() }
                        .padding(horizontal = 7.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(10.dp))
                    TextPoppins(stringResource(R.string.set_timer), false, Color.White, 14)
                    Spacer(Modifier.height(10.dp))
                }
            }

            LaunchedEffect(Unit) {
                if (!checkAlarmPermission()) {
                    isTimerSet = false
                    return@LaunchedEffect
                }

                val td = wakeUpDataDB.firstOrNull()
                if (td?.hour == null && td?.minutes == null) {
                    isTimerSet = false
                    return@LaunchedEffect
                }
                isTimerSet = true
                timePickerState.hour = td.hour!!
                timePickerState.minute = td.minutes!!

                delay(2.seconds)
                keyboardController?.hide()
            }
        }
    }
}


@Composable
fun WakeUpSongInfo() {
    val wakUpSongs by wakeUpMusicDataDB.collectAsState(null)
    Row(
        Modifier
            .padding(horizontal = 5.dp, vertical = 10.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        if (wakUpSongs?.id == null) {
            TextPoppins(stringResource(R.string.not_selected_song_wak_up_alarm), true, size = 16)

            TextPoppins(stringResource(R.string.how_to_add_a_song), true, size = 16)
        } else {
            AsyncImage(
                imgBuilder(wakUpSongs!!.thumbnail), wakUpSongs!!.name,
                Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 9.dp)
            ) {
                TextPoppins(wakUpSongs!!.name ?: "", false, size = 16)
                Spacer(Modifier.height(4.dp))
                TextPoppins(wakUpSongs!!.artists ?: "", false, size = 13)
            }

            ImageIcon(R.drawable.ic_remove_circle, 19) {
                wakeUpMusicDataDB = flowOf(null)
            }
        }
    }
}