package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation.SpotifyUsersAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaylistImportViewModel @Inject constructor(
    private val spotifyUserAPI: SpotifyUsersAPIImplInterface
) : ViewModel() {


    var usersPlaylists by mutableStateOf<DataResponse<SpotifyUserPlaylistResponse>>(DataResponse.Empty)
        private set


    fun spotifyPlaylistInfo() = viewModelScope.launch(Dispatchers.IO) {
        spotifyUserAPI.usersPlaylist().onStart {
            usersPlaylists = DataResponse.Loading
        }.catch {
            usersPlaylists = DataResponse.Error(it)
        }.collectLatest {
            usersPlaylists = DataResponse.Success(it)
        }
    }

}