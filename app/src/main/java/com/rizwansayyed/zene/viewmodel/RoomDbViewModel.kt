package com.rizwansayyed.zene.viewmodel


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.selectedFavouriteArtistsSongs
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.domain.ArtistsFanData
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.workmanager.OfflineDownloadManager.Companion.startOfflineDownloadWorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class RoomDbViewModel @Inject constructor(
    private val roomDBImpl: RoomDBInterface,
    private val youtubeAPIImpl: YoutubeAPIImplInterface
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            recentSixPlayedSongs()
            savedPlaylist()
            init()
            albumsYouMayLike(null)
        }
    }

    fun updateList(selectArtists: SnapshotStateList<String>) =
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                selectedFavouriteArtistsSongs = flowOf(selectArtists.toTypedArray())
            }
            delay(1.seconds)
            init()
        }

    fun init() = viewModelScope.launch(Dispatchers.IO) {
        downloadIfNotDownloaded()

        if (roomDBImpl.readRecentPlay(2).first().isNotEmpty()) {
            topTwentySongsRecords()
            artistsFansRead()
        } else if (selectedFavouriteArtistsSongs.first()?.isNotEmpty() == true)
            selectedFavouriteArtistsSongs.first()?.toList()?.let {
                songYouMayLikeArtists(it.distinct())

                if (it.distinct().size > 14)
                    artistsFans(it.distinct().subList(0, 14))
                else
                    artistsFans(it.distinct())
            }
    }

    var recentSongPlayed by mutableStateOf<Flow<List<RecentPlayedEntity>>>(flowOf(emptyList()))
        private set

    var savePlaylists by mutableStateOf<Flow<List<SavedPlaylistEntity>>>(flowOf(emptyList()))
        private set

    var songsYouMayLike by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var songsSuggestionForUsers by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
        private set

    var artistsFans by mutableStateOf<DataResponse<List<ArtistsFanData>>>(DataResponse.Empty)
        private set

    var songsSuggestionForUsersTop by mutableStateOf<DataResponse<List<List<MusicData>>>>(
        DataResponse.Empty
    )
        private set

    var artistsSuggestionForUsers by mutableStateOf<DataResponse<List<MusicData>>>(DataResponse.Empty)
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

    private fun savedPlaylist() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.savedPlaylists().onStart {
            savePlaylists = flowOf(emptyList())
        }.catch {
            savePlaylists = flowOf(emptyList())
        }.collectLatest {
            savePlaylists = it
        }
    }

    private fun topTwentySongsRecords() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.readRecentPlay(20).catch {}.collectLatest {
            songYouMayLike(it)
            songsSuggestions(it)
        }
    }


    private fun songYouMayLikeArtists(search: List<String>) =
        viewModelScope.launch(Dispatchers.IO) {
            youtubeAPIImpl.songFromArtistsTopFive(search).onStart {
                songsYouMayLike = DataResponse.Loading
            }.catch {
                songsYouMayLike = DataResponse.Error(it)
            }.collectLatest {
                songsYouMayLike = DataResponse.Success(it)
            }
        }

    private fun songYouMayLike(list: List<RecentPlayedEntity>) =
        viewModelScope.launch(Dispatchers.IO) {
            youtubeAPIImpl.topThreeSongsSuggestionOnHistory(list.map { i -> i.songId }).onStart {
                songsYouMayLike = DataResponse.Loading
            }.catch { e ->
                songsYouMayLike = DataResponse.Error(e)
            }.collectLatest { res ->
                albumsYouMayLike(res)
                songsYouMayLike = DataResponse.Success(res)
            }
        }

    private fun albumsYouMayLike(list: List<MusicData>?) = viewModelScope.launch(Dispatchers.IO) {
        val l = mutableListOf<String>()
        list?.forEach { a ->
            a.artists?.split(",", "&")?.forEach { n ->
                l.add(n.trim())
            }
        }

        youtubeAPIImpl.artistsAlbumsTopFive(l.toHashSet().toList()).onStart {
            if (albumsYouMayLike == DataResponse.Empty)
                albumsYouMayLike = DataResponse.Loading
        }.catch { e ->
            albumsYouMayLike = DataResponse.Error(e)
        }.collectLatest { res ->
            albumsYouMayLike = DataResponse.Success(res)
        }
    }

    private fun songsSuggestions(list: List<RecentPlayedEntity>) =
        viewModelScope.launch(Dispatchers.IO) {
            youtubeAPIImpl.songsSuggestionsForUsers(list.map { i -> i.songId }).onStart {
                artistsSuggestionForUsers = DataResponse.Loading
                songsSuggestionForUsersTop = DataResponse.Loading
                songsSuggestionForUsers = DataResponse.Loading
            }.catch { e ->
                artistsSuggestionForUsers = DataResponse.Error(e)
                songsSuggestionForUsers = DataResponse.Error(e)
                songsSuggestionForUsersTop = DataResponse.Error(e)
            }.collectLatest { res ->
                artistsSuggestionForUsers = DataResponse.Success(res.artists)

                if (res.related.size <= 15 || (res.next.size + res.related.size) <= 15) {
                    songsSuggestionForUsersTop =
                        DataResponse.Success((res.next + res.related).chunked(3))
                } else {
                    songsSuggestionForUsersTop =
                        DataResponse.Success(res.related.subList(0, 15).chunked(3))
                    val wholeList = res.related.subList(15, res.related.size) + res.next
                    wholeList.shuffled()
                    songsSuggestionForUsers = DataResponse.Success(wholeList)
                }
            }
        }

    private fun artistsFansRead() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.readRecentPlay(14).catch { }.collectLatest {
            val l = mutableListOf<String>()
            it.forEach { a ->
                a.artists?.split(",", "&")?.forEach { n ->
                    l.add(n.trim())
                }
            }
            artistsFans(l)
        }
    }


    private fun artistsFans(list: List<String>) = viewModelScope.launch(Dispatchers.IO) {
        youtubeAPIImpl.artistsFansItemSearch(list).onStart {
            artistsFans = DataResponse.Loading
        }.catch {
            artistsFans = DataResponse.Error(it)
        }.collectLatest {
            artistsFans = DataResponse.Success(it)
        }
    }

    fun downloadIfNotDownloaded() = viewModelScope.launch(Dispatchers.IO) {
        roomDBImpl.nonDownloadedSongs().catch { }.collectLatest {
            it.forEach { song ->
                startOfflineDownloadWorkManager(song.songId)
            }
        }
    }
}