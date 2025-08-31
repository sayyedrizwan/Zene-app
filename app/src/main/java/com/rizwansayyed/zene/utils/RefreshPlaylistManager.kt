package com.rizwansayyed.zene.utils


object RefreshPlaylistManager {

    interface RefreshPlaylistListener {
        fun refresh()
    }

    private var callback: RefreshPlaylistListener? = null

    fun setRefreshPlaylistState(state: RefreshPlaylistListener) {
        callback = state
    }

    fun refreshPlaylistState() {
        callback?.refresh()
    }
}