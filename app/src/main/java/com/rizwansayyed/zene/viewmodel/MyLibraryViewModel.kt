package com.rizwansayyed.zene.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.CountResponse
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.MyLibraryTypes
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.view.myplaylist.SortMyPlaylistType
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import com.rizwansayyed.zene.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MyLibraryViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var selectedType by mutableStateOf(MyLibraryTypes.HISTORY)

    fun setType(v: MyLibraryTypes) {
        selectedType = v
    }


    var historyList = mutableStateListOf<MusicHistoryResponse>()
    var historyIsLoading by mutableStateOf(false)
    private var historyPage by mutableIntStateOf(0)

    private var historyCheckJob: Job? = null
    fun songHistoryList() {
        historyCheckJob?.cancel()

        historyCheckJob = viewModelScope.safeLaunch {
            zeneAPI.getHistory(historyPage).onStart {
                historyPage += 1
                historyIsLoading = true
            }.catch {
                historyIsLoading = false
            }.collectLatest {
                historyIsLoading = false
                historyList.addAll(it)
            }
        }
    }


    var savedList = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()
    var savedIsLoading by mutableStateOf(false)
    private var savedPage by mutableIntStateOf(0)

    private var savedPlaylistCheckJob: Job? = null

    fun savedPlaylistsList() {
        savedPlaylistCheckJob?.cancel()
        savedPlaylistCheckJob = viewModelScope.safeLaunch {
            zeneAPI.getSavePlaylists(savedPage).onStart {
                savedPage += 1
                savedIsLoading = true
            }.catch {
                savedIsLoading = false
            }.collectLatest {
                savedIsLoading = false
                savedList.addAll(it)
            }
        }
    }

    var myList = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()
    var myIsLoading by mutableStateOf(false)
    private var myPage by mutableIntStateOf(0)
    var likedItemsCount by mutableStateOf<ResponseResult<CountResponse>>(ResponseResult.Empty)

    private var myPlaylistCheckJob: Job? = null

    fun myPlaylistsList(forceClean: Boolean = false) {
        myPlaylistCheckJob?.cancel()
        myPlaylistCheckJob = viewModelScope.safeLaunch {
            if (forceClean) {
                myPage = 0
                myList.clear()
            }
            zeneAPI.myPlaylists(myPage).onStart {
                myPage += 1
                myIsLoading = true
            }.catch {
                myIsLoading = false
            }.collectLatest {
                myIsLoading = false
                myList.addAll(it)
            }
        }
    }


    var userAllPlaylist by mutableStateOf<ResponseResult<SavedPlaylistsPodcastsResponse>>(
        ResponseResult.Empty
    )

    fun myAllPlaylistsList() = viewModelScope.safeLaunch {
        zeneAPI.myAllPlaylists().onStart {
            userAllPlaylist = ResponseResult.Loading
        }.catch {
            userAllPlaylist = ResponseResult.Error(it)
        }.collectLatest {
            userAllPlaylist = ResponseResult.Success(it)
        }
    }

    fun likedItemCount() = viewModelScope.safeLaunch {
        zeneAPI.likeSongsCount().onStart {
            likedItemsCount = ResponseResult.Loading
        }.catch {
            likedItemsCount = ResponseResult.Error(it)
        }.collectLatest {
            likedItemsCount = ResponseResult.Success(it)
        }
    }

    var myPlaylistSongsList = mutableStateListOf<ZeneMusicData>()
    var myPlaylistSongsIsLoading by mutableStateOf(false)
    private var myPlaylistSongsPage by mutableIntStateOf(0)

    private var myPlaylistSongsCheckJob: Job? = null
    private var myPlaylistSongsUpdateJob: Job? = null


    fun moveItem(from: Int, to: Int, id: String) {
        myPlaylistSongsList.apply {
            if (from in indices && to in indices) {
                add(to, removeAt(from))
            }
        }
        myPlaylistSongsUpdateJob?.cancel()

        myPlaylistSongsUpdateJob = viewModelScope.safeLaunch {
            delay(1.5.seconds)
            val songInfo = myPlaylistSongsList.getOrNull(to) ?: return@safeLaunch
            zeneAPI.myPlaylistsSongsReorder(songInfo, id, to).catch { }.collectLatest { }
        }
    }

    fun myPlaylistSongsViaSort(playlistID: String) = viewModelScope.safeLaunch {
        myPlaylistSongsPage = 0
        myPlaylistSongsList.clear()
        myPlaylistSongsCheckJob?.cancel()
        delay(500)
        myPlaylistSongsData(playlistID, null)
    }

    fun myPlaylistSongsData(playlistID: String, customOrder: SortMyPlaylistType?) =
        viewModelScope.safeLaunch {
        myPlaylistSongsCheckJob?.cancel()
        val email = userInfo.firstOrNull()?.email ?: ""

        if (playlistID.contains(LIKED_SONGS_ON_ZENE) && !playlistID.contains(email))
            return@safeLaunch

        myPlaylistSongsCheckJob = viewModelScope.safeLaunch {
            zeneAPI.myPlaylistsSongs(playlistID, myPlaylistSongsPage, customOrder).onStart {
                myPlaylistSongsPage += 1
                myPlaylistSongsIsLoading = true
            }.catch {
                myPlaylistSongsIsLoading = false
            }.collectLatest {
                myPlaylistSongsIsLoading = false
                myPlaylistSongsList.addAll(
                    it.mapIndexed { index, song ->
                        val newIndex = myPlaylistSongsList.size + index
                        song.copy(secId = "${song.id ?: "noid"}_$newIndex")
                    }
                )
            }
        }
    }


    var playlistInfo by mutableStateOf<ResponseResult<ZeneMusicData>>(ResponseResult.Empty)
    var isPlaylistOfSameUser by mutableStateOf(false)

    fun myPlaylistInfo(playlistID: String) = viewModelScope.safeLaunch {
        val email = userInfo.firstOrNull()?.email ?: ""
        if (playlistID.contains(LIKED_SONGS_ON_ZENE) && !playlistID.contains(email))
            return@safeLaunch

        if (playlistID.contains(LIKED_SONGS_ON_ZENE)) {
            isPlaylistOfSameUser = true
            return@safeLaunch
        }
        zeneAPI.myPlaylistInfo(playlistID).onStart {
            playlistInfo = ResponseResult.Empty
        }.catch {
            playlistInfo = ResponseResult.Loading
        }.collectLatest {
            val email = userInfo.firstOrNull()?.email
            isPlaylistOfSameUser = it.extra?.contains(email ?: "") ?: false
            playlistInfo = ResponseResult.Success(it)
        }
    }

    fun removeMyPlaylistItems(playlistID: String, data: ZeneMusicData, index: Int) =
        viewModelScope.safeLaunch {
            myPlaylistSongsList.removeAt(index)
            zeneAPI.removeMyPlaylistsSongs(playlistID, data.id ?: "", data.type).onStart {}.catch {}
                .collectLatest {}
        }

    fun deleteMyPlaylist(playlistID: String) = viewModelScope.safeLaunch {
        zeneAPI.deleteMyPlaylists(playlistID).onStart {}.catch {}.collectLatest {}
    }


    var playlistNameStatus by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)

    fun updateMyPlaylistName(playlistID: String?, title: String) =
        viewModelScope.safeLaunch {
            playlistID ?: return@safeLaunch
            zeneAPI.nameUserPlaylist(playlistID, title).onStart {
                playlistNameStatus = ResponseResult.Loading
            }.catch {
                playlistNameStatus = ResponseResult.Error(it)
            }.collectLatest {
                playlistNameStatus = ResponseResult.Success(it)
            }
        }


    var playlistImageStatus by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)
    fun updateMyPlaylistImage(id: String?, thumbnail: Uri?) =
        viewModelScope.safeLaunch {
            zeneAPI.updateImageUserPlaylist(id, thumbnail).onStart {
                playlistImageStatus = ResponseResult.Loading
            }.catch {
                playlistImageStatus = ResponseResult.Error(it)
            }.collectLatest {
                playlistImageStatus = ResponseResult.Success(it)
                delay(1.seconds)
                playlistImageStatus = ResponseResult.Empty
            }
        }


    var searchImages by mutableStateOf<ResponseResult<List<String>>>(ResponseResult.Empty)
    private var searchImageJob: Job? = null
    fun searchImage(q: String) {
        searchImageJob?.cancel()
        searchImageJob = viewModelScope.safeLaunch {
            searchImages = ResponseResult.Loading
            delay(1.seconds)
            zeneAPI.searchImages(q).onStart {
                searchImages = ResponseResult.Loading
            }.catch {
                searchImages = ResponseResult.Error(it)
            }.collectLatest {
                searchImages = ResponseResult.Success(it)
            }
        }
    }


    fun clearAll() {
        historyList.clear()
        savedList.clear()
        myList.clear()

        historyPage = 0
        savedPage = 0
        myPage = 0
    }
}