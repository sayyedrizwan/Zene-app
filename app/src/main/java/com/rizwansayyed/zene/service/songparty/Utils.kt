package com.rizwansayyed.zene.service.songparty

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.loginUser
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.GroupMusicUserInfo
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.service.player.utils.Utils.playOrPauseMedia
import com.rizwansayyed.zene.service.player.utils.Utils.playRadioOnPlayer
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_CLOSE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_RADIO_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PAUSE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PLAY
import com.rizwansayyed.zene.service.songparty.Utils.Action.emailParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.joinParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.leaveParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.nameParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.pauseParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.photoParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.playParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.radioChange
import com.rizwansayyed.zene.service.songparty.Utils.Action.songChange
import com.rizwansayyed.zene.service.songparty.Utils.Action.songIdParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.tempParam
import com.rizwansayyed.zene.service.songparty.Utils.Action.typeParam
import com.rizwansayyed.zene.service.songparty.Utils.ActionString.generatePartyNotification
import com.rizwansayyed.zene.utils.EncodeDecodeGlobal.simpleEncode
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_CHANNEL_ID
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.PARTY_DEFAULT_CHANNEL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

object Utils {

    val groupMusicUsersList = mutableStateListOf<GroupMusicUserInfo>()

    private var notificationJob: Job? = null

    object Free4WebSocket {
        const val FREE_4_WEB_SOCKET = "wss://rtc1.free4.chat/socket/websocket?vsn=2.0.0"
    }

    object Action {
        var partyRoomId by mutableStateOf<String?>(null)


        fun generatePartyRoomId(): String = runBlocking {
            return@runBlocking try {
                val room =
                    simpleEncode("zene_music-${loginUser.first()?.email}-${UUID.randomUUID()}")
                room.replace(Regex("\\s+"), "").toCharArray()
                    .toMutableList().shuffled().joinToString("")
            } catch (e: Exception) {
                ""
            }
        }

        val PARTY_ACTION_SONG_CHANGE = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_CHANGE"
        val PARTY_ACTION_RADIO_CHANGE =
            "${context.packageManager}.PARTY_SERVICE_ACTION_RADIO_CHANGE"
        val PARTY_ACTION_SONG_PLAY = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PLAY"
        val PARTY_ACTION_SONG_PAUSE = "${context.packageManager}.PARTY_SERVICE_ACTION_SONG_PAUSE"
        val PARTY_ACTION_CLOSE = "${context.packageManager}.PARTY_ACTION_CLOSE"


        const val nameParam = "name"
        const val emailParam = "email"
        const val photoParam = "photo"
        const val typeParam = "type"
        const val tempParam = "temps"


        const val leaveParam = "leave"
        const val joinParam = "join"
        const val songIdParam = "song_id"
        const val playParam = "play"
        const val pauseParam = "pause"
        const val songChange = "song_change"
        const val radioChange = "radio_change"
    }

    object ActionFunctions {

        fun sendSongChange(id: String?) = serviceAction(PARTY_ACTION_SONG_CHANGE, id)
        fun sendRadioChange(id: String?) = serviceAction(PARTY_ACTION_RADIO_CHANGE, id)
        fun playSongChange() = serviceAction(PARTY_ACTION_SONG_PLAY, null)
        fun pauseSongChange() = serviceAction(PARTY_ACTION_SONG_PAUSE, null)
        fun closeParty() = serviceAction(PARTY_ACTION_CLOSE, null)


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

        val left = context.resources.getString(R.string._n_left)
        val leftNew = context.resources.getString(R.string._n_has_left_the_song_party)

        val radioChange = context.resources.getString(R.string._n_changed_the_radio_channel)
        val changedRadioNew = context.resources.getString(R.string._n_changed_the_radio_channel_to)

        val changed = context.resources.getString(R.string._n_changed_the_song)
        val changedNew = context.resources.getString(R.string._n_changed_the_song_to)

