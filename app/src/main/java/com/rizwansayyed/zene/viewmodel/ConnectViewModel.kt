package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ConnectViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var connectSearch by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(ResponseResult.Empty)
    var connectUserInfo by mutableStateOf<ResponseResult<ConnectUserInfoResponse>>(ResponseResult.Empty)
    var connectUserList by mutableStateOf<ResponseResult<List<ConnectUserInfoResponse>>>(
        ResponseResult.Empty
    )

    fun searchConnectUsers(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchConnect(q).onStart {
            connectSearch = ResponseResult.Loading
        }.catch {
            connectSearch = ResponseResult.Error(it)
        }.collectLatest {
            connectSearch = ResponseResult.Success(it)
        }
    }

    fun connectFriendsList() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.connectFriendsList().onStart {
            if (connectUserList !is ResponseResult.Success) connectUserList = ResponseResult.Loading
        }.catch {
            connectUserList = ResponseResult.Error(it)
        }.collectLatest {
            connectUserList = ResponseResult.Success(it)
        }
    }

    fun connectUserInfo(email: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.connectUserInfo(email).onStart {
            if (connectUserInfo is ResponseResult.Empty) connectUserInfo = ResponseResult.Loading
        }.catch {
            if (connectUserInfo is ResponseResult.Loading || connectUserInfo is ResponseResult.Empty) connectUserInfo =
                ResponseResult.Error(it)
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
            zeneAPI.updateConnectSettings(
                data.user?.email ?: "",
                data.status?.lastListeningSong ?: false,
                data.status?.locationSharing ?: false,
                data.status?.silentNotification ?: false
            ).catch { }.collectLatest { }
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

    fun sendConnectMessage(email: String?, message: String) =
        viewModelScope.launch(Dispatchers.IO) {
            email ?: return@launch
            zeneAPI.sendConnectMessage(email, message).catch { }.collectLatest {
                connectUserInfo(email)
            }
        }

    fun sendConnectLocation(email: String?) = viewModelScope.launch(Dispatchers.IO) {
        email ?: return@launch
        zeneAPI.sendConnectLocation(email).catch { }.collectLatest { }
    }


    var connectFileSelected by mutableStateOf<ConnectFeedDataResponse?>(null)

    fun updateVibeFileInfo(file: File?, isVibing: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.media = file?.absolutePath
        v.isVibing = isVibing
        connectFileSelected = null
        delay(1.seconds)
        connectFileSelected = v
    }

    fun addVibeEmoji(emoji: String) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.emoji = emoji
        connectFileSelected = null
        delay(1.seconds)
        connectFileSelected = v
    }


    fun updateVibeJazzInfo(z: ZeneMusicData) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.jazzName = z.name
        v.jazzArtists = z.artists
        v.jazzId = z.id
        v.jazzThumbnail = z.thumbnail
        v.jazzType = z.type
        connectFileSelected = null
        delay(1.seconds)
        connectFileSelected = v
    }

    fun updateVibeLocationInfo(z: SearchPlacesDataResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            val v = connectFileSelected ?: ConnectFeedDataResponse()
            v.locationName = z.name
            v.locationAddress = z.address
            v.locationLongitude = z.lon
            v.locationLatitude = z.lat
            connectFileSelected = null
            delay(1.seconds)
            connectFileSelected = v
        }
}