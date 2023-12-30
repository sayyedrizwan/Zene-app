package com.rizwansayyed.zene.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation.SpotifyUsersAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.asMusicPlayerList
import com.rizwansayyed.zene.domain.toPlaylistInfo
import com.rizwansayyed.zene.domain.toTrack
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaylistImportViewModel @Inject constructor(
    private val spotifyUserAPI: SpotifyUsersAPIImplInterface,
    private val youtubeAPIImpl: YoutubeAPIImplInterface,
) : ViewModel() {

    private var playlistTrackJob: Job? = null

    var selectedPlaylist by mutableStateOf<ImportPlaylistInfoData?>(null)

    var songMenu by mutableStateOf<DataResponse<MusicData?>>(DataResponse.Empty)

    var playlistTrackers = mutableStateListOf<ImportPlaylistTrackInfoData>()

    var usersPlaylists by mutableStateOf<DataResponse<List<ImportPlaylistInfoData>>>(DataResponse.Empty)
        private set


    fun spotifyPlaylistInfo() = viewModelScope.launch(Dispatchers.IO) {
        spotifyUserAPI.usersPlaylist().onStart {
            usersPlaylists = DataResponse.Loading
        }.catch {
            usersPlaylists = DataResponse.Error(it)
        }.collectLatest {
            playlistTrackers.clear()
            val list = it.toPlaylistInfo(PlaylistImportersType.SPOTIFY) ?: emptyList()
            usersPlaylists = DataResponse.Success(list)

            list.forEachIndexed { i, item ->
                if (i == 0) {
                    spotifyPlaylistTrack(item)
                }
            }
        }
    }

    fun spotifyPlaylistTrack(item: ImportPlaylistInfoData) = viewModelScope.launch(Dispatchers.IO) {
        if (selectedPlaylist == item) return@launch
        selectedPlaylist = item
        playlistTrackJob?.cancel()
        playlistTrackJob = CoroutineScope(Dispatchers.IO).launch {
            playlistTrackers.clear()

            try {
                val lists = spotifyUserAPI.playlistTrack(item.id ?: "", 0).first()
                lists.items?.forEach {
                    it?.toTrack()?.let { t -> playlistTrackers.add(t) }
                }

                var page = 0

                repeat(((lists.total ?: 0) / 50)) {
                    if (page > 0) {
                        val listsOffset =
                            spotifyUserAPI.playlistTrack(item.id ?: "", page * 50).first()
                        listsOffset.items?.forEach {
                            it?.toTrack()?.let { t -> playlistTrackers.add(t) }
                        }
                    }
                    page += 1
                }

            } catch (e: Exception) {
                e.message
            }
        }
    }

    fun searchSongForPlaylist(n: String, menu: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPIImpl.musicInfoSearch(n).onStart {
            songMenu = DataResponse.Loading
        }.catch {
            songMenu = DataResponse.Error(it)
        }.collectLatest {
            if (menu) songMenu = DataResponse.Success(it)
            else addAllPlayer(listOf(it).toTypedArray(), 0)
        }
    }

    fun clear() = viewModelScope.launch(Dispatchers.IO) {
        songMenu = DataResponse.Empty
    }

}