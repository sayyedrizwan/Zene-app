package com.rizwansayyed.zene.ui.home.homenavmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeNavViewModel @Inject constructor(private val mediaPlayer: MediaPlayerObjects) :
    ViewModel() {

    var homeNavigationView = mutableStateOf(HomeNavigationStatus.MAIN)
        private set

    fun homeNavigationView(nav: HomeNavigationStatus) {
        homeNavigationView.value = nav
    }


    var showMusicPlayerView = mutableStateOf(false)
        private set

    fun hideMusicPlayer() {
        showMusicPlayerView.value = false
    }

    fun showMusicPlayer() {
        showMusicPlayerView.value = true
    }

    fun playerDuration(duration: Long) {
        mediaPlayer.setPlayerDuration(duration)
    }

    fun getPlayerDuration(): Long {
       return mediaPlayer.getPlayerDuration()
    }

}