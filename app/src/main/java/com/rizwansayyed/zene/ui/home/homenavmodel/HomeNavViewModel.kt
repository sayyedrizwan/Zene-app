package com.rizwansayyed.zene.ui.home.homenavmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerObjects
import com.rizwansayyed.zene.ui.musicplay.video.VideoPlayerViewStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@HiltViewModel
class HomeNavViewModel @Inject constructor(private val mediaPlayer: MediaPlayerObjects) :
    ViewModel() {

    var homeNavigationView = mutableStateOf(HomeNavigationStatus.MAIN)
        private set

    fun homeNavigationView(nav: HomeNavigationStatus) {
        hideMusicPlayer()
        homeNavigationView.value = nav
    }

    var homeScrollPosition by mutableIntStateOf(0)
        private set

    var homeScrollOffSet by mutableIntStateOf(0)
        private set

    fun homeScrollPosition(i: Int, offset: Int) {
        homeScrollPosition = i
        homeScrollOffSet = offset
    }

    var musicViewType = mutableStateOf(VideoPlayerViewStatus.MUSIC)
        private set

    fun musicViewType(view: VideoPlayerViewStatus) {
        musicViewType.value = view
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