package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoSheetViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {
    var connectSearch by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(ResponseResult.Empty)

    fun searchConnectUsers(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchConnect(q).onStart {
            connectSearch = ResponseResult.Loading
        }.catch {
            connectSearch = ResponseResult.Error(it)
        }.collectLatest {
            connectSearch = ResponseResult.Success(it)
        }
    }
}