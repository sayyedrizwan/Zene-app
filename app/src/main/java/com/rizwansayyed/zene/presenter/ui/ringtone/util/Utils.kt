package com.rizwansayyed.zene.presenter.ui.ringtone.util

import android.util.Log
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.Math.min
import kotlin.math.max

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

}