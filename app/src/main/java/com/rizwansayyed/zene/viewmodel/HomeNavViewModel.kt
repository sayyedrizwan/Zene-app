package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.utils.Utils.isInternetConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeNavViewModel : ViewModel() {

    var isOnline = mutableStateOf(true)
        private set


    fun checkAndSetOnlineStatus() = viewModelScope.launch(Dispatchers.IO) {
        isOnline.value = isInternetConnected()
    }
}