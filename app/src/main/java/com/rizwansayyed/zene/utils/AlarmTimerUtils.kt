package com.rizwansayyed.zene.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.rizwansayyed.zene.data.db.model.TimerData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.receiver.AlarmListenerReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class AlarmTimerType(val code: Int) {
    SLEEP_TIMER(199), WAKE_TIMER(198)
}

class AlarmTimerUtils(private val timer: Flow<TimerData?>, alarmType: AlarmTimerType) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val intent = Intent(context, AlarmListenerReceiver::class.java).apply {
        putExtra(Intent.ACTION_MAIN, alarmType.code)
    }

    private val pendingIntent =
        PendingIntent.getBroadcast(context, alarmType.code, intent, PendingIntent.FLAG_IMMUTABLE)

    fun setAnAlarm() = CoroutineScope(Dispatchers.IO).launch {
        delay(2.seconds)
        val info = timer.firstOrNull()
        if (info?.hour == null && info?.minutes == null) {
            alarmManager?.cancel(pendingIntent)
            return@launch
        }
        alarmManager?.cancel(pendingIntent)
        delay(1.seconds)
        try {
            val calendar: Calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, info.hour!!)
                set(Calendar.MINUTE, info.minutes!!)
                set(Calendar.SECOND, 0)
            }

            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis - 1000,
                AlarmManager.INTERVAL_DAY, pendingIntent
            )
        } catch (e: Exception) {
            e.message
        }

        if (isActive) cancel()
    }
}