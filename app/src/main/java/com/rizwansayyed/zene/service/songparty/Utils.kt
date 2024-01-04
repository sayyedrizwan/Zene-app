package com.rizwansayyed.zene.service.songparty

import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.simpleEncode
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.UUID

object Utils {
    private var roomId: String? = null

    object Free4WebSocket {
        const val FREE_4_WEB_SOCKET = "wss://rtc1.free4.chat/socket/websocket?vsn=2.0.0"
    }


    fun generateAndSendFirstTime(): String {
        roomId = generateTheRoom()
        return """
            ["2",null,"room:${roomId}","phx_join",{"isSimulcastOn":false}]
        """.trimIndent()
    }

    suspend fun joinedMessage(): String {
        val json = JSONObject().apply {
            put("name", loginUser.first()?.name)
            put("photo", loginUser.first()?.image)
            put("type", "join")
        }

        return """["2", null,"room:${roomId}","textEvent",{"data":"${
            json.toString().replace("\"", "\\\\\\\"")
        }"}]"""
    }


    private fun generateTheRoom(): String = runBlocking {
        return@runBlocking try {
            val room = "${loginUser.first()?.email}-${UUID.randomUUID()}"
            simpleEncode(room).toString()
        } catch (e: Exception) {
            ""
        }
    }
}