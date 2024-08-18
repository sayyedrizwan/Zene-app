package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.view.NavHomeMenu

class HomeNavModel : ViewModel() {

    var showMusicPlayer by mutableStateOf(false)
    var selectedMenuItems by mutableStateOf(NavHomeMenu.HOME)

    fun showMusicPlayer(b: Boolean) {
        showMusicPlayer = b
    }

    fun selectedMenuItems(n: NavHomeMenu) {
        selectedMenuItems = n
    }
}