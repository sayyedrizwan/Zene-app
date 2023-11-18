package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick.SongKickScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation.SoundCloudImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.PlaylistItemsData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class PlaylistAlbumViewModel @Inject constructor(
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface
) : ViewModel() {

    var playlistSongsItem = mutableStateListOf<MusicData>()
        private set

    var similarAlbumPlaylistAlbum by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var playlistAlbum by mutableStateOf<DataResponse<PlaylistItemsData>>(DataResponse.Empty)
        private set

    fun playlistAlbum(id: String) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPI.albumsSearch(id).onStart {
            similarAlbumPlaylistAlbum = DataResponse.Loading
            playlistAlbum = DataResponse.Loading
        }.catch {
            playlistAlbum = DataResponse.Error(it)
        }.collectLatest {
            playlistAlbum = DataResponse.Success(it)
            searchSimilarAlbums("${it.name} - ${it.artistsName}")
            addSongsToPlaylist(it.list)
        }
    }

    private fun addSongsToPlaylist(list: List<MusicData>) = viewModelScope.launch(Dispatchers.IO) {
        playlistSongsItem.clear()
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()?.music ?: ""

        list.forEach { m ->
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val name = "${m.name} - ${m.artists}"
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
}