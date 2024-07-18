package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.data.api.APIResponse

class HomeNavModel() : ViewModel() {

    var showMusicPlayer by mutableStateOf(false)

    fun showMusicPlayer(b: Boolean) {
        showMusicPlayer = b
    }
}