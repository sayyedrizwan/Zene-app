package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.ui.connect_status.ConnectCommentListenerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GifViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var trendingGif by mutableStateOf<ResponseResult<List<String>>>(ResponseResult.Empty)
    var searchGif by mutableStateOf<ResponseResult<List<String>>>(ResponseResult.Empty)

    var commentLists = mutableStateListOf<VibesCommentsResponse>()
    var isLoading by mutableStateOf(false)

    fun trendingGif() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.trendingGIF().onStart {
            trendingGif = ResponseResult.Loading
        }.catch {
            trendingGif = ResponseResult.Error(it)
        }.collectLatest {
            trendingGif = ResponseResult.Success(it)
        }
    }

    fun searchGif(search: String) = viewModelScope.launch(Dispatchers.IO) {
        if (search.trim().isEmpty()) {
            searchGif = ResponseResult.Empty
            return@launch
        }
        zeneAPI.searchGif(search.trim()).onStart {
            searchGif = ResponseResult.Loading
        }.catch {
            searchGif = ResponseResult.Error(it)
        }.collectLatest {
            searchGif = ResponseResult.Success(it)
        }
    }

    fun postAGif(gif: String, id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.postCommentOnVibes(gif, id).onStart { }.catch { }.collectLatest {
            ConnectCommentListenerManager.triggerEvent()
        }
    }

    fun commentGifs(id: Int?, page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) {
            commentLists.clear()
        }
        zeneAPI.getCommentOfVibes(id, page).onStart {
            isLoading = true
        }.catch {
            isLoading = false
        }.collectLatest {
            isLoading = false
            commentLists.addAll(it)
        }
    }
}