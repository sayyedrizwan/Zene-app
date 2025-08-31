package com.rizwansayyed.zene.viewmodel

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
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.notification.NotificationUtils
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_UPDATES_NAME
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_UPDATES_NAME_DESC
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusCallbackManager
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.compressVideoFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeMediaThumbnailPreview
import com.rizwansayyed.zene.ui.connect_status.utils.compressImageHighQuality
import com.rizwansayyed.zene.ui.connect_status.utils.getMiddleVideoPreviewFrame
import com.rizwansayyed.zene.utils.MainUtils.clearImagesCache
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ConnectViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {
    var connectSearch by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(ResponseResult.Empty)
        private set

    var connectUserInfo by mutableStateOf<ResponseResult<ConnectUserInfoResponse>>(ResponseResult.Empty)
        private set

    var createPlaylist by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)
        private set

    var connectUserList by mutableStateOf<ResponseResult<List<ConnectUserInfoResponse>>>(
        ResponseResult.Empty
    )
        private set
    var connectUserFriendsList by mutableStateOf<ResponseResult<List<ConnectUserResponse>>>(
        ResponseResult.Empty
    )
        private set


    var isLoadingConnectPlaylist by mutableStateOf(false)
    var connectPlaylistsLists = mutableStateListOf<SavedPlaylistsPodcastsResponseItem>()

    var isLoadingVibeFeed by mutableStateOf(false)
    var connectUserVibesFeeds = mutableStateListOf<ConnectFeedDataResponse>()

    fun clear() = viewModelScope.safeLaunch  {
        connectUserVibesFeeds.clear()
    }

    fun searchConnectUsers(q: String) = viewModelScope.safeLaunch  {
        zeneAPI.searchConnect(q).onStart {
            connectSearch = ResponseResult.Loading
        }.catch {
            connectSearch = ResponseResult.Error(it)
        }.collectLatest {
            connectSearch = ResponseResult.Success(it)
        }
    }

    fun connectFriendsList() = viewModelScope.safeLaunch  {
        zeneAPI.connectFriendsList().onStart {
            if (connectUserList !is ResponseResult.Success) connectUserList = ResponseResult.Loading
        }.catch {
            connectUserList = ResponseResult.Error(it)
        }.collectLatest {
            connectUserList = ResponseResult.Success(it)
        }
    }

    fun connectFriendsRequestsList() = viewModelScope.safeLaunch  {
        zeneAPI.connectFriendsRequestList().onStart {
            connectUserFriendsList = ResponseResult.Loading
        }.catch {
            connectUserFriendsList = ResponseResult.Error(it)
        }.collectLatest {
            connectUserFriendsList = ResponseResult.Success(it)
        }
    }

    fun connectFriendsVibesList(page: Int) = viewModelScope.safeLaunch  {
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

    fun connectUserInfoEmpty() = viewModelScope.safeLaunch  {
        connectUserInfo = ResponseResult.Empty
    }

    fun connectUserInfo(email: String) = viewModelScope.safeLaunch  {
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
        viewModelScope.safeLaunch  {
            data.myStatus?.isConnected = if (remove) null else false
            data.didRequestToYou = false
            connectUserInfo = ResponseResult.Empty
            delay(100)
            connectUserInfo = ResponseResult.Success(data)
            data.user?.email ?: return@safeLaunch
            doRemove(data.user.email, remove, false)
        }

    fun updateSettingsStatus(data: ConnectUserInfoResponse) =
        viewModelScope.safeLaunch  {
            connectUserInfo = ResponseResult.Empty
            connectUserInfo = ResponseResult.Success(data)
            zeneAPI.updateConnectSettings(
                data.user?.email ?: "",
                data.myStatus?.lastListeningSong ?: false,
                data.myStatus?.locationSharing ?: false,
                data.myStatus?.silentNotification ?: false,
                data.expireInMinutes
            ).catch { }.collectLatest { }
        }

    fun doRemove(email: String, remove: Boolean, loadAgain: Boolean = true) =
        viewModelScope.safeLaunch  {
            zeneAPI.connectSendRequest(email, remove).catch { }.collectLatest {
                if (loadAgain) connectUserInfo(email)
            }
        }

    fun acceptConnectRequest(email: String?) = viewModelScope.safeLaunch  {
        email ?: return@safeLaunch
        zeneAPI.connectAcceptRequest(email).catch { }.collectLatest {
            connectUserInfo(email)
        }
    }

    fun sendConnectLocation(email: String?) = viewModelScope.safeLaunch  {
        email ?: return@safeLaunch
        zeneAPI.sendConnectLocation(email).catch {}.collectLatest {}
    }

    var connectFileSelected by mutableStateOf<ConnectFeedDataResponse?>(null)
    var loadingTypeForFile by mutableStateOf("")
    var isConnectSharing by mutableStateOf<ResponseResult<StatusTypeResponse>>(ResponseResult.Empty)
        private set

    fun updateCaptionInfo(caption: String) = viewModelScope.safeLaunch  {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.caption = caption
        connectFileSelected = v
    }

    fun updateVibeFileInfo(file: File?, isVibing: Boolean) = viewModelScope.safeLaunch  {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.media = file?.absolutePath
        clearImagesCache()

        if (v.media?.contains(".jpg") == true) {
            val compressed = compressImageHighQuality(
                File(v.media!!), vibeMediaThumbnailPreview, 1200, 1200, 50
            )
            v.media_thubnail = if (compressed) vibeMediaThumbnailPreview.absolutePath else v.media
        } else {
            getMiddleVideoPreviewFrame(v.media!!)?.let { l ->
                v.media_thubnail = l.absolutePath
            }
        }
        v.is_vibing = isVibing
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }

    fun addVibeEmoji(emoji: String) = viewModelScope.safeLaunch  {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.emoji = emoji
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }


    fun updateVibejamInfo(z: ZeneMusicData) = viewModelScope.safeLaunch  {
        val v = connectFileSelected ?: ConnectFeedDataResponse()
        v.jam_name = z.name
        v.jam_artists = z.artists
        v.jam_id = z.id
        v.jam_thumbnail = z.thumbnail
        v.jam_type = z.type
        connectFileSelected = null
        delay(500)
        connectFileSelected = v
    }

    fun updateVibeLocationInfo(z: SearchPlacesDataResponse) =
        viewModelScope.safeLaunch  {
            val v = connectFileSelected ?: ConnectFeedDataResponse()
            v.location_name = z.name
            v.location_address = z.address
            v.longitude = z.lon
            v.latitude = z.lat
            connectFileSelected = null
            delay(500)
            connectFileSelected = v
        }

    fun uploadAVibe() = viewModelScope.safeLaunch  {
        if ((connectFileSelected?.caption?.length ?: "".length) <= 3) return@safeLaunch
        connectFileSelected ?: return@safeLaunch
        isConnectSharing = ResponseResult.Loading
        loadingTypeForFile = context.resources.getString(R.string.compress_processing_video)

        val file = if (connectFileSelected?.media != null) {
            if (connectFileSelected?.media?.contains(".jpg") == true) connectFileSelected!!.media!!
            else compressVideoFile(connectFileSelected!!.media!!)
        } else null

        zeneAPI.shareConnectVibe(connectFileSelected!!, file, connectFileSelected?.media_thubnail)
            .onStart {
                loadingTypeForFile = context.resources.getString(R.string.uploading_please_wait)
                isConnectSharing = ResponseResult.Loading
            }.catch {
                loadingTypeForFile = ""
                isConnectSharing = ResponseResult.Error(it)
            }.collectLatest {
                loadingTypeForFile = ""
                isConnectSharing = ResponseResult.Success(it)

                CoroutineScope(Dispatchers.IO).safeLaunch {
                    delay(1.seconds)
                    if (it.status == true) {
                        NotificationUtils(
                            context.resources.getString(R.string.vibe_uploaded_successfully),
                            "\uD83D\uDE09"
                        ).channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC).generate()

                        delay(1.seconds)
                        ConnectStatusCallbackManager.triggerEvent()
                    }

                    if (isActive) cancel()
                }
            }
    }


    fun sendPartyCall(email: String?, randomCode: String) = viewModelScope.safeLaunch  {
        email ?: return@safeLaunch
        zeneAPI.sendPartyCall(email, randomCode).catch { }.collectLatest {

        }
    }


    fun createPlaylistName(email: String?, name: String) = viewModelScope.safeLaunch  {
        createPlaylist = ResponseResult.Empty
        email ?: return@safeLaunch
        zeneAPI.connectCreatePlaylists(email, name).onStart {
            createPlaylist = ResponseResult.Loading
        }.catch {
            createPlaylist = ResponseResult.Error(it)
        }.collectLatest {
            createPlaylist = ResponseResult.Success(it.status ?: false)
        }
    }


    fun connectPlaylists(email: String?, page: Int) = viewModelScope.safeLaunch  {
        email ?: return@safeLaunch
        zeneAPI.getConnectPlaylists(email, page).onStart {
            if (page == 0) connectPlaylistsLists.clear()
            isLoadingConnectPlaylist = true
        }.catch {
            isLoadingConnectPlaylist = false
        }.collectLatest {
            connectPlaylistsLists.addAll(it.toTypedArray())
            isLoadingConnectPlaylist = false
        }
    }

}