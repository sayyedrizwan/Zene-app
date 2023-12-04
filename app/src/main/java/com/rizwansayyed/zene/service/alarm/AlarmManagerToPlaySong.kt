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
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_TIME
import com.rizwansayyed.zene.utils.DateFormatter.DateStyle.SIMPLE_TIME_SINGLE
import com.rizwansayyed.zene.utils.DateFormatter.toDate
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.ALARM_CHANNEL_ID
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.ALARM_DEFAULT_CHANNEL
import com.rizwansayyed.zene.utils.Utils.isPermission
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
            e.message
        }
    }
}

@AndroidEntryPoint
class AlarmReceiverSong : BroadcastReceiver() {

    @Inject
    lateinit var youtubeAPIImplInterface: YoutubeAPIImplInterface

    private val audioManager by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (alarmTimeSettings.first().trim() != toDate(SIMPLE_TIME_SINGLE)) {
                if (isActive) cancel()
                return@launch
            }

            if (alarmSongData.first()?.pId != null) {
                val songDetails =
                    youtubeAPIImplInterface.songDetail(alarmSongData.first()!!.pId!!).first()

                val data = MusicPlayerList(
                    songDetails.name, songDetails.artists, songDetails.pId, songDetails.thumbnail
                )

                val playerData = MusicPlayerData(
                    false, data, songsLists = listOf(songDetails),
                    playType = MusicType.MUSIC, temp = 122
                )
                musicPlayerData = flowOf(playerData)
                addAllPlayer(playerData.songsLists.toTypedArray(), 0)

                context ?: return@launch

                val playingAlarm =
                    context.resources.getString(R.string.playing_as_morning_alarm_song)

                NotificationViewManager(context).nIds(ALARM_CHANNEL_ID, ALARM_DEFAULT_CHANNEL)
                    .title(context.resources.getString(R.string.morning_song_alarm))
                    .body(String.format(playingAlarm, songDetails.name)).generate()

            } else {
                val playerData = musicPlayerData.first()
                if ((playerData?.songsLists?.size ?: 0) > 0) {
                    var songPosition = 0
                    playerData?.songsLists?.forEachIndexed { i, musicData ->
                        if (musicData?.pId == playerData.v?.songID) songPosition = i
                    }

                    addAllPlayer(playerData?.songsLists?.toTypedArray(), songPosition)

                    context ?: return@launch

                    val playingAlarm =
                        context.resources.getString(R.string.playing_as_morning_alarm_song)

                    NotificationViewManager(context).nIds(ALARM_CHANNEL_ID, ALARM_DEFAULT_CHANNEL)
                        .title(context.resources.getString(R.string.morning_song_alarm))
                        .body(String.format(playingAlarm, playerData?.v?.songName)).generate()
                } else {
                    context ?: return@launch

                    val playingAlarm =
                        context.resources.getString(R.string.no_song_found_to_play_as_morning_alarm)

                    NotificationViewManager(context).nIds(ALARM_CHANNEL_ID, ALARM_DEFAULT_CHANNEL)
                        .title(context.resources.getString(R.string.morning_song_alarm))
                        .body(playingAlarm).generate()
                }
            }

            delay(1.seconds)

            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0
            )

            if (isActive) cancel()
        }
    }

}