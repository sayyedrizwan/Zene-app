package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var connectSearch by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(ResponseResult.Empty)
    var connectUserInfo by mutableStateOf<ResponseResult<ConnectUserInfoResponse>>(ResponseResult.Empty)

    fun searchConnectUsers(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchConnect(q).onStart {
            connectSearch = ResponseResult.Loading
        }.catch {
            connectSearch = ResponseResult.Error(it)
        }.collectLatest {
            connectSearch = ResponseResult.Success(it)
        }
    }

    fun connectUserInfo(email: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.connectUserInfo(email).onStart {
            connectUserInfo = ResponseResult.Loading
        }.catch {
            connectUserInfo = ResponseResult.Error(it)
        }.collectLatest {
            connectUserInfo = ResponseResult.Success(it)
        }
    }

    fun updateAddStatus(data: ConnectUserInfoResponse, remove: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            connectUserInfo = ResponseResult.Empty
            data.status?.isConnected = if (remove) null else false
            data.didRequestToYou = false
            connectUserInfo = ResponseResult.Success(data)
            data.user?.email ?: return@launch
            doRemove(data.user.email, remove, false)
        }

    fun updateSettingsStatus(data: ConnectUserInfoResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            connectUserInfo = ResponseResult.Empty
            connectUserInfo = ResponseResult.Success(data)
        }

    fun doRemove(email: String, remove: Boolean, loadAgain: Boolean = true) =
        viewModelScope.launch(Dispatchers.IO) {
            zeneAPI.connectSendRequest(email, remove).catch { }.collectLatest {
                if (loadAgain) connectUserInfo(email)
            }
        }

    fun acceptConnectRequest(email: String?) = viewModelScope.launch(Dispatchers.IO) {
        email ?: return@launch
        zeneAPI.connectAcceptRequest(email).catch { }.collectLatest {
            connectUserInfo(email)
        }
    }
}