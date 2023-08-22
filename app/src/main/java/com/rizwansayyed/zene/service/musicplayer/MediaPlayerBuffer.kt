package com.rizwansayyed.zene.service.musicplayer

import android.app.Activity
import android.net.Uri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import com.rizwansayyed.zene.BaseApplication.Companion.context

@UnstableApi
object MediaPlayerBuffer {

    const val MIN_BUFF = 8 * 1024
    const val MAX_BUFF = 16 * 1024
    const val BUFF_PLAYBACK = 2 * 1024


    lateinit var exoPlayerGlobal: ExoPlayer

    fun isExoPlayerGlobalInitialized() = ::exoPlayerGlobal.isInitialized

    fun generateMediaSource(activity: Activity, playUrl: String): MediaSource {
        val mediaItem = MediaItem.fromUri(Uri.parse(playUrl))

        val mediaSourceFactory = DefaultMediaSourceFactory(activity)
        return mediaSourceFactory.createMediaSource(mediaItem)
    }

}