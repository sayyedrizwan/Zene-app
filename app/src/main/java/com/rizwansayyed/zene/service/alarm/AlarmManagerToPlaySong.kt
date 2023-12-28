package com.rizwansayyed.zene.service.alarm

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.alarmSongData
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.alarmTimeSettings
import com.rizwansayyed.zene.data.db.datastore.TIME_ALARM
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.receivers.AlarmReceiverSong
import com.rizwansayyed.zene.utils.Utils.isPermission
import com.rizwansayyed.zene.utils.Utils.printStack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class AlarmManagerToPlaySong @Inject constructor() {

    private val alarmMgr by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val alarmIntent by lazy {
        Intent(context, AlarmReceiverSong::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 9, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun startAlarmIfThere() = CoroutineScope(Dispatchers.IO).launch {
        delay(2.seconds)
        val alarm = alarmTimeSettings.first()
        if (alarm == TIME_ALARM) {
            alarmMgr.cancel(alarmIntent)
            return@launch
        }

        try {
            val hours = alarm.substringBefore(":").trim().toInt()
            val minutes = alarm.substringAfter(":").trim().toInt()

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hours)
                set(Calendar.MINUTE, minutes)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!isPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)) return@launch
            }

            alarmMgr.setExact(
                AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent
            )
        } catch (e: Exception) {
            e.printStack()
        }
    }
}