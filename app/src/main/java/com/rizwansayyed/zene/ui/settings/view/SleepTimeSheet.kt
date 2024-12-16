package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.timerDataDB
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpDataDB
import com.rizwansayyed.zene.data.db.model.TimerData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.AlarmTimerType
import com.rizwansayyed.zene.utils.AlarmTimerUtils
import com.rizwansayyed.zene.utils.Utils.checkAlarmPermission
import com.rizwansayyed.zene.utils.Utils.openAlarmPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepTimeSheet(hour: Int, minutes: Int, close: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isTimerSet by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(hour, minutes, false)

    fun setTimers() {
        if (!checkAlarmPermission()) {
            openAlarmPermission()
            return
        }
        timerDataDB = flowOf(TimerData(timePickerState.hour, timePickerState.minute))
        AlarmTimerUtils(timerDataDB, AlarmTimerType.SLEEP_TIMER).setAnAlarm()
        close()
    }

    fun clearTimers() {
        timerDataDB = flowOf(TimerData(null, null))
        AlarmTimerUtils(timerDataDB, AlarmTimerType.SLEEP_TIMER).setAnAlarm()
        close()
    }

    ModalBottomSheet(close, containerColor = MainColor) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 7.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            TextPoppinsSemiBold(stringResource(R.string.sleep_timer), false, Color.White, 19)
            Spacer(Modifier.height(5.dp))
            TextPoppins(stringResource(R.string.sleep_timer_desc), false, Color.White, 14)
            Spacer(Modifier.height(30.dp))

            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                TimeInput(timePickerState)
            }

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
                    Alignment.CenterHorizontally
                ) {
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
                    Alignment.CenterHorizontally
                ) {
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

                val td = timerDataDB.firstOrNull()
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
