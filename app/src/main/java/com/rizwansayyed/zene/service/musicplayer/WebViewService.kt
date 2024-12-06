package com.rizwansayyed.zene.service.musicplayer

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.view.View
import android.webkit.WebView

class WebViewService(context: Context) : WebView(context) {

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.VISIBLE) super.onWindowVisibilityChanged(visibility)
    }
}

fun audioWebViewManager(context: Context) {
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_MEDIA)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
        .setAudioAttributes(audioAttributes)
        .setAcceptsDelayedFocusGain(true)
        .setWillPauseWhenDucked(false)
        .setOnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> {
                    // Resume or boost playback volume
                }

                AudioManager.AUDIOFOCUS_LOSS -> {
                    // Pause playback or stop
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    // Pause playback temporarily
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    // Reduce volume (ducking)
                }
            }
        }
        .build()

    audioManager.abandonAudioFocusRequest(focusRequest)
}