package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.HomeNavigation
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.utils.Utils.isInternetConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNavViewModel @Inject constructor(private val remoteConfig: RemoteConfigInterface) :
    ViewModel() {

    var isOnline = mutableStateOf(true)
        private set

    var homeNavV by mutableStateOf(HomeNavigation.HOME)
        private set

    var homeScrollPosition = mutableIntStateOf(0)
        private set

    var songDetailDialog by mutableStateOf<MusicData?>(null)
        private set

    fun setSongDetailsDialog(v: MusicData?) {
        songDetailDialog = v
    }

    fun setHomeScrollPosition(v: Int) {
        homeScrollPosition.intValue = v
    }

    fun setHomeNav(h: HomeNavigation) {
        homeNavV = h
    }

    fun checkAndSetOnlineStatus() = viewModelScope.launch(Dispatchers.IO) {
        isOnline.value = isInternetConnected()
    }

    fun resetConfig() = viewModelScope.launch(Dispatchers.IO) {
        remoteConfig.config(true)
    }
}