package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
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

    val selectArtists = mutableStateListOf<String>()


    fun checkAndSetOnlineStatus() = viewModelScope.launch(Dispatchers.IO) {
        isOnline.value = isInternetConnected()

    }

    fun resetConfig() = viewModelScope.launch(Dispatchers.IO) {
        remoteConfig.config(true)
    }

    fun selectedArtists(name: String) {
        if (name.isEmpty()) return

        if (selectArtists.contains(name.lowercase())){
            selectArtists.remove(name.lowercase())
            return
        }
        selectArtists.add(name.lowercase())
    }
}