package com.rizwansayyed.zene.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.data.db.DataStoreManager.timerDataDB
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.receiver.AlarmListenerReceiver
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.time.Duration.Companion.seconds

object SleepTimerUtils {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    private val intent = Intent(context, AlarmListenerReceiver::class.java).apply {
        putExtra(Intent.ACTION_MAIN, 0)
    }

    private val pendingIntentSleeper = PendingIntent
        .getBroadcast(context, 199, intent, PendingIntent.FLAG_IMMUTABLE)

    fun setSleepAlarm() = CoroutineScope(Dispatchers.IO).launch {
        delay(2.seconds)
        val info = timerDataDB.firstOrNull()
        if (info?.hour == null && info?.minutes == null) {
            alarmManager?.cancel(pendingIntentSleeper)
            return@launch
        }
        alarmManager?.cancel(pendingIntentSleeper)
        delay(1.seconds)
        try {
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, info.hour!!)
                set(Calendar.MINUTE, info.minutes!!)
                set(Calendar.SECOND, 0)
            }

            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, pendingIntentSleeper
            )
        } catch (e: Exception) {
            e.message
        }

        if (isActive) cancel()
    }
}