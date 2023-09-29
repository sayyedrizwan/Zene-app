package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.data.db.impl.RoomDBImpl
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomDbViewModel @Inject constructor(
    private val roomDBImpl: RoomDBImpl,
    private val youtubeAPIImpl: YoutubeAPIImpl
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            recentSixPlayedSongs()
            savedPlaylist()
            init()

            delay(1500)
            tempInsert()
        }
    }

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.topTenList().first().size.toast()
        if (roomDBImpl.topTenList().first().isNotEmpty())
            topTenSongsRecords()
        else if (selectedFavouriteArtistsSongs.first()?.isNotEmpty() == true)
            selectedFavouriteArtistsSongs.first()?.toList()?.let { songYouMayLike(it) }
    }

    var recentSongPlayed by mutableStateOf<Flow<List<RecentPlayedEntity>>>(flowOf(emptyList()))
        private set

    var savePlaylists by mutableStateOf<Flow<List<SavedPlaylistEntity>>>(flowOf(emptyList()))
        private set

    var songsYouMayLike by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var albumsYouMayLike by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set


    private fun recentSixPlayedSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.recentSixPlayed().onStart {
            recentSongPlayed = flowOf(emptyList())
        }.catch {
            recentSongPlayed = flowOf(emptyList())
        }.collectLatest {
            recentSongPlayed = it
        }
    }

    private fun tempInsert() = viewModelScope.launch(Dispatchers.IO) {
//
//        val insert = RecentPlayedEntity(
//            null,
//            "10,000 Hours",
//            "Dan + Shay & Justin Bieber",
//            3,
//            "M6Qo43zkDeE",
//            "https://lh3.googleusercontent.com/pGBh7nsGr5Ztbb9uW2SNHBZbGy2iFf8LlemrY4oc_CkTKSRGm5UHWuKfj11_THKqfvT8A3DoR_tUztbV_g=w847-h847-l90-rj",
//            System.currentTimeMillis(),
//            0,
//            0
//        )
//
//        roomDBImpl.insert(insert).collect()
    }

    private fun savedPlaylist() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.savedPlaylists().onStart {
            savePlaylists = flowOf(emptyList())
        }.catch {
            savePlaylists = flowOf(emptyList())
        }.collectLatest {
            savePlaylists = it
        }
    }

    private fun topTenSongsRecords() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.topTenList().catch {}.collectLatest {
            songYouMayLike(it)
        }
    }


    private fun songYouMayLike(list: List<RecentPlayedEntity>) =
        viewModelScope.launch(Dispatchers.IO) {
            youtubeAPIImpl.topTwoSongsSuggestionOnHistory(list.map { i -> i.pid }).onStart {
                songsYouMayLike = DataResponse.Loading
            }.catch { e ->
                songsYouMayLike = DataResponse.Error(e)
            }.collectLatest { res ->
                albumsYouMayLike(res)
                songsYouMayLike = DataResponse.Success(res)
            }
        }

    private fun albumsYouMayLike(list: List<MusicData>) = viewModelScope.launch(Dispatchers.IO) {
        val l = ArrayList<String>().apply {
            addAll(list.map { it.name ?: "" })
        }
        l.addAll(l.flatMap { it.split(",", "&").map { i -> i.trim() } })
        youtubeAPIImpl.artistsAlbums(l.toHashSet().toList()).onStart {
            songsYouMayLike = DataResponse.Loading
        }.catch { e ->
            songsYouMayLike = DataResponse.Error(e)
        }.collectLatest { res ->
            songsYouMayLike = DataResponse.Success(res)
        }
    }

    private fun songYouMayLike(search: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPIImpl.songFromArtistsTopFive(search).onStart {
            songsYouMayLike = DataResponse.Loading
        }.catch {
            songsYouMayLike = DataResponse.Error(it)
        }.collectLatest {
            songsYouMayLike = DataResponse.Success(it)
        }
    }
}