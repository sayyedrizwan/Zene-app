package com.rizwansayyed.zene.service.songparty

import android.content.Intent
import androidx.compose.runtime.mutableStateListOf
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.GroupMusicUserInfo
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playOrPauseMedia
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PAUSE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PLAY
import com.rizwansayyed.zene.service.songparty.Utils.Action.emailParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.joinParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.nameParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.pauseParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.photoParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.playParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.songChange
import com.rizwansayyed.zene.service.songparty.Utils.Action.songIdParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.typeParam
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_CHANNEL_ID
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_DEFAULT_CHANNEL
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

object Utils {

    val groupMusicUsersList = mutableStateListOf<GroupMusicUserInfo>()

    object Free4WebSocket {
        const val FREE_4_WEB_SOCKET = "wss://rtc1.free4.chat/socket/websocket?vsn=2.0.0"
    }

    object Action {
        var partyRoomId: String? = null


        fun generatePartyRoomId(): String = runBlocking {
            return@runBlocking "the-zene-theqqaaz12222"

//        try {
//            val room = simpleEncode("zene_music-${loginUser.first()?.email}-${UUID.randomUUID()}")
//            room?.toCharArray()?.toMutableList()?.shuffled()?.joinToString("")
//        } catch (e: Exception) {
//            ""
//        }
        }

        val PARTY_ACTION_SONG_CHANGE = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_CHANGE"
        val PARTY_ACTION_SONG_PLAY = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PLAY"
        val PARTY_ACTION_SONG_PAUSE = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PAUSE"


        const val nameParam = "name"
        const val emailParam = "email"
        const val photoParam = "photo"
        const val typeParam = "type"


        const val joinParam = "join"
        const val songIdParam = "song_id"
        const val playParam = "play"
        const val pauseParam = "pause"
        const val songChange = "song_change"
    }

    object ActionFunctions {

        fun sendSongChange(id: String?) = serviceAction(PARTY_ACTION_SONG_CHANGE, id)
        fun playSongChange() = serviceAction(PARTY_ACTION_SONG_PLAY, null)
        fun pauseSongChange() = serviceAction(PARTY_ACTION_SONG_PAUSE, null)


        private fun serviceAction(action: String, extra: String?) {
            Intent(action).apply {
                if (extra != null) putExtra(Intent.EXTRA_TEXT, extra)
                context.sendBroadcast(this)
            }
        }
    }

    object ActionString {
        val joined = context.resources.getString(R.string._n_joined)
        val joinedNew = context.resources.getString(R.string._n_have_also_joined_the_song_party)

        val changed = context.resources.getString(R.string._n_changed_the_song)
        val changedNew = context.resources.getString(R.string._n_changed_the_song_to)

        val paused = context.resources.getString(R.string._n_paused_the_song)
        val play = context.resources.getString(R.string._n_start_playing_the_song)

    }


    fun isNewAJoin(json: String): Boolean {
        val response = getResponseInfo(json)

        if (typeCheck(response, joinParam)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val email = r.getString(emailParam)

            if (!groupMusicUsersList.any { it.email.trim() == name.trim() }) {
                NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                    .title(String.format(ActionString.joined, name)).image(photo)
                    .body(String.format(ActionString.joinedNew, name)).generate()

                groupMusicUsersList.add(GroupMusicUserInfo(name, photo, email))
            }
            return true
        }

        return false
    }

    fun playPauseSongStateSync(json: String) {
        val response = getResponseInfo(json)
        if (typeCheck(response, playParam) || typeCheck(response, pauseParam)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val type = r.getString(typeParam).trim()

            val getTitle = String
                .format(if (type == playParam) ActionString.play else ActionString.paused, name)

            NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                .title(getTitle).image(photo).body(getTitle).generate()

            playOrPauseMedia(type == playParam)
        }
    }


    suspend fun songPartySongSync(json: String, ytAPI: YoutubeAPIImplInterface) {
        val response = getResponseInfo(json)

        if (typeCheck(response, songChange)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val songId = r.getString(songIdParam)
            if (musicPlayerData.firstOrNull()?.v?.songID == songId) return

            ytAPI.songDetail(songId).catch { }.collectLatest {
                NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                    .title(String.format(ActionString.changed, name)).image(photo)
                    .body(String.format(ActionString.changedNew, name, it.name)).generate()
                addAllPlayer(listOf(it).toTypedArray(), 0)
            }
        }
    }

    private fun typeCheck(r: String?, type: String) =
        r?.contains("\"${typeParam}\":\"${type}\"") == true

    fun newJoin() =
        """["2",null,"room:${Action.partyRoomId}","phx_join",{"isSimulcastOn":false}]""".trimIndent()

    suspend fun sendMyDetailsInParty(): String = textDataEvents(joinParam)

    suspend fun sendMusicChangeData(songId: String?): String =
        textDataEvents(songChange, Pair(songIdParam, songId))

    suspend fun playMusicChangeData(): String = textDataEvents(playParam)
    suspend fun pauseMusicChangeData(): String = textDataEvents(pauseParam)


    private suspend fun textDataEvents(type: String, extra: Pair<String, String?>? = null): String {
        val json = JSONObject()
        json.put(nameParam, loginUser.first()?.name)
        json.put(photoParam, loginUser.first()?.image)
        json.put(emailParam, loginUser.first()?.email)
        if (extra != null) json.put(extra.first, extra.second)
        json.put(typeParam, type)
        val data = json.toString().replace("\"", "\\\\\\\"")
        return """["2", null,"room:${Action.partyRoomId}","textEvent",{"data":"$data"}]""".trimIndent()
    }


    private fun getResponseInfo(json: String): String? {
        var response: String? = null
        try {
            val jsonArray = JSONArray(json)

            for (i in 0 until jsonArray.length()) {
                response = try {
                    val element = jsonArray.getJSONObject(i).getJSONObject("data")
                    element.getString("text").replace("\\\"", "\"")
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            response = null
        }

        return response
    }

}