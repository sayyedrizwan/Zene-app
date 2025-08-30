package com.rizwansayyed.zene.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive

object SnackBarManager {
    private var snackBarHostState: SnackbarHostState? = null

    fun setHostState(state: SnackbarHostState) {
        snackBarHostState = state
    }

    fun showMessage(message: String) = CoroutineScope(Dispatchers.IO).safeLaunch {
        snackBarHostState?.showSnackbar(message)
        if (isActive) cancel()
    }
}