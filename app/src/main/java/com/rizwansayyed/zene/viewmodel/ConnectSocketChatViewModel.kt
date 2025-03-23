package com.rizwansayyed.zene.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.SaveParams.NEW_JOIN_USER_SOCKET
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
    private var mSocket: Socket? = null
    private var roomId: String? = null
    private var myEmail: String? = null
    var offJob: Job? = null
    var inLobby by mutableStateOf(false)
    var justLeft by mutableStateOf(false)


    fun connect(senderEmail: String) = CoroutineScope(Dispatchers.IO).launch {
        myEmail = DataStorageManager.userInfo.firstOrNull()?.email ?: return@launch
        roomId = generateRoomId(senderEmail, myEmail!!)

        inLobby = false

        try {
            mSocket = IO.socket(ZENE_BASE_URL_SOCKET)
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

        mSocket?.on("message") { args ->
            return@on
            val data = args.first() as JSONObject
            val sender = data.getString("sender")
            val email = data.getString("email")
            val message = data.getString("message")

            if (sender == "System" && message.contains("new joined the room") && email == senderEmail) {
                inLobby = true
                Log.d("Socket", "connect: runnned on $roomId")
                sendConnectMessage()
            }
            if (sender == "System" && message.contains("left the room") && email == senderEmail) {
                inLobby = false
            }
            Log.d("Socket", "Message from $sender: ${email} $message")
            Log.d(
                "Socket",
                "Message from ${sender == "System"}: ${message.contains("new joined the room")} ${email == senderEmail} "
            )
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
                offJob = viewModelScope.launch {
                    delay(4.seconds)
                    inLobby = false
                }
            }
        }

        mSocket?.connect()
    }

    fun sendMessage(message: String) {
        val data = JSONObject()
        data.put("room", roomId)
        data.put("email", myEmail)
        data.put("message", message)
        mSocket?.emit("chatMessage", data)
    }

    private fun sendConnectMessage() = CoroutineScope(Dispatchers.IO).launch {
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