package com.rizwansayyed.zene.service.player.listener

import androidx.media3.common.MediaItem

class PlayServiceListener {
    companion object {
        private var instance: PlayServiceListener? = null

        fun getInstance(): PlayServiceListener {
            if (instance == null) {
                instance = PlayServiceListener()
            }
            return instance!!
        }
    }

    private var listeners = mutableListOf<PlayerServiceInterface>()

    fun addListener(listener: PlayerServiceInterface) {
        listeners.add(listener)
    }

    fun rmListener(listener: PlayerServiceInterface) {
        listeners.remove(listener)
    }

    fun isBuffering(b: Boolean) {
        for (listener in listeners) {
            listener.songBuffering(b)
        }
    }

    fun playingState() {
        for (listener in listeners) {
            listener.playingStateChange()
        }
    }

    fun mediaItemUpdate(b: MediaItem) {
        for (listener in listeners) {
            listener.mediaItemUpdate(b)
        }
    }
}