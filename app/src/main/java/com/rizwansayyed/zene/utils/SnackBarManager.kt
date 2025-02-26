package com.rizwansayyed.zene.utils

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object SnackBarManager {
    private var snackBarHostState: SnackbarHostState? = null

    fun setHostState(state: SnackbarHostState) {
        snackBarHostState = state
    }

    fun showMessage(message: String) = CoroutineScope(Dispatchers.IO).launch {
        snackBarHostState?.showSnackbar(message)
        if (isActive) cancel()
    }
}