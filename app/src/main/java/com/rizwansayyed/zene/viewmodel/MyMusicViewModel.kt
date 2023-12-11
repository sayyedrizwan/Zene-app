package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles.SubtitlesScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.data.onlinesongs.pinterest.implementation.PinterestAPIImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyMusicViewModel @Inject constructor(
    private val subtitlesScraps: SubtitlesScrapsImplInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val roomDb: RoomDBInterface,
    private val lastFMImpl: LastFMImplInterface,
    private val pinterestAPI: PinterestAPIImplInterface
) : ViewModel() {

    var recentSongPlayed by mutableStateOf<Flow<List<RecentPlayedEntity>>>(flowOf(emptyList()))
        private set

    var recentSongPlayedLoadList = mutableStateListOf<RecentPlayedEntity>()
        private set

    var recentSongPlayedLoadMore by mutableStateOf(true)
        private set

    var savePlaylists by mutableStateOf<Flow<List<SavedPlaylistEntity>>>(flowOf(emptyList()))
        private set

    var savePlaylistsLoadList = mutableStateListOf<SavedPlaylistEntity>()
        private set

    var savePlaylistsLoadMore by mutableStateOf(true)
        private set


    var offlineSongsLists by mutableStateOf<Flow<List<OfflineDownloadedEntity>>>(flowOf(emptyList()))
        private set

    var defaultPlaylistSongs by mutableIntStateOf(0)
        private set

    fun init() {
        savePlaylistsLoadList.clear()
        recentSongPlayedLoadList.clear()
        recentSongPlayedLoadMore = true
        savePlaylistsLoadMore = true
        recentPlayedSongs()
        savedPlaylist()
        defaultPlaylistSongs()
        offlineDownloadSongs()
    }

    private fun recentPlayedSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDb.recentMainPlayed().onStart {
            recentSongPlayed = flowOf(emptyList())
        }.catch {
            recentSongPlayed = flowOf(emptyList())
        }.collectLatest {
            recentSongPlayed = it
        }
    }

    fun recentPlayedLoadMore(offset: Int) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.recentPlayedList(offset).catch { }.collectLatest {
            recentSongPlayedLoadMore = it.size >= OFFSET_LIMIT

            recentSongPlayedLoadList.addAll(it)
        }
    }

    private fun savedPlaylist() = viewModelScope.launch(Dispatchers.IO) {
        roomDb.savedPlaylists().onStart {
            savePlaylists = flowOf(emptyList())
        }.catch {
            savePlaylists = flowOf(emptyList())
        }.collectLatest {
            savePlaylists = it
        }
    }

    fun savedPlaylistLoadList(offset: Int) = viewModelScope.launch(Dispatchers.IO) {
        roomDb.allCreatedPlaylists(offset).catch {}.collectLatest {
            savePlaylistsLoadMore = it.size >= OFFSET_LIMIT

            savePlaylistsLoadList.addAll(it)
        }
    }


    private fun defaultPlaylistSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDb.defaultPlaylistSongsCount().onStart {
            defaultPlaylistSongs = 0
        }.catch {
            defaultPlaylistSongs = 0
        }.collectLatest {
            defaultPlaylistSongs = it
        }
    }

    private fun offlineDownloadSongs() = viewModelScope.launch(Dispatchers.IO) {
        roomDb.offlineDownloadedSongs().onStart {
            offlineSongsLists = flowOf(emptyList())
        }.catch {
            offlineSongsLists = flowOf(emptyList())
        }.collectLatest {
            offlineSongsLists = it
        }
    }
}