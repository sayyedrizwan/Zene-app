package com.rizwansayyed.zene.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.data.db.DataStoreManager.timerDataDB
import com.rizwansayyed.zene.data.db.DataStoreManager.wakeUpDataDB
import com.rizwansayyed.zene.data.db.model.TimerData
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SLEEP_PAUSE_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.WAKE_ALARM
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.utils.AlarmTimerType
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInMinutes
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmListenerReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context?, i: Intent?) {
        c ?: return
        i ?: return

        CoroutineScope(Dispatchers.IO).launch {
            val type = i.getIntExtra(Intent.ACTION_MAIN, -1)
            if (type == AlarmTimerType.SLEEP_TIMER.code) {
                timerDataDB.firstOrNull()?.let {
                    if (it.hour == null && it.minutes == null) return@launch
                    val timeDifference = timeDifferenceInMinutes(timeInMS(it))
                    if (timeDifference < 2) sendWebViewCommand(SLEEP_PAUSE_VIDEO)
                }
            }

            if (type == AlarmTimerType.WAKE_TIMER.code) {
                wakeUpDataDB.firstOrNull()?.let {
                    if (it.hour == null && it.minutes == null) return@launch
                    val timeDifference = timeDifferenceInMinutes(timeInMS(it))
                    if (timeDifference < 2) sendWebViewCommand(WAKE_ALARM)
                }
            }
        }
    }


    private fun timeInMS(ts: TimerData): Long {
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, ts.hour!!)
            set(Calendar.MINUTE, ts.minutes!!)
            set(Calendar.SECOND, 0)
        }

        return calendar.timeInMillis
    }
}