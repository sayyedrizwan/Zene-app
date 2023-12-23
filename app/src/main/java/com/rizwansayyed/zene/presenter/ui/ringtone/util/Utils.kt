package com.rizwansayyed.zene.presenter.ui.ringtone.util

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context

object Utils {
    private val ringtonePlayer = ExoPlayer.Builder(context).build()

    fun startPlayingRingtoneSong() {
        val mediaItem = MediaItem.fromUri(demoRingtonePath.toUri())
        ringtonePlayer.setMediaItem(mediaItem)
        ringtonePlayer.prepare()
        ringtonePlayer.playWhenReady = true
        ringtonePlayer.play()
    }

    fun pauseRingtoneSong() {
        ringtonePlayer.pause()
    }

    fun setPlayerDurationDependOnSlider(ringtoneSlider: ClosedFloatingPointRange<Float>) {
        val targetPositionMs = (ringtoneSlider. * player.duration).toLong()
    }

    fun progressRingtoneSong() = ringtonePlayer.currentPosition.toFloat() / ringtonePlayer.duration
    fun isRingtoneSongPlaying() = ringtonePlayer.isPlaying

}