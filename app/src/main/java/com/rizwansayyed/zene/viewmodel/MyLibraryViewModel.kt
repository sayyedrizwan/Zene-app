package com.rizwansayyed.zene.viewmodel

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

    fun songHistoryList(new: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (new) {
            historyPage = 0
            historyList.clear()
        } else {
            historyPage += 1
        }

        zeneAPI.getHistory(historyPage).onStart {
            historyIsLoading = true
        }.catch {
            historyIsLoading = true
        }.collectLatest {
            historyIsLoading = false
            historyList.addAll(it)
        }
    }


    var savedList = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()
    var savedIsLoading by mutableStateOf(false)
    private var savedPage by mutableIntStateOf(0)

    fun savedPlaylistsList(new: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        if (new) {
            savedPage = 0
            savedList.clear()
        } else {
            savedPage += 1
        }

        zeneAPI.getSavePlaylists(savedPage).onStart {
            savedIsLoading = true
        }.catch {
            savedIsLoading = true
        }.collectLatest {
            savedIsLoading = false
            savedList.addAll(it)
        }
    }
}