package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.compressVideoFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConnectChatViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) :
    ViewModel() {

    var recentChatItems = mutableStateListOf<ConnectChatMessageResponse>()
    var isRecentChatLoading by mutableStateOf(false)
    var sendConnectMessageLoading by mutableStateOf(false)
    var recentChatItemsToSend by mutableStateOf<ConnectChatMessageResponse?>(null)

    fun clearChatSendItem() {
        recentChatItemsToSend = null
    }

    fun addANewItemChat(message: ConnectChatMessageResponse) {
        recentChatItems.add(0, message)
    }

    fun removeANewItemChat(email: String?, id: String?, isMine: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            val position = recentChatItems.indexOfFirst { it._id == id }
            recentChatItems.removeAt(position)

            if (!isMine) return@launch

            zeneAPI.deleteConnectMessage(email, id).catch {}.collectLatest { }
        }

    fun sendConnectMessage(email: String?, message: String, gif: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            email ?: return@launch
            zeneAPI.sendConnectMessage(email, message, gif).onStart {
                sendConnectMessageLoading = true
            }.catch {
                sendConnectMessageLoading = false
            }.collectLatest {
                sendConnectMessageLoading = false
                if (it.status == false) return@collectLatest

                val myEmail = DataStorageManager.userInfo.firstOrNull()?.email
                val data = ConnectChatMessageResponse(
                    it.message, myEmail, email, false, message.trim(),
                    it.ts ?: System.currentTimeMillis(), gif = gif,
                    expire_at = it.expire
                )
                recentChatItemsToSend = data
                recentChatItems.add(0, data)
            }
        }


    fun sendConnectJamMessage(email: String?, musicData: ZeneMusicData?) =
        viewModelScope.launch(Dispatchers.IO) {
            email ?: return@launch
            musicData ?: return@launch
            zeneAPI.sendConnectJamMessage(email, musicData).onStart {
                sendConnectMessageLoading = true
            }.catch {
                sendConnectMessageLoading = false
            }.collectLatest {
                sendConnectMessageLoading = false
                if (it.status == false) return@collectLatest

                val myEmail = DataStorageManager.userInfo.firstOrNull()?.email
                val data = ConnectChatMessageResponse(
                    it.message, myEmail, email, false, "",
                    it.ts ?: System.currentTimeMillis(), jam_name = musicData.name,
                    jam_artists = musicData.artists, jam_id = musicData.id,
                    jam_type = musicData.type, jam_thumbnail = musicData.thumbnail,
                    expire_at = it.expire
                )

                recentChatItemsToSend = data
                recentChatItems.add(0, data)
            }
        }


    fun sendFileMessage(email: String?, file: File?) = viewModelScope.launch(Dispatchers.IO) {
        email ?: return@launch
        file ?: return@launch
        zeneAPI.sendConnectFileMessage(email, file).onStart {
            sendConnectMessageLoading = true
        }.catch {
            sendConnectMessageLoading = false
        }.collectLatest {
            sendConnectMessageLoading = false
            if (it.status == false) return@collectLatest

            val myEmail = DataStorageManager.userInfo.firstOrNull()?.email
            val data = ConnectChatMessageResponse(
                it.message, myEmail, email, false, "",
                it.ts ?: System.currentTimeMillis(), file_path = it.media,
                file_name = file.name, file_size = file.length().toString(),
                expire_at = it.expire
            )

            recentChatItemsToSend = data
            recentChatItems.add(0, data)
        }
    }

    fun sendImageVideo(email: String?, file: String?, thumbnail: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            email ?: return@launch
            sendConnectMessageLoading = true

            val finalFile = if (file != null) {
                if (file.contains(".jpg")) file else compressVideoFile(file)
            } else null

            if (finalFile != null) {
                sendConnectMessageLoading = true

                zeneAPI.sendConnectMediaMessage(email, file, thumbnail).catch {
                    sendConnectMessageLoading = false
                }.collectLatest {
                    sendConnectMessageLoading = false

                    if (it.status == false) return@collectLatest
                    val myEmail = DataStorageManager.userInfo.firstOrNull()?.email

                    val data = ConnectChatMessageResponse(
                        it.message, myEmail, email, false, "",
                        it.ts ?: System.currentTimeMillis(),
                        it.expire, it.media, it.thumbnail,
                    )

                    recentChatItemsToSend = data
                    recentChatItems.add(0, data)
                }
            } else {
                sendConnectMessageLoading = false
            }
        }

    fun markConnectMessageToRead(email: String?) = viewModelScope.launch(Dispatchers.IO) {
        email ?: return@launch
        zeneAPI.markConnectMessageToRead(email).catch { }.collectLatest { }
    }

    fun getChatConnectRecentMessage(email: String?, new: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            email ?: return@launch

            if (new) recentChatItems.clear()

            val lastID = recentChatItems.lastOrNull()?._id
            zeneAPI.getChatConnectRecentMessage(email, lastID).onStart {
                isRecentChatLoading = true
            }.catch {
                isRecentChatLoading = false
            }.collectLatest {
                isRecentChatLoading = false
                it.forEach { v ->
                    if (!recentChatItems.any { c -> c._id == v._id }) recentChatItems.add(v)
                }
            }
        }
}