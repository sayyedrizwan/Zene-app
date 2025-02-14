package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var videoSimilarVideos by
    mutableStateOf<ResponseResult<Pair<String, ZeneMusicDataList>>>(ResponseResult.Empty)

    fun similarVideos(id: String) = viewModelScope.launch(Dispatchers.IO) {
        when (val v = videoSimilarVideos) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> {}
            is ResponseResult.Success -> {
                if (v.data.first == id && v.data.second.isNotEmpty()) return@launch
            }
        }

        zeneAPI.similarVideos(id).onStart {
            videoSimilarVideos = ResponseResult.Loading
        }.catch {
            videoSimilarVideos = ResponseResult.Error(it)
        }.collectLatest {
            videoSimilarVideos = ResponseResult.Success(Pair(id, it))
        }
    }
}