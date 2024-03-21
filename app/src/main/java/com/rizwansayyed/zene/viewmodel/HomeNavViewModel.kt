package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.HomeNavigation
import com.rizwansayyed.zene.domain.MoodData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse
import com.rizwansayyed.zene.utils.Utils.isInternetConnected
import com.rizwansayyed.zene.utils.Utils.littleVibrate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class HomeNavViewModel @Inject constructor(private val remoteConfig: RemoteConfigInterface) :
    ViewModel() {

    var isOnline by mutableStateOf(true)
        private set

    var homeNavV by mutableStateOf(HomeNavigation.HOME)
        private set

    var homeScrollPosition = mutableIntStateOf(0)
        private set

    var songDetailDialog by mutableStateOf<MusicData?>(null)
        private set

    var selectMyMusicPlaylists = mutableStateOf<SavedPlaylistEntity?>(null)
        private set

    var selectedArtists by mutableStateOf("")
        private set

    var setImageAsWallpaper by mutableStateOf("")
        private set

    var selectedAlbum by mutableStateOf("")
        private set

    var selectedMood by mutableStateOf<MoodData?>(null)
        private set

    var onlineRadioTemps by mutableStateOf<OnlineRadioResponseItem?>(null)
        private set

    var songPartyDialog = mutableStateOf<String?>(null)
        private set

    var songShareDialog = mutableStateOf<String?>(null)
        private set

    var radioShareDialog = mutableStateOf<String?>(null)
        private set

    var downloadsAppLists = mutableStateOf<RemoteConfigPresentAppDownloadResponse?>(null)
        private set


    fun setSongDetailsDialog(v: MusicData?) {
        if (v != null) littleVibrate()
        songDetailDialog = v
    }

    fun setSongPartyDialog(v: String?) {
        songPartyDialog.value = v
    }

    fun setRadioShareDialog(v: String?) {
        radioShareDialog.value = v
    }

    fun setTheMood(v: MoodData?) {
        selectedMood = v
    }

    fun setSongShareDialog(v: String?) {
        songShareDialog.value = v
    }

    fun setRadioTemp(v: OnlineRadioResponseItem?) {
        onlineRadioTemps = v
    }

    fun setSelectMyMusicPlaylists(v: SavedPlaylistEntity?) {
        selectMyMusicPlaylists.value = v
    }

    fun setHomeScrollPosition(v: Int) {
        homeScrollPosition.intValue = v
    }

    fun setImageAsWallpaper(v: String) {
        setImageAsWallpaper = v
    }

    fun setHomeNav(h: HomeNavigation) {
        homeNavV = h
    }

    fun setArtists(a: String) = viewModelScope.launch {
        if (selectedArtists.isNotEmpty()) {
            selectedArtists = ""
            delay(100.milliseconds)
        }
        selectedAlbum = ""
        selectedArtists = a
    }

    fun setAlbum(a: String) = viewModelScope.launch {
        if (selectedAlbum.isNotEmpty()) {
            selectedAlbum = ""
            delay(100.milliseconds)
        }
        selectedAlbum = a
    }

    fun checkAndSetOnlineStatus() = viewModelScope.launch(Dispatchers.IO) {
//        isOnline = isInternetConnected()
    }

    fun resetConfig() = viewModelScope.launch(Dispatchers.IO) {
        remoteConfig.config(true)
    }

    fun getAppDownloadResponse() = viewModelScope.launch(Dispatchers.IO) {
        downloadsAppLists.value = remoteConfig.downloadsAppLists()
    }
}