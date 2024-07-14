package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIHttpService.youtubeMusicPlaylist
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginFlow: LoginFlow, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var recommendedPlaylists by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var recommendedAlbums by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var recommendedVideo by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var songsYouMayLike by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var moodList by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var latestReleases by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var topMostListeningSong by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        moodLists()
        latestReleases()
        topMostListeningSong()

        val list = listOf(
            "qqywIKDTK6o",
            "9Gduk7Zjem4",
            "0siKyXL_h08",
            "81oCkIJko5s",
            "ffqliB42Nh4",
            "gnN_jiesIkM",
            "DcDbKDAb7go",
            "sEPXrepgujY",
            "KPM_BYl-EaQ",
            "k3smYB3Nfqc"
        )
        recommendedPlaylists(list)
        recommendedAlbums(list)
        recommendedVideo(list)
        songsYouMayLike(list)
    }

    private fun recommendedPlaylists(list: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedPlaylists(list.toTypedArray()).onStart {
            recommendedPlaylists = APIResponse.Loading
        }.catch {
            recommendedPlaylists = APIResponse.Error(it)
        }.collectLatest {
            recommendedPlaylists = APIResponse.Success(it)
        }
    }


    private fun recommendedAlbums(list: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedAlbums(list.toTypedArray()).onStart {
            recommendedAlbums = APIResponse.Loading
        }.catch {
            recommendedAlbums = APIResponse.Error(it)
        }.collectLatest {
            recommendedAlbums = APIResponse.Success(it)
        }
    }

    private fun recommendedVideo(list: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedVideo(list.toTypedArray()).onStart {
            recommendedVideo = APIResponse.Loading
        }.catch {
            recommendedVideo = APIResponse.Error(it)
        }.collectLatest {
            recommendedVideo = APIResponse.Success(it)
        }
    }

    private fun songsYouMayLike(list: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.suggestTopSongs(list.toTypedArray()).onStart {
            songsYouMayLike = APIResponse.Loading
        }.catch {
            songsYouMayLike = APIResponse.Error(it)
        }.collectLatest {
            songsYouMayLike = APIResponse.Success(it)
        }
    }

    private fun moodLists() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.moodLists().onStart {
            moodList = APIResponse.Loading
        }.catch {
            moodList = APIResponse.Error(it)
        }.collectLatest {
            moodList = APIResponse.Success(it)
        }
    }

    private fun latestReleases() = viewModelScope.launch(Dispatchers.IO) {
        val id = youtubeMusicPlaylist()
        if (id == "") {
            moodList = APIResponse.Empty
            return@launch
        }

        zeneAPI.latestReleases(id).onStart {
            latestReleases = APIResponse.Loading
        }.catch {
            latestReleases = APIResponse.Error(it)
        }.collectLatest {
            latestReleases = APIResponse.Success(it)
        }
    }

    private fun topMostListeningSong() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.topMostListeningSong().onStart {
            topMostListeningSong = APIResponse.Loading
        }.catch {
            topMostListeningSong = APIResponse.Error(it)
        }.collectLatest {
            topMostListeningSong = APIResponse.Success(it)
        }
    }
}