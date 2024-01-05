package com.rizwansayyed.zene.service.songparty

import android.content.Intent
import android.util.Log
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

object Utils {
    private var roomId: String? = null
    val PARTY_SERVICE_ACTION = "${context.packageManager}.PARTY_SERVICE_ACTION"

    object Free4WebSocket {
        const val FREE_4_WEB_SOCKET = "wss://rtc1.free4.chat/socket/websocket?vsn=2.0.0"
    }


    fun getRoomId() = roomId
    fun setRoomId(id: String?) {
        roomId = id
    }


    fun sendSongChangeInService(music: MusicData?) {
        val d = moshi.adapter(MusicData::class.java).toJson(music)
        Intent(PARTY_SERVICE_ACTION).apply {
            putExtra(Intent.EXTRA_TEXT, d)
            context.sendBroadcast(this)
        }
    }


    fun generateAndSendFirstTime(): String {
        if (roomId == null) roomId = generateTheRoom()
        return """
            ["2",null,"room:${roomId}","phx_join",{"isSimulcastOn":false}]
        """.trimIndent()
    }

    suspend fun sendMusicChangeData(musicData: MusicData?): String {
        val json = JSONObject().apply {
            put("name", loginUser.first()?.name)
            put("photo", loginUser.first()?.image)
            put("song_id", musicData?.pId)
            put("type", "song_change")
        }

        return """["2", null,"room:${roomId}","textEvent",{"data":"${
            json.toString().replace("\"", "\\\\\\\"")
        }"}]"""
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

    fun joinedUserDetails(json: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = getResponseInfo(json)
        if (response?.contains("\"type\":\"join\"") == true) {
            val r = JSONObject(response)
            val name = r.getString("name")
            val photo = r.getString("photo")
            val songId = r.getString("song_id")
            Log.d("TAG", "joinedUserDetails: data $name")
        }
    }

    fun songListeningSync(json: String) = CoroutineScope(Dispatchers.IO).launch {
        val response = getResponseInfo(json)
        if (response?.contains("\"type\":\"song_change\"") == true) {
            val r = JSONObject(response)
            val name = r.getString("name")
            Log.d("TAG", "joinedUserDetails: data $name")
        }
    }

    private fun getResponseInfo(json: String): String? {
        var response: String? = null
        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                response = try {
                    val element = jsonArray.getJSONObject(i)
                    val dataObject = element.getJSONObject("data")
                    val peerId = dataObject.getString("peerId")
                    val text = dataObject.getString("text").replace("\\\"", "\"")

                    text
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            response = null
        }

        return response
    }


    fun generateTheRoom(): String = runBlocking {
        return@runBlocking "the-zene-theqqaaz"


//        try {
//            val room = simpleEncode("zene_music-${loginUser.first()?.email}-${UUID.randomUUID()}")
//            room?.toCharArray()?.toMutableList()?.shuffled()?.joinToString("")
//        } catch (e: Exception) {
//            ""
//        }
    }
}