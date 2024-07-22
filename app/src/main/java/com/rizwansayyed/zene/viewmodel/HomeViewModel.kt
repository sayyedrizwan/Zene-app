package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.APIHttpService.youtubeMusicPlaylist
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import com.rizwansayyed.zene.utils.Utils.toast
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
    var topMostListeningArtists by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var suggestedSongsForYou by mutableStateOf<APIResponse<ZeneMusicDataResponse>>(APIResponse.Empty)
    var favArtistsLists by mutableStateOf<APIResponse<ZeneArtistsData>>(APIResponse.Empty)
    var searchQuery by mutableStateOf<APIResponse<ZeneSearchData>>(APIResponse.Empty)
    var searchSuggestions by mutableStateOf<APIResponse<List<String>>>(APIResponse.Empty)
    var albumPlaylistData by mutableStateOf<APIResponse<ZenePlaylistAlbumsData>>(APIResponse.Empty)
    var moodPlaylist by mutableStateOf<APIResponse<ZeneMoodPlaylistData>>(APIResponse.Empty)


    fun init() = viewModelScope.launch(Dispatchers.IO) {
        moodLists()
        latestReleases()
        topMostListeningSong()
        topMostListeningArtists()

        recommendedPlaylists()
        recommendedAlbums()
        recommendedVideo()
        songsYouMayLike()
        suggestedSongsForYou()
        favArtistsData()
    }

    private fun recommendedPlaylists() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedPlaylists().onStart {
            recommendedPlaylists = APIResponse.Loading
        }.catch {
            recommendedPlaylists = APIResponse.Error(it)
        }.collectLatest {
            recommendedPlaylists = APIResponse.Success(it)
        }
    }


    private fun recommendedAlbums() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedAlbums().onStart {
            recommendedAlbums = APIResponse.Loading
        }.catch {
            recommendedAlbums = APIResponse.Error(it)
        }.collectLatest {
            recommendedAlbums = APIResponse.Success(it)
        }
    }

    private fun recommendedVideo() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.recommendedVideo().onStart {
            recommendedVideo = APIResponse.Loading
        }.catch {
            recommendedVideo = APIResponse.Error(it)
        }.collectLatest {
            recommendedVideo = APIResponse.Success(it)
        }
    }

    private fun songsYouMayLike() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.suggestTopSongs().onStart {
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

    private fun topMostListeningArtists() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.topMostListeningArtists().onStart {
            topMostListeningArtists = APIResponse.Loading
        }.catch {
            topMostListeningArtists = APIResponse.Error(it)
        }.collectLatest {
            topMostListeningArtists = APIResponse.Success(it)
        }
    }

    private fun favArtistsData() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.favArtistsData().onStart {
            favArtistsLists = APIResponse.Loading
        }.catch {
            favArtistsLists = APIResponse.Error(it)
        }.collectLatest {
            favArtistsLists = APIResponse.Success(it)
        }
    }

    private fun suggestedSongsForYou() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.suggestedSongs().onStart {
            suggestedSongsForYou = APIResponse.Loading
        }.catch {
            suggestedSongsForYou = APIResponse.Error(it)
        }.collectLatest {
            suggestedSongsForYou = APIResponse.Success(it)
        }
    }

    fun search(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchData(q).onStart {
            searchQuery = APIResponse.Loading
        }.catch {
            searchQuery = APIResponse.Error(it)
        }.collectLatest {
            searchQuery = APIResponse.Success(it)
        }
    }

    fun searchSuggestions(q: String, doEmpty: Boolean = false) =
        viewModelScope.launch(Dispatchers.IO) {
            if (doEmpty) {
                searchSuggestions = APIResponse.Empty
                return@launch
            }
            zeneAPI.searchSuggestions(q).onStart {
                searchSuggestions = APIResponse.Loading
            }.catch {
                searchSuggestions = APIResponse.Error(it)
            }.collectLatest {
                searchSuggestions = APIResponse.Success(it)
            }
        }

    fun playlistsData(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playlistAlbums(id).onStart {
            albumPlaylistData = APIResponse.Loading
        }.catch {
            albumPlaylistData = APIResponse.Error(it)
        }.collectLatest {
            albumPlaylistData = APIResponse.Success(it)
        }
    }


    fun moodPlaylists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.moodLists(id).onStart {
            moodPlaylist = APIResponse.Loading
        }.catch {
            moodPlaylist = APIResponse.Error(it)
        }.collectLatest {
            moodPlaylist = APIResponse.Success(it)
        }
    }
}