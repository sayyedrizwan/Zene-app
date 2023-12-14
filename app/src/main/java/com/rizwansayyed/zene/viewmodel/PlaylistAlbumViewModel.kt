package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.PlaylistItemsData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlaylistAlbumViewModel @Inject constructor(
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val roomDb: RoomDBInterface,
    private val remoteConfig: RemoteConfigInterface
) : ViewModel() {

    var playlistSongsItem = mutableStateListOf<MusicData>()
        private set

    var isAlbumPresent by mutableStateOf(flowOf(0))
        private set

    var playlistSongsListSize by mutableIntStateOf(0)

    var similarAlbumPlaylistAlbum by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var playlistAlbum by mutableStateOf<DataResponse<PlaylistItemsData>>(DataResponse.Empty)
        private set

    fun playlistAlbum(id: String) = viewModelScope.launch(Dispatchers.IO) {
        isAlbumsSaved(id)
        youtubeAPI.albumsSearch(id).onStart {
            similarAlbumPlaylistAlbum = DataResponse.Loading
            playlistAlbum = DataResponse.Loading
        }.catch {
            playlistAlbum = DataResponse.Error(it)
        }.collectLatest {
            playlistSongsListSize = it.list.size
            playlistAlbum = DataResponse.Success(it)
            searchSimilarAlbums("${it.name} - ${it.artistsName}")
            addSongsToPlaylist(it.list, it.artistsName)
        }
    }

    private fun addSongsToPlaylist(list: List<MusicData>, artists: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            playlistSongsItem.clear()
            val ip = userIpDetails.first()
            val key = remoteConfig.allApiKeys()?.music ?: ""

            list.forEach { m ->
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        try {
                            val name = "${m.name} - $artists"
                            val s = youtubeAPI.musicInfoSearch(name, ip, key)
                            s?.let { playlistSongsItem.add(it) }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    private fun searchSimilarAlbums(search: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.searchData(search).onStart {
            similarAlbumPlaylistAlbum = DataResponse.Loading
        }.catch {
            similarAlbumPlaylistAlbum = DataResponse.Error(it)
        }.collectLatest {
            similarAlbumPlaylistAlbum = DataResponse.Success(it.albums.subList(1, it.albums.size))
        }
    }

    private fun isAlbumsSaved(albumId: String) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.isAlbums(albumId).catch { }.collectLatest {
            isAlbumPresent = it
        }
    }


    fun saveAlbumsLocally(item: PlaylistItemsData, albumId: String, rm: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            roomDb.deleteAlbums(albumId).first()
            if (!rm) {
                val i = SavedPlaylistEntity(
                    null, item.name ?: "", System.currentTimeMillis(),
                    item.thumbnail, playlistSongsListSize, albumId
                )
                roomDb.insert(i).collect()
            }
        }
}