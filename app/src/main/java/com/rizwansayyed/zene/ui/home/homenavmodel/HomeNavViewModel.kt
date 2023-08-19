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

    var playMusicVideo = mutableStateOf("")
        private set

    fun playingVideo(song: String) {
        playMusicVideo.value = song.trim()
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


    fun doPlayer(forceStop: Boolean = false) {
        mediaPlayer.doPlayer(forceStop)
    }

    fun restartMusic() {
        mediaPlayer.restart()
    }

    fun repeatMode() {
        mediaPlayer.repeatMode()
    }

}