package com.rizwansayyed.zene.viewmodel

import android.net.Uri
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
import com.rizwansayyed.zene.utils.URLSUtils.LIKED_SONGS_ON_ZENE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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
        historyCheckJob = viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.getHistory(historyPage).onStart {
                historyIsLoading = true
            }.catch {
                historyIsLoading = false
            }.collectLatest {
                historyPage += 1
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
        savedPlaylistCheckJob = viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.getSavePlaylists(savedPage).onStart {
                savedIsLoading = true
            }.catch {
                savedIsLoading = false
            }.collectLatest {
                savedPage += 1
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
        myPlaylistCheckJob = viewModelScope.launch(Dispatchers.IO) {
            if (forceClean) {
                myPage = 0
                myList.clear()
            }
            zeneAPI.myPlaylists(myPage).onStart {
                myIsLoading = true
            }.catch {
                myIsLoading = false
            }.collectLatest {
                myPage += 1
                myIsLoading = false
                myList.addAll(it)
            }
        }
    }


    var userAllPlaylist by mutableStateOf<ResponseResult<SavedPlaylistsPodcastsResponse>>(
        ResponseResult.Empty
    )

    fun myAllPlaylistsList() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.myAllPlaylists().onStart {
            userAllPlaylist = ResponseResult.Loading
        }.catch {
            userAllPlaylist = ResponseResult.Error(it)
        }.collectLatest {
            userAllPlaylist = ResponseResult.Success(it)
        }
    }

    fun likedItemCount() = viewModelScope.launch(Dispatchers.IO) {
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

    fun myPlaylistSongsData(playlistID: String) = viewModelScope.launch(Dispatchers.IO) {
        myPlaylistSongsCheckJob?.cancel()
        val email = userInfo.firstOrNull()?.email ?: ""

        if (playlistID.contains(LIKED_SONGS_ON_ZENE) && !playlistID.contains(email))
            return@launch

        myPlaylistSongsCheckJob = viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.myPlaylistsSongs(playlistID, myPlaylistSongsPage).onStart {
                myPlaylistSongsIsLoading = true
            }.catch {
                myPlaylistSongsIsLoading = false
            }.collectLatest {
                myPlaylistSongsPage += 1
                myPlaylistSongsIsLoading = false
                myPlaylistSongsList.addAll(it)
            }
        }
    }


    var playlistInfo by mutableStateOf<ResponseResult<ZeneMusicData>>(ResponseResult.Empty)
    var isPlaylistOfSameUser by mutableStateOf(false)

    fun myPlaylistInfo(playlistID: String) = viewModelScope.launch(Dispatchers.IO) {
        val email = userInfo.firstOrNull()?.email ?: ""
        if (playlistID.contains(LIKED_SONGS_ON_ZENE) && !playlistID.contains(email))
            return@launch

        if (playlistID.contains(LIKED_SONGS_ON_ZENE)) {
            isPlaylistOfSameUser = true
            return@launch
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
        viewModelScope.launch(Dispatchers.IO) {
            myPlaylistSongsList.removeAt(index)
            zeneAPI.removeMyPlaylistsSongs(playlistID, data.id ?: "", data.type).onStart {}.catch {}
                .collectLatest {}
        }

    fun deleteMyPlaylist(playlistID: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.deleteMyPlaylists(playlistID).onStart {}.catch {}.collectLatest {}
    }


    var playlistNameStatus by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)

    fun updateMyPlaylistName(playlistID: String?, title: String) =
        viewModelScope.launch(Dispatchers.IO) {
            playlistID ?: return@launch
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
        viewModelScope.launch(Dispatchers.IO) {
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
        searchImageJob = viewModelScope.launch(Dispatchers.IO) {
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