package com.rizwansayyed.zene.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.utils.DateFormatter
import com.rizwansayyed.zene.utils.NotificationViewManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class AlarmReceiverSong : BroadcastReceiver() {

    @Inject
    lateinit var youtubeAPIImplInterface: YoutubeAPIImplInterface

    private val audioManager by lazy { ApplicationModule.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            if (DataStorageSettingsManager.alarmTimeSettings.first().trim() != DateFormatter.toDate(
                    DateFormatter.DateStyle.SIMPLE_TIME_SINGLE
                )
            ) {
                if (isActive) cancel()
                return@launch
            }

            if (DataStorageSettingsManager.alarmSongData.first()?.pId != null) {
                val songDetails =
                    youtubeAPIImplInterface.songDetail(DataStorageSettingsManager.alarmSongData.first()!!.pId!!)
                        .first()

                val data = MusicPlayerList(
                    songDetails.name, songDetails.artists, songDetails.pId, songDetails.thumbnail
                )

                val playerData = MusicPlayerData(
                    false, data, songsLists = listOf(songDetails),
                    playType = MusicType.MUSIC, temp = 122
                )
                DataStorageManager.musicPlayerData = flowOf(playerData)
                Utils.addAllPlayer(playerData.songsLists.toTypedArray(), 0)

                context ?: return@launch

                val playingAlarm =
                    context.resources.getString(R.string.playing_as_morning_alarm_song)

                NotificationViewManager(context).nIds(
                    NotificationViewManager.ALARM_CHANNEL_ID,
                    NotificationViewManager.ALARM_DEFAULT_CHANNEL
                )
                    .title(context.resources.getString(R.string.morning_song_alarm))
                    .body(String.format(playingAlarm, songDetails.name)).generate()

            } else {
                val playerData = DataStorageManager.musicPlayerData.first()
                if ((playerData?.songsLists?.size ?: 0) > 0) {
                    var songPosition = 0
                    playerData?.songsLists?.forEachIndexed { i, musicData ->
                        if (musicData?.pId == playerData.v?.songID) songPosition = i
                    }

                    Utils.addAllPlayer(playerData?.songsLists?.toTypedArray(), songPosition)

                    context ?: return@launch

                    setVolumeTo80Percent(context)

                    val playingAlarm =
                        context.resources.getString(R.string.playing_as_morning_alarm_song)

                    NotificationViewManager(context).nIds(
                        NotificationViewManager.ALARM_CHANNEL_ID,
                        NotificationViewManager.ALARM_DEFAULT_CHANNEL
                    )
                        .title(context.resources.getString(R.string.morning_song_alarm))
                        .body(String.format(playingAlarm, playerData?.v?.songName)).generate()
                } else {
                    context ?: return@launch

                    val playingAlarm =
                        context.resources.getString(R.string.no_song_found_to_play_as_morning_alarm)

                    NotificationViewManager(context).nIds(
                        NotificationViewManager.ALARM_CHANNEL_ID,
                        NotificationViewManager.ALARM_DEFAULT_CHANNEL
                    )
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

    private fun setVolumeTo80Percent(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (currentVolume == 0) {
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val newVolume = (0.8 * maxVolume).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
        }
    }

}