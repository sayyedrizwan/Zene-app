package com.rizwansayyed.zene.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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