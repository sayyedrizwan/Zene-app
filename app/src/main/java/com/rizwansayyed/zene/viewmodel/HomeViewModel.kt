package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.APIHttpService.youtubeMusicPlaylist
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityItem
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.db.DataStoreManager.pinnedArtistsList
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.login.flow.LoginFlow
import com.rizwansayyed.zene.utils.Utils.internetIsConnected
import com.rizwansayyed.zene.utils.Utils.isUpdateAvailableFunction
import com.rizwansayyed.zene.utils.Utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginFlow: LoginFlow, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var loadFirstUI by mutableStateOf(false)
    var loadSecondUI by mutableStateOf(false)
    var loadThirdUI by mutableStateOf(false)

    private var lastSynced by mutableStateOf<Long?>(null)
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
    var userPlaylistsSong = mutableStateListOf<ZeneMusicDataItems>()
    var showMorePlaylistSongs by mutableStateOf(false)
    var isUserPlaylistLoading by mutableStateOf(true)
    var isAppUpdateAvailable by mutableStateOf<APIResponse<ZeneUpdateAvailabilityItem>>(APIResponse.Empty)


    fun init(force: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (lastSynced != null && !force) {
            val diffInMillis = lastSynced!! - System.currentTimeMillis()
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            if (songsYouMayLike is APIResponse.Success && minutes <= 3) return@launch
            lastSynced = System.currentTimeMillis()
        }

        appUpdateAvailable()
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

        zeneAPI.sponsorsAds()
    }

    private fun recommendedPlaylists() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            recommendedPlaylists = APIResponse.Empty
            return@launch
        }

        zeneAPI.recommendedPlaylists().onStart {
            recommendedPlaylists = APIResponse.Loading
        }.catch {
            recommendedPlaylists = APIResponse.Error(it)
        }.collectLatest {
            recommendedPlaylists = APIResponse.Success(it)
        }
    }


    private fun recommendedAlbums() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            recommendedAlbums = APIResponse.Empty
            return@launch
        }
        zeneAPI.recommendedAlbums().onStart {
            recommendedAlbums = APIResponse.Loading
        }.catch {
            recommendedAlbums = APIResponse.Error(it)
        }.collectLatest {
            recommendedAlbums = APIResponse.Success(it)
        }
    }

    private fun recommendedVideo() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            recommendedVideo = APIResponse.Empty
            return@launch
        }
        zeneAPI.recommendedVideo().onStart {
            recommendedVideo = APIResponse.Loading
        }.catch {
            recommendedVideo = APIResponse.Error(it)
        }.collectLatest {
            recommendedVideo = APIResponse.Success(it)
        }
    }

    private fun appUpdateAvailable() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            isAppUpdateAvailable = APIResponse.Empty
            return@launch
        }
        zeneAPI.updateAvailability().onStart {
            isAppUpdateAvailable = APIResponse.Loading
        }.catch {
            isAppUpdateAvailable = APIResponse.Error(it)
        }.collectLatest {
            isAppUpdateAvailable = if (it.android != null)
                APIResponse.Success(it.android)
            else
                APIResponse.Empty
        }
    }

    private fun songsYouMayLike() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            songsYouMayLike = APIResponse.Empty
            return@launch
        }
        zeneAPI.suggestTopSongs().onStart {
            songsYouMayLike = APIResponse.Loading
        }.catch {
            songsYouMayLike = APIResponse.Error(it)
        }.collectLatest {
            lastSynced = System.currentTimeMillis()
            songsYouMayLike = APIResponse.Success(it)
        }
    }

    private fun moodLists() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            moodList = APIResponse.Empty
            return@launch
        }
        zeneAPI.moodLists().onStart {
            moodList = APIResponse.Loading
        }.catch {
            moodList = APIResponse.Error(it)
        }.collectLatest {
            moodList = APIResponse.Success(it)
        }
    }

    private fun latestReleases() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            latestReleases = APIResponse.Empty
            return@launch
        }
        val id = youtubeMusicPlaylist()
        if (id == "") {
            latestReleases = APIResponse.Empty
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
        if (!internetIsConnected()) {
            topMostListeningSong = APIResponse.Empty
            return@launch
        }
        zeneAPI.topMostListeningSong().onStart {
            topMostListeningSong = APIResponse.Loading
        }.catch {
            topMostListeningSong = APIResponse.Error(it)
        }.collectLatest {
            topMostListeningSong = APIResponse.Success(it)
        }
    }

    private fun topMostListeningArtists() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            topMostListeningArtists = APIResponse.Empty
            return@launch
        }
        zeneAPI.topMostListeningArtists().onStart {
            topMostListeningArtists = APIResponse.Loading
        }.catch {
            topMostListeningArtists = APIResponse.Error(it)
        }.collectLatest {
            topMostListeningArtists = APIResponse.Success(it)
        }
    }

    private fun favArtistsData() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            favArtistsLists = APIResponse.Empty
            return@launch
        }
        zeneAPI.favArtistsData().onStart {
            favArtistsLists = APIResponse.Loading
        }.catch {
            favArtistsLists = APIResponse.Error(it)
        }.collectLatest {
            favArtistsLists = APIResponse.Success(it)
        }
    }

    private fun suggestedSongsForYou() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            suggestedSongsForYou = APIResponse.Empty
            return@launch
        }
        zeneAPI.suggestedSongs().onStart {
            suggestedSongsForYou = APIResponse.Loading
        }.catch {
            suggestedSongsForYou = APIResponse.Error(it)
        }.collectLatest {
            suggestedSongsForYou = APIResponse.Success(it)
        }
    }

    fun search(q: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            searchQuery = APIResponse.Empty
            return@launch
        }
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
            if (!internetIsConnected()) {
                searchSuggestions = APIResponse.Empty
                return@launch
            }
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
        if (!internetIsConnected()) {
            albumPlaylistData = APIResponse.Empty
            return@launch
        }
        zeneAPI.playlistAlbums(id).onStart {
            albumPlaylistData = APIResponse.Loading
        }.catch {
            albumPlaylistData = APIResponse.Error(it)
        }.collectLatest {
            albumPlaylistData = APIResponse.Success(it)
        }
    }


    fun moodPlaylists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            moodPlaylist = APIResponse.Empty
            return@launch
        }
        zeneAPI.moodLists(id).onStart {
            moodPlaylist = APIResponse.Loading
        }.catch {
            moodPlaylist = APIResponse.Error(it)
        }.collectLatest {
            moodPlaylist = APIResponse.Success(it)
        }
    }

    fun userArtistsList() = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) return@launch

        val email = userInfoDB.firstOrNull()?.email ?: ""
        if (email == "") return@launch

        zeneAPI.getUser(email).catch {}.collectLatest {
            userInfoDB = flowOf(it.toUserInfo(email))
            pinnedArtistsList = flowOf(it.pinned_artists?.filterNotNull()?.toTypedArray())
        }
    }

    fun userPlaylists(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            albumPlaylistData = APIResponse.Empty
            return@launch
        }
        zeneAPI.userPlaylistData(id).onStart {
            albumPlaylistData = APIResponse.Loading
        }.catch {
            albumPlaylistData = APIResponse.Error(it)
        }.collectLatest {
            val d = ZenePlaylistAlbumsData(it, emptyList(), false, it.artists)
            albumPlaylistData = APIResponse.Success(d)
        }
    }

    fun userPlaylistsSongs(id: String, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) userPlaylistsSong.clear()
        if (!internetIsConnected()) {
            isUserPlaylistLoading = false
            return@launch
        }

        zeneAPI.userPlaylistSongs(id, page).onStart {
            isUserPlaylistLoading = true
        }.catch {
            isUserPlaylistLoading = false
        }.collectLatest {
            isUserPlaylistLoading = false
            viewModelScope.launch(Dispatchers.IO) {
                it.forEach { v -> userPlaylistsSong.add(v) }
            }
            showMorePlaylistSongs = it.size >= 24
        }
    }

    fun removePlaylistsSongs(id: String?) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val position = userPlaylistsSong.indexOfFirst { it.id == id }
            userPlaylistsSong.removeAt(position)
        } catch (e: Exception) {
            e.message
        }
    }

    suspend fun getSongInfo(id: String): ZeneMusicDataItems? {
        return try {
            zeneAPI.songInfo(id).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getRadioInfo(id: String): ZeneMusicDataItems? {
        return try {
            zeneAPI.radioInfo(id).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
}