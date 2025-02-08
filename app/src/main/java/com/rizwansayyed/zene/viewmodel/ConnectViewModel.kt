package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.compressVideoFile
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.CONNECT_UPDATES_NAME
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.CONNECT_UPDATES_NAME_DESC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var connectSearch by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(ResponseResult.Empty)
    var connectUserInfo by mutableStateOf<ResponseResult<ConnectUserInfoResponse>>(ResponseResult.Empty)
    var connectUserList by mutableStateOf<ResponseResult<List<ConnectUserInfoResponse>>>(
        ResponseResult.Empty
    )
    var connectUserFriendsList by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(
        ResponseResult.Empty
    )


    var isLoadingVibeFeed by mutableStateOf(false)
    var connectUserVibesFeeds = mutableStateListOf<ConnectFeedDataResponse>()

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

    fun connectFriendsRequestsList() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.connectFriendsRequestList().onStart {
            connectUserFriendsList = ResponseResult.Loading
        }.catch {
            connectUserFriendsList = ResponseResult.Error(it)
        }.collectLatest {
            connectUserFriendsList = ResponseResult.Success(it)
        }
    }

    fun connectFriendsVibesList(page: Int) = viewModelScope.launch(Dispatchers.IO) {
        if (page == 0) connectUserVibesFeeds.clear()
        zeneAPI.connectFriendsVibesList(page).onStart {
            isLoadingVibeFeed = true
        }.catch {
            isLoadingVibeFeed = false
        }.collectLatest {
            isLoadingVibeFeed = false
            connectUserVibesFeeds.addAll(it.toTypedArray())
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
    var loadingTypeForFile by mutableStateOf("")
    var isConnectSharing by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)

    fun updateCaptionInfo(caption: String) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.caption = caption
        connectFileSelected = v
    }

    fun updateVibeFileInfo(file: File?, isVibing: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.media = file?.absolutePath
        v.isVibing = isVibing
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }

    fun addVibeEmoji(emoji: String) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.emoji = emoji
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }


    fun updateVibeJazzInfo(z: ZeneMusicData) = viewModelScope.launch(Dispatchers.IO) {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.jazz_name = z.name
        v.jazz_artists = z.artists
        v.jazz_id = z.id
        v.jazz_thumbnail = z.thumbnail
        v.jazz_type = z.type
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }

    fun updateVibeLocationInfo(z: SearchPlacesDataResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            val v = connectFileSelected ?: ConnectFeedDataResponse()
            v.location_name = z.name
            v.location_address = z.address
            v.longitude = z.lon
            v.latitude = z.lat
            connectFileSelected = null
            delay(500)
            connectFileSelected = v
        }

    fun uploadAVibe() = viewModelScope.launch(Dispatchers.IO) {
        if ((connectFileSelected?.caption?.length ?: "".length) <= 3) return@launch
        connectFileSelected ?: return@launch
        isConnectSharing = ResponseResult.Loading
        loadingTypeForFile = context.resources.getString(R.string.compress_processing_video)

        val file = if (connectFileSelected?.media != null) {
            if (connectFileSelected?.media?.contains(".jpg") == true) connectFileSelected!!.media!!
            else compressVideoFile(connectFileSelected!!.media!!)
        } else null

        zeneAPI.shareConnectVibe(connectFileSelected!!, file).onStart {
            loadingTypeForFile = context.resources.getString(R.string.uploading_please_wait)
            isConnectSharing = ResponseResult.Loading
        }.catch {
            loadingTypeForFile = ""
            isConnectSharing = ResponseResult.Error(it)
        }.collectLatest {
            loadingTypeForFile = ""
            isConnectSharing = ResponseResult.Success(it)

            if (it.status == true) {
                NotificationUtils(
                    context.resources.getString(R.string.vibe_uploaded_successfully), "\uD83D\uDE09"
                ).channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC).generate()
            }
        }
    }
}