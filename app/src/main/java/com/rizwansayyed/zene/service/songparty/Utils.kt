package com.rizwansayyed.zene.service.songparty

import android.content.Intent
import android.util.Log
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playOrPauseMedia
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_CHANNEL_ID
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_DEFAULT_CHANNEL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

object Utils {
    private var roomId: String? = null
    val PARTY_SERVICE_ACTION_SONG_CHANGE =
        "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_CHANGE"
    val PARTY_SERVICE_ACTION_SONG_PLAY = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PLAY"
    val PARTY_SERVICE_ACTION_SONG_PAUSE =
        "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PAUSE"

    object Free4WebSocket {
        const val FREE_4_WEB_SOCKET = "wss://rtc1.free4.chat/socket/websocket?vsn=2.0.0"
    }


    fun getRoomId() = roomId
    fun setRoomId(id: String?) {
        roomId = id
    }


    fun sendSongChangeInService(music: MusicData?) {
        val d = moshi.adapter(MusicData::class.java).toJson(music)
        Intent(PARTY_SERVICE_ACTION_SONG_CHANGE).apply {
            putExtra(Intent.EXTRA_TEXT, d)
            context.sendBroadcast(this)
        }
    }

    fun playSongChangeInService() {
        Intent(PARTY_SERVICE_ACTION_SONG_PLAY).apply {
            context.sendBroadcast(this)
        }
    }


    fun pauseSongChangeInService() {
        Intent(PARTY_SERVICE_ACTION_SONG_PAUSE).apply {
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

    suspend fun playMusicChangeData(): String {
        val json = JSONObject().apply {
            put("name", loginUser.first()?.name)
            put("photo", loginUser.first()?.image)
            put("type", "play")
        }

        return """["2", null,"room:${roomId}","textEvent",{"data":"${
            json.toString().replace("\"", "\\\\\\\"")
        }"}]"""
    }

    suspend fun pauseMusicChangeData(): String {
        val json = JSONObject().apply {
            put("name", loginUser.first()?.name)
            put("photo", loginUser.first()?.image)
            put("type", "pause")
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

    fun joinedUserDetails(json: String) {
        val response = getResponseInfo(json)
        if (response?.contains("\"type\":\"join\"") == true) {
            val r = JSONObject(response)
            val name = r.getString("name")
            val photo = r.getString("photo")

            val joined = context.resources.getString(R.string._n_joined)
            val joinedNew = context.resources.getString(R.string._n_have_also_joined_the_song_party)

            NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                .title(String.format(joined, name)).image(photo)
                .body(String.format(joinedNew, name)).generate()
        }
    }

    suspend fun songListeningSync(json: String, ytAPI: YoutubeAPIImplInterface) {
        val response = getResponseInfo(json)
        if (response?.contains("\"type\":\"song_change\"") == true) {
            val changed = context.resources.getString(R.string._n_changed_the_song)
            val changedNew = context.resources.getString(R.string._n_changed_the_song_to)


            val r = JSONObject(response)
            val name = r.getString("name")
            val photo = r.getString("photo")
            val songId = r.getString("song_id")
            if (musicPlayerData.firstOrNull()?.v?.songID == songId) return

            ytAPI.songDetail(songId).catch { }.collectLatest {
                NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                    .title(String.format(changed, name)).image(photo)
                    .body(String.format(changedNew, name, it.name)).generate()
                addAllPlayer(listOf(it).toTypedArray(), 0)
            }
        }
    }

    fun playPausePartySync(json: String) {
        val response = getResponseInfo(json)
        if (response?.contains("\"type\":\"pause\"") == true ||
            response?.contains("\"type\":\"play\"") == true
        ) {
            val paused = context.resources.getString(R.string._n_paused_the_song)
            val play = context.resources.getString(R.string._n_start_playing_the_song)

            val r = JSONObject(response)
            val name = r.getString("name")
            val photo = r.getString("photo")
            val type = r.getString("type").trim()

            NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                .title(String.format(if (type == "play") play else paused, name)).image(photo)
                .body(String.format(if (type == "play") play else paused, play)).generate()

            playOrPauseMedia(type == "play")
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