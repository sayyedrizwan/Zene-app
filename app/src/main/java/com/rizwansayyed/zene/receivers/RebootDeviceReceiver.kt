package com.rizwansayyed.zene.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.service.alarm.AlarmManagerToPlaySong
import com.rizwansayyed.zene.utils.NotificationViewManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class RebootDeviceReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManagerToPlaySong: AlarmManagerToPlaySong

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        ApplicationModule.context.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            delay(2.seconds)
            alarmManagerToPlaySong.startAlarmIfThere()
        }
    }
}