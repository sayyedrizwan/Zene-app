package com.rizwansayyed.zene.service.songparty

import android.app.ActivityManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.rizwansayyed.zene.data.onlinesongs.radio.implementation.OnlineRadioImplInterface
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_CLOSE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_RADIO_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PAUSE
import com.rizwansayyed.zene.service.songparty.Utils.Action.PARTY_ACTION_SONG_PLAY
import com.rizwansayyed.zene.service.songparty.Utils.Action.generatePartyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.Action.partyRoomId
import com.rizwansayyed.zene.service.songparty.Utils.Free4WebSocket.FREE_4_WEB_SOCKET
import com.rizwansayyed.zene.service.songparty.Utils.groupMusicUsersList
import com.rizwansayyed.zene.service.songparty.Utils.isNewAJoin
import com.rizwansayyed.zene.service.songparty.Utils.leaveDetailsInParty
import com.rizwansayyed.zene.service.songparty.Utils.leaveStatus
import com.rizwansayyed.zene.service.songparty.Utils.newJoin
import com.rizwansayyed.zene.service.songparty.Utils.pauseMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.playMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.playPauseSongStateSync
import com.rizwansayyed.zene.service.songparty.Utils.sendMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.sendMyDetailsInParty
import com.rizwansayyed.zene.service.songparty.Utils.sendRadioChangeData
import com.rizwansayyed.zene.service.songparty.Utils.songPartyRadioSync
import com.rizwansayyed.zene.service.songparty.Utils.songPartySongSync
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.Utils.registerAppReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class SongPartyService : Service() {

    @Inject
    lateinit var youtubeAPIImpl: YoutubeAPIImplInterface

    @Inject
    lateinit var onlineRadioImpl: OnlineRadioImplInterface

    private var tempJob: Job? = null

    companion object {
        fun isSongPartyServiceRunning(): Boolean {
            return (context.getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .getRunningServices(Integer.MAX_VALUE)
                .any { it.service.className == SongPartyService::class.java.name }
        }


        private var webSocket: WebSocket? = null
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                PARTY_ACTION_SONG_CHANGE -> CoroutineScope(Dispatchers.IO).launch {
                    registerEvent(FirebaseEvents.FirebaseEvent.PARTY_SONG_CHANGE)
                    val songId = intent.getStringExtra(Intent.EXTRA_TEXT)
                    webSocket?.send(sendMusicChangeData(songId))
                    if (isActive) cancel()
                }

                PARTY_ACTION_RADIO_CHANGE -> CoroutineScope(Dispatchers.IO).launch {
                    val radioId = intent.getStringExtra(Intent.EXTRA_TEXT)
                    webSocket?.send(sendRadioChangeData(radioId))
                    if (isActive) cancel()
                }

                PARTY_ACTION_SONG_PLAY -> CoroutineScope(Dispatchers.IO).launch {
                    registerEvent(FirebaseEvents.FirebaseEvent.PARTY_SONG_PLAY)
                    webSocket?.send(playMusicChangeData())
                    if (isActive) cancel()
                }

                PARTY_ACTION_SONG_PAUSE -> CoroutineScope(Dispatchers.IO).launch {
                    registerEvent(FirebaseEvents.FirebaseEvent.PARTY_SONG_PAUSE)
                    webSocket?.send(pauseMusicChangeData())
                    if (isActive) cancel()
                }

                PARTY_ACTION_CLOSE -> CoroutineScope(Dispatchers.IO).launch {
                    webSocket?.send(leaveDetailsInParty())
                    delay(2.seconds)
                    onDestroy()
                    if (isActive) cancel()
                }
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val roomId = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: generatePartyRoomId()
        partyRoomId = roomId
        groupMusicUsersList.clear()

        registerEvent(FirebaseEvents.FirebaseEvent.PARTY_SERVICE_RUNNING)

        IntentFilter().apply {
            addAction(PARTY_ACTION_SONG_CHANGE)
            addAction(PARTY_ACTION_SONG_PLAY)
            addAction(PARTY_ACTION_SONG_PAUSE)
            addAction(PARTY_ACTION_CLOSE)
            addAction(PARTY_ACTION_RADIO_CHANGE)
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY

            registerAppReceiver(receiver, this, this@SongPartyService)
        }

        tempJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(8.seconds)
                webSocket?.send(newJoin())
            }
        }


        connectWebSocket()

        return START_STICKY
    }


    inner class WebSocketServiceListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Companion.webSocket = webSocket
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                webSocket.send(newJoin())

                delay(2.seconds)
                joinMessage()
                if (isActive) cancel()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            CoroutineScope(Dispatchers.IO).launch {
                if (isNewAJoin(text)) {
                    joinMessage()
                }
                songPartySongSync(text, youtubeAPIImpl)
                songPartyRadioSync(text, onlineRadioImpl)
                playPauseSongStateSync(text)
                leaveStatus(text)
                if (isActive) cancel()
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            connectWebSocket()
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            connectWebSocket()
        }
    }

    private fun joinMessage() = CoroutineScope(Dispatchers.IO).launch {
        webSocket?.send(sendMyDetailsInParty())
    }

    private fun connectWebSocket() {
        webSocket?.close(1000, "Closing or Reconnect WebSocket connection")

        val client = OkHttpClient()
        val request = Request.Builder().url(FREE_4_WEB_SOCKET).build()
        webSocket = client.newWebSocket(request, WebSocketServiceListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        groupMusicUsersList.clear()
        tempJob?.cancel()
        partyRoomId = null
        webSocket?.close(1000, "Closing WebSocket connection")
    }
}