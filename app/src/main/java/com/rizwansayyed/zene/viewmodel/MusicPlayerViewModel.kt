package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    private var songID = ""
    private var lyricsID = ""
    private var videoDetailsID = ""
    private var storeID = ""

    var lyrics by mutableStateOf<APIResponse<ZeneLyricsData>>(APIResponse.Empty)
    var similarSongs by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var videoDetails by mutableStateOf<APIResponse<ZeneVideosMusicData>>(APIResponse.Empty)
    var storeData by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)

    fun similarSongs(songId: String) = viewModelScope.launch(Dispatchers.IO) {
        if (songId == songID) return@launch

        zeneAPI.suggestedSongs(songId).onStart {
            similarSongs = APIResponse.Loading
        }.catch {
            similarSongs = APIResponse.Error(it)
        }.collectLatest {
            songID = songId
            similarSongs = APIResponse.Success(it)
        }
    }

    fun lyrics(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        if (m.id == lyricsID) return@launch
        if (m.id == null) {
            similarSongs = APIResponse.Empty
            return@launch
        }

        zeneAPI.lyrics(m.id, m.name ?: "", m.artists ?: "").onStart {
            lyrics = APIResponse.Loading
        }.catch {
            lyrics = APIResponse.Error(it)
        }.collectLatest {
            lyricsID = m.id
            lyrics = APIResponse.Success(it)
        }
    }

    fun videoPlayerData(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        if (m.id == videoDetailsID) return@launch
        if (m.id == null) {
            similarSongs = APIResponse.Empty
            return@launch
        }

        zeneAPI.playerVideoData(m.name ?: "", m.artists ?: "").onStart {
            videoDetails = APIResponse.Loading
        }.catch {
            videoDetails = APIResponse.Error(it)
        }.collectLatest {
            videoDetailsID = m.id
            videoDetails = APIResponse.Success(it)
        }
    }

    fun storeData(m: ZeneMusicDataItems) = viewModelScope.launch(Dispatchers.IO) {
        if (m.id == storeID) return@launch
        if (m.id == null) {
            storeData = APIResponse.Empty
            return@launch
        }

        zeneAPI.merchandise(m.name ?: "", m.artists ?: "").onStart {
            storeData = APIResponse.Loading
        }.catch {
            storeData = APIResponse.Error(it)
        }.collectLatest {
            storeID = m.id
            storeData = APIResponse.Success(it)
        }
    }
}