package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.utils.share.IntentFCMNotification

class NavigationViewModel : ViewModel() {
    var homeSection by mutableStateOf(HomeSectionSelector.MUSIC)
    var homeNavSection by mutableStateOf(HomeNavSelector.HOME)
    var homeNotificationSection by mutableStateOf<IntentFCMNotification?>(null)
    var showMusicPlayer by mutableStateOf(false)
    var showMediaInfoSheet by mutableStateOf<ZeneMusicData?>(null)

    fun setHomeSections(v: HomeSectionSelector) {
        homeSection = v
    }

    fun setMusicPlayer(v: Boolean) {
        showMusicPlayer = v
    }

    fun setHomeNavSections(v: HomeNavSelector) {
        homeNavSection = v
    }

    fun setHomeInfoNavigation(v: IntentFCMNotification?) {
        homeNotificationSection = v
    }

    fun setShowMediaInfo(value: ZeneMusicData?) {
        showMediaInfoSheet = value
    }

    fun updateArtistsAndAlbums(value: ZeneMusicData?) {
        val infos = showMediaInfoSheet
        infos?.artistsList = value?.artistsList ?: emptyList()
        infos?.albumInfo = value?.albumInfo
        showMediaInfoSheet = null
        showMediaInfoSheet = infos
    }
}