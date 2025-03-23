package com.rizwansayyed.zene.ui.main.connect.utils

import android.util.Log
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_BASE_URL_SOCKET
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import kotlin.time.Duration.Companion.seconds

class ConnectChatSocketIO {
    private var mSocket: Socket? = null
    private var roomId: String? = null
    private var myEmail: String? = null

    fun connect(email: String) = CoroutineScope(Dispatchers.IO).launch {
        myEmail = DataStorageManager.userInfo.firstOrNull()?.email ?: return@launch
        roomId = generateRoomId(email, myEmail!!)

        Log.d("TAG", "connect: runnned on $roomId")

        try {
            mSocket = IO.socket(ZENE_BASE_URL_SOCKET)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mSocket?.on(Socket.EVENT_CONNECT) {
            sendConnectMessage(email)
            mSocket?.emit("joinRoom", roomId)
        }

        mSocket?.on("message") { args ->
            val data = args.first() as JSONObject
            val sender = data.getString("sender")
            val message = data.getString("message")

            if (sender == "System" && message.contains("joined the room")) {
                sendConnectMessage(email)
            }
            Log.d("Socket", "Message from $sender: $message")
        }

        mSocket?.on(Socket.EVENT_DISCONNECT) {
            Log.d("Socket", "Disconnected")
        }

        mSocket?.connect()
    }

    fun sendMessage(roomId: String, message: String) {
        val data = JSONObject()
        data.put("room", roomId)
        data.put("email", myEmail)
        data.put("message", message)
        mSocket?.emit("chatMessage", data)
    }

    private fun sendConnectMessage(roomId: String) = CoroutineScope(Dispatchers.IO).launch {
        delay(1.seconds)
        val data = JSONObject()
        data.put("room", roomId)
        data.put("email", myEmail)
        data.put("message", "just joined")
        mSocket?.emit("chatMessage", data)
    }

    fun disconnect() {
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