        val paused = context.resources.getString(R.string._n_paused_the_song)
        val play = context.resources.getString(R.string._n_start_playing_the_song)


        fun generatePartyNotification(title: String, body: String, photo: String) {
            notificationJob?.cancel()
            notificationJob = CoroutineScope(Dispatchers.IO).launch {
                delay(1.seconds)
                NotificationViewManager(context).nIds(PARTY_CHANNEL_ID, PARTY_DEFAULT_CHANNEL)
                    .title(title).image(photo).body(body).generate()
            }
        }
    }


    suspend fun isNewAJoin(json: String): Boolean {
        val response = getResponseInfo(json)

        if (typeCheck(response, joinParam)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val email = r.getString(emailParam)

            if (email == loginUser.first()?.email) return false

            if (!groupMusicUsersList.any { it.email.trim() == email.trim() }) {
                generatePartyNotification(
                    String.format(ActionString.joined, name),
                    String.format(ActionString.joinedNew, name), photo
                )
                groupMusicUsersList.add(GroupMusicUserInfo(name, photo, email))

                return true
            }
        }

        return false
    }

    suspend fun leaveStatus(json: String) {
        val response = getResponseInfo(json)

        if (typeCheck(response, leaveParam)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val email = r.getString(emailParam)

            if (email == loginUser.first()?.email) return

            generatePartyNotification(
                String.format(ActionString.left, name),
                String.format(ActionString.leftNew, name), photo
            )
        }
    }

    suspend fun playPauseSongStateSync(json: String) {
        val response = getResponseInfo(json)
        if (typeCheck(response, playParam) || typeCheck(response, pauseParam)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val type = r.getString(typeParam).trim()
            val email = r.getString(emailParam)

            if (email == loginUser.first()?.email) return
            val getTitle = String
                .format(if (type == playParam) ActionString.play else ActionString.paused, name)

            generatePartyNotification(getTitle, getTitle, photo)
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
            val email = r.getString(emailParam)

            if (musicPlayerData.firstOrNull()?.v?.songID == songId) return
            if (email == loginUser.first()?.email) return

            ytAPI.songDetail(songId).catch { }.collectLatest {
                generatePartyNotification(
                    String.format(ActionString.changed, name),
                    String.format(ActionString.changedNew, name, it.name), photo
                )
                addAllPlayer(listOf(it).toTypedArray(), 0)
            }
        }
    }

    suspend fun songPartyRadioSync(json: String, radioAPI: OnlineRadioImplInterface) {
        val response = getResponseInfo(json)

        if (typeCheck(response, radioChange)) {
            val r = JSONObject(response!!)
            val name = r.getString(nameParam)
            val photo = r.getString(photoParam)
            val radioId = r.getString(songIdParam)
            val email = r.getString(emailParam)

            if (email == loginUser.first()?.email) return


            radioAPI.searchUuids(radioId).catch { }.collectLatest {
                it.forEach { r ->
                    generatePartyNotification(
                        String.format(ActionString.radioChange, name),
                        String.format(ActionString.changedRadioNew, name, r.name), photo
                    )
                    playRadioOnPlayer(r)
                }
            }
        }
    }

    private fun typeCheck(r: String?, type: String) =
        r?.contains("\"${typeParam}\":\"${type}\"") == true

    fun newJoin() =
        """["2",null,"room:${Action.partyRoomId}","phx_join",{"isSimulcastOn":false}]""".trimIndent()


    suspend fun sendMyDetailsInParty(): String = textDataEvents(joinParam)

    suspend fun leaveDetailsInParty(): String = textDataEvents(leaveParam)

    suspend fun sendMusicChangeData(songId: String?): String =
        textDataEvents(songChange, Pair(songIdParam, songId))

    suspend fun sendRadioChangeData(radioId: String?): String =
        textDataEvents(radioChange, Pair(songIdParam, radioId))

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