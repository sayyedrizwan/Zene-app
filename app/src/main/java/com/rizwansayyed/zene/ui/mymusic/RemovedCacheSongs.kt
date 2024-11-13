package com.rizwansayyed.zene.ui.mymusic


object GlobalRemovedCacheSongsProvider {
    private var listeners: RemovedCacheSongs? = null

    fun registerListener(listener: RemovedCacheSongs) {
        this.listeners = listener
    }

    fun unregisterListener() {
        this.listeners = null
    }

    fun sendEvent(songID: String) {
        listeners?.onRemoved(songID)
    }
}

interface RemovedCacheSongs {
    fun onRemoved(songID: String)
}