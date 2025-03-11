package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
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

    fun myPlaylistsList() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.myPlaylists(myPage).onStart {
            myIsLoading = true
        }.catch {
            Log.d("TAG", "myPlaylistsList: runnned on dd ${it.message}")
            myIsLoading = false
        }.collectLatest {
            Log.d("TAG", "myPlaylistsList: runnned on ${it.size}")
            myPage += 1
            myIsLoading = false
            myList.addAll(it)
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