package com.rizwansayyed.zene.ui.partycall

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.notification.NotificationUtils
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_UPDATES_NAME
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_UPDATES_NAME_DESC
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_BASE_URL_SOCKET
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject
import kotlin.time.Duration.Companion.seconds

class PartySongSocketModel : ViewModel() {
    private var mSocket by mutableStateOf<Socket?>(null)
    private var roomID by mutableStateOf("")

    private var authToken: String? = null
    private var myEmail: String? = null
    private var myName: String? = null
    private var myProfilePhoto: String? = null
    private var checkJob: Job? = null
    private var lastSongID: String = ""

    fun connect(id: String) = viewModelScope.safeLaunch  {
        roomID = "${id}_party_sync"
        authToken = DataStorageManager.userInfo.firstOrNull()?.authToken ?: return@safeLaunch
        myEmail = DataStorageManager.userInfo.firstOrNull()?.email ?: return@safeLaunch
        myName = DataStorageManager.userInfo.firstOrNull()?.name ?: return@safeLaunch
        myProfilePhoto = DataStorageManager.userInfo.firstOrNull()?.photo ?: return@safeLaunch

        try {
            val options = IO.Options.builder()
                .setReconnection(true)
                .setPath(BuildConfig.SOCKET_ZENE_BASE_URL)
                .setTransports(listOf(WebSocket.NAME).toTypedArray())
                .setReconnectionDelay(15.seconds.inWholeMilliseconds)
                .build()

            mSocket = IO.socket(ZENE_BASE_URL_SOCKET, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mSocket?.on(Socket.EVENT_CONNECT) {
            val data = JSONObject()
            data.put("roomId", roomID)
            data.put("email", myEmail)
            data.put("token", authToken)
            mSocket?.emit("joinRoom", data)
        }

        mSocket?.on("partySync") { args ->
            val data = args.first() as JSONObject
            val email = data.getString("email")
            val name = data.getString("name")
            val photo = data.getString("photo")
            val message = data.getString("message")

            if (myEmail != email) {
                checkJob?.cancel()
                checkJob = viewModelScope.safeLaunch job@{
                    delay(1.seconds)
                    try {
                        val json = moshi.adapter(ZeneMusicData::class.java).fromJson(message)
                        if (lastSongID == json?.id) return@job

                        startMedia(json, listOf(json))
                        lastSongID = json?.id ?: ""

                        val body = context.resources.getString(R.string.changed_the_song)
                        NotificationUtils(name, body).apply {
                            channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC)
                            setSmallImage(photo)
                            generate()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        mSocket?.connect()
    }


    fun sendPartyJson(v: ZeneMusicData) = viewModelScope.safeLaunch  {
        val json = moshi.adapter(ZeneMusicData::class.java).toJson(v)
        val data = JSONObject()
        data.put("room", roomID)
        data.put("email", myEmail)
        data.put("name", myName)
        data.put("photo", myProfilePhoto)
        data.put("token", authToken)
        data.put("message", json)

        mSocket?.emit("partySync", data)
    }

}