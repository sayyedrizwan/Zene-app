package com.rizwansayyed.zene.viewmodel

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.main.home.HomeNavSelector
import com.rizwansayyed.zene.ui.main.home.HomeSectionSelector
import com.rizwansayyed.zene.utils.ads.nativeAdsAndroidViewMap
import com.rizwansayyed.zene.utils.ads.nativeAdsMap
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.IntentFCMNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    var homeSection by mutableStateOf(HomeSectionSelector.MUSIC)
    var homeNavSection by mutableStateOf(HomeNavSelector.HOME)
    var entNavSection by mutableStateOf(EntSectionSelector.DISCOVER)
    var homeNotificationSection by mutableStateOf<IntentFCMNotification?>(null)
    var showMusicPlayer by mutableStateOf(false)
    var showMediaInfoSheet by mutableStateOf<ZeneMusicData?>(null)

    private val videoAdsPlayers = mutableStateMapOf<String, ExoPlayer>()

    @OptIn(UnstableApi::class)
    fun getPlayer(context: Context, url: String): ExoPlayer {
        val existing = videoAdsPlayers[url]
        if (existing != null) {
            if (existing.mediaItemCount == 0) {
                val mediaItem = MediaItem.fromUri(url)
                existing.setMediaItem(mediaItem)
                existing.prepare()
                existing.play()
            }
            return existing
        }

        val exo = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            volume = 0f
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
            prepare()
            play()
        }

        viewModelScope.safeLaunch(Dispatchers.Main) {
            videoAdsPlayers[url] = exo
        }

        return exo
    }

    override fun onCleared() {
        super.onCleared()
        videoAdsPlayers.values.forEach { it.release() }
    }

    fun setHomeSections(v: HomeSectionSelector) {
        nativeAdsMap.clear()
        nativeAdsAndroidViewMap.clear()
        homeSection = v
    }

    fun setMusicPlayer(v: Boolean) {
        showMusicPlayer = v
    }

    fun setHomeNavSections(v: HomeNavSelector) {
        nativeAdsMap.clear()
        nativeAdsAndroidViewMap.clear()
        homeNavSection = v
    }

    fun setHomeInfoNavigation(v: IntentFCMNotification?) {
        homeNotificationSection = v
    }

    fun setEntNavigation(v: EntSectionSelector) {
        entNavSection = v
    }

    fun setShowMediaInfo(value: ZeneMusicData?) {
        showMediaInfoSheet = value
    }

    fun updateArtistsAndAlbums(value: ZeneMusicData?) = viewModelScope.safeLaunch {
        val infos = showMediaInfoSheet
        infos?.artistsList = value?.artistsList ?: emptyList()
        infos?.albumInfo = value?.albumInfo
        showMediaInfoSheet = null
        showMediaInfoSheet = infos
    }
}