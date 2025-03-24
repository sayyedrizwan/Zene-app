package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.compressVideoFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectChatViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) :
    ViewModel() {

    var recentChatItems = mutableStateListOf<ConnectChatMessageResponse>()
    var isRecentChatLoading by mutableStateOf(false)
    var sendConnectMessageLoading by mutableStateOf(false)

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
                    System.currentTimeMillis(), gif = gif
                )

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
                        it.message, myEmail, email, false, "", System.currentTimeMillis(),
                        it.media, it.thumbnail
                    )
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