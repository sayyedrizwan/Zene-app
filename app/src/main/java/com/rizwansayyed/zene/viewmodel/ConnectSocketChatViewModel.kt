package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.SaveParams.NEW_JOIN_USER_SOCKET
import com.rizwansayyed.zene.utils.SaveParams.NEW_MESSAGE_ON_SOCKET
import com.rizwansayyed.zene.utils.SaveParams.OLD_JOIN_USER_SOCKET
import com.rizwansayyed.zene.utils.SaveParams.USER_LEAVED_SOCKET
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_BASE_URL_SOCKET
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import kotlin.time.Duration.Companion.seconds

class ConnectSocketChatViewModel : ViewModel() {
    private var mSocket by mutableStateOf<Socket?>(null)
    private var roomId: String? = null
    private var myEmail: String? = null
    private var offJob: Job? = null
    var inLobby by mutableStateOf(false)
    var justLeft by mutableStateOf(false)
    var newIncomingMessage by mutableStateOf<ConnectChatMessageResponse?>(null)


    fun clearChatSendItem() {
        newIncomingMessage = null
    }


    fun connect(senderEmail: String) = viewModelScope.launch(Dispatchers.IO) {
        myEmail = DataStorageManager.userInfo.firstOrNull()?.email ?: return@launch
        roomId = generateRoomId(senderEmail, myEmail!!)

        inLobby = false

        try {
            val options = IO.Options.builder()
                .setReconnection(true)
                .setReconnectionDelay(15.seconds.inWholeMilliseconds)
                .build()

            mSocket = IO.socket(ZENE_BASE_URL_SOCKET, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mSocket?.on(Socket.EVENT_CONNECT) {
            val data = JSONObject()
            data.put("roomId", roomId)
            data.put("email", myEmail)
            mSocket?.emit("joinRoom", data)
        }

        mSocket?.on("oldJoin") { args ->
            val data = args.first() as JSONObject
            val email = data.getString("email")
            val type = data.getString("type")

            if (email == senderEmail && type == OLD_JOIN_USER_SOCKET) {
                offJob?.cancel()
                justLeft = false
                inLobby = true
            }
        }

        mSocket?.on("joinRoom") { args ->
            val data = args.first() as JSONObject
            val email = data.getString("email")
            val type = data.getString("type")

            if (email == senderEmail && type == NEW_JOIN_USER_SOCKET) {
                offJob?.cancel()
                justLeft = false
                inLobby = true
            }

            sendConnectMessage()
        }

        mSocket?.on("connectMessage") { args ->
            val data = args.first() as JSONObject
            val message = data.getString("message")
            val type = data.getString("type")
            val email = data.getString("email")

            if (type == NEW_MESSAGE_ON_SOCKET && email != myEmail) try {
                val json = moshi.adapter(ConnectChatMessageResponse::class.java).fromJson(message)
                newIncomingMessage = json
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mSocket?.on(Socket.EVENT_DISCONNECT) {
            inLobby = false
        }

        mSocket?.on("disconnectUser") { args ->
            val data = args.first() as JSONObject
            val email = data.getString("email")
            val type = data.getString("type")

            if (email == senderEmail && type == USER_LEAVED_SOCKET) {
                justLeft = true
                offJob?.cancel()
                offJob = CoroutineScope(Dispatchers.IO).launch {
                    delay(4.seconds)
                    inLobby = false
                    justLeft = true
                }
            }

        }

        mSocket?.connect()
    }

    fun sendMessage(item: ConnectChatMessageResponse) {
        val json = moshi.adapter(ConnectChatMessageResponse::class.java).toJson(item)
        val data = JSONObject()
        data.put("room", roomId)
        data.put("email", myEmail)
        data.put("message", json)

        mSocket!!.emit("connectMessage", data)
    }

    private fun sendConnectMessage() = viewModelScope.launch(Dispatchers.IO) {
        val data = JSONObject()
        data.put("room", roomId)
        data.put("email", myEmail)
        mSocket?.emit("oldJoin", data)
    }

    fun disconnect() {
        inLobby = false
        mSocket?.disconnect()
    }

    private fun generateRoomId(email1: String, email2: String): String {
        val sortedEmails = listOf(email1.trim().lowercase(), email2.trim().lowercase()).sorted()
        val combined = "${sortedEmails[0]}_${sortedEmails[1]}"
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(combined.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }.take(25)
    }
}