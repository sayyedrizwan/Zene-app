package com.rizwansayyed.zene.presenter.ui.ringtone.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

object Utils {
    val ringtonePlayer = ExoPlayer.Builder(context).build()

    fun startPlayingRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        val mediaItem = MediaItem.fromUri(demoRingtonePath.toUri())
        ringtonePlayer.setMediaItem(mediaItem)
        ringtonePlayer.prepare()
        ringtonePlayer.repeatMode = Player.REPEAT_MODE_ONE
        ringtonePlayer.playWhenReady = true
        ringtonePlayer.play()


        if (isActive) cancel()
    }

    fun pauseRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        ringtonePlayer.pause()


        if (isActive) cancel()
    }

    fun playOrPauseRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        if (ringtonePlayer.isPlaying) ringtonePlayer.pause()
        else ringtonePlayer.play()


        if (isActive) cancel()
    }

    fun setPlayerDurationDependOnSlider(ringtoneSlider: ClosedFloatingPointRange<Float>) {
        val start = (ringtoneSlider.start * ringtonePlayer.duration) / 100
        CoroutineScope(Dispatchers.Main).launch {
            ringtonePlayer.seekTo(start.toLong())

            if (isActive) cancel()
        }
    }

    fun ifSongGoingOutOfSlider(ringtoneSlider: ClosedFloatingPointRange<Float>) {
        val start = (ringtoneSlider.start * ringtonePlayer.duration) / 100
        val end = (ringtoneSlider.endInclusive * ringtonePlayer.duration) / 100
        CoroutineScope(Dispatchers.Main).launch {
            if (ringtonePlayer.currentPosition > end) {
                ringtonePlayer.seekTo(start.toLong())
            }
            if (isActive) cancel()
        }
    }


    fun progressRingtoneSong() = ringtonePlayer.currentPosition.toFloat() / ringtonePlayer.duration
    fun isRingtoneSongPlaying() = ringtonePlayer.isPlaying


    fun writeRingtoneSettings(): Boolean {
        return if (!Settings.System.canWrite(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.packageName)
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            context.resources.getString(R.string.need_permission_to_set_the_ringtone).toast()
            false
        } else
            true

    }

    fun startCroppingAndSaving(ringtoneSlider: ClosedFloatingPointRange<Float>) = CoroutineScope(Dispatchers.IO).launch {
        val demoRingtonePathNew = File(context.cacheDir, "new_demo_ringtone.mp3")
        val session =
//            FFmpegKit.execute("ffmpeg -ss 50000 -i '/data/data/com.rizwansayyed.zene/cache/demo_ringtone.mp3' -to 15000 -c copy /data${demoRingtonePathNew.absolutePath}")
            FFmpegKit.execute("ffmpeg -i '/data/data/com.rizwansayyed.zene/cache/demo_ringtone.mp3' -codec:a libmp3lame -b:a 128k /data${demoRingtonePathNew.absolutePath}")

        val failStackTrace = session.failStackTrace

        Log.d("TAG", "startCroppingAndSaving: runned faile $failStackTrace")
        val logs = session.logs.forEach {
            Log.d("TAG", "startCroppingAndSaving: runned session ${it.message}")
        }


        if (ReturnCode.isSuccess(session.returnCode)) {
            "success".toast()
        } else if (ReturnCode.isCancel(session.returnCode)) {

            "canceled".toast()

        } else {
            String.format(
                "Command failed with state %s and rc %s.%s",
                session.getState(),
                session.getReturnCode(),
                session.getFailStackTrace()
            ).toast()

        }
    }
}