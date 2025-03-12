package com.rizwansayyed.zene.viewmodel

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
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.data.model.ZeneMusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLibraryViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var historyList = mutableStateListOf<MusicHistoryResponse>()
    var historyIsLoading by mutableStateOf(false)
    private var historyPage by mutableIntStateOf(0)

    fun songHistoryList() = viewModelScope.launch(Dispatchers.IO) {
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


    var savedList = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()
    var savedIsLoading by mutableStateOf(false)
    private var savedPage by mutableIntStateOf(0)

    fun savedPlaylistsList() = viewModelScope.launch(Dispatchers.IO) {
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


    var myList = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()
    var myIsLoading by mutableStateOf(false)
    private var myPage by mutableIntStateOf(0)
    var likedItemsCount by mutableStateOf<ResponseResult<CountResponse>>(ResponseResult.Empty)

    fun myPlaylistsList() = viewModelScope.launch(Dispatchers.IO) {
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

    fun myPlaylistSongsData(playlistID: String) = viewModelScope.launch(Dispatchers.IO) {
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

    fun removeMyPlaylistItems(playlistID: String, data: ZeneMusicData, index: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            myPlaylistSongsList.removeAt(index)
            zeneAPI.removeMyPlaylistsSongs(playlistID, data.id ?: "", data.type)
                .onStart {}.catch {}.collectLatest {}
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