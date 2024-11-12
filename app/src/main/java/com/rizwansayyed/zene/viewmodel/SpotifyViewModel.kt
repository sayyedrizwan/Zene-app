package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.api.model.ZeneMusicImportPlaylistsItems
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.utils.Utils.internetIsConnected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpotifyViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var spotifyPlaylists = mutableStateListOf<ZeneMusicImportPlaylistsItems>()
    var isSpotifyPlaylistsLoading by mutableStateOf(false)


    fun spotifyPlaylists(token: String, path: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (!internetIsConnected()) {
            isSpotifyPlaylistsLoading = false
            return@launch
        }

        isSpotifyPlaylistsLoading = true
        try {
            val list = zeneAPI.importSpotifyPlaylists(token, path).firstOrNull()
            val nextPath = list?.firstOrNull()?.next
            if (nextPath != null) {
                isSpotifyPlaylistsLoading = true
            } else {
                isSpotifyPlaylistsLoading = false
            }

            list?.forEach { spotifyPlaylists.add(it) }
        } catch (e: Exception) {
            isSpotifyPlaylistsLoading = false
        }
    }
}