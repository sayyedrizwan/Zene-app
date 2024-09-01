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


    var lastAdRunTimestamp by mutableStateOf<Long?>(null)
    var showingWebViewAds by mutableStateOf(false)

    fun showMusicPlayer(b: Boolean) {
        showMusicPlayer = b
    }

    fun selectedMenuItems(n: NavHomeMenu) {
        selectedMenuItems = n
    }

    fun setAdsTs() {
        lastAdRunTimestamp = System.currentTimeMillis()
    }

    fun clearAdsTs() {
        lastAdRunTimestamp = null
    }

    fun webViewStatus() {
        showingWebViewAds = !showingWebViewAds
    }
}