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
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.songparty.Utils.Free4WebSocket.FREE_4_WEB_SOCKET
import com.rizwansayyed.zene.service.songparty.Utils.PARTY_SERVICE_ACTION_SONG_CHANGE
import com.rizwansayyed.zene.service.songparty.Utils.PARTY_SERVICE_ACTION_SONG_PAUSE
import com.rizwansayyed.zene.service.songparty.Utils.PARTY_SERVICE_ACTION_SONG_PLAY
import com.rizwansayyed.zene.service.songparty.Utils.generateAndSendFirstTime
import com.rizwansayyed.zene.service.songparty.Utils.joinedMessage
import com.rizwansayyed.zene.service.songparty.Utils.joinedUserDetails
import com.rizwansayyed.zene.service.songparty.Utils.pauseMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.playMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.playPausePartySync
import com.rizwansayyed.zene.service.songparty.Utils.sendMusicChangeData
import com.rizwansayyed.zene.service.songparty.Utils.songListeningSync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    lateinit var youtubeAPIImplInterface: YoutubeAPIImplInterface

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
                PARTY_SERVICE_ACTION_SONG_CHANGE -> {
                    val extra = intent.getStringExtra(Intent.EXTRA_TEXT)
                    val musicData = moshi.adapter(MusicData::class.java).fromJson(extra!!)
                    CoroutineScope(Dispatchers.IO).launch {
                        webSocket?.send(sendMusicChangeData(musicData))
                        if (isActive) cancel()
                    }
                }

                PARTY_SERVICE_ACTION_SONG_PLAY -> CoroutineScope(Dispatchers.IO).launch {
                    webSocket?.send(playMusicChangeData())
                    if (isActive) cancel()
                }

                PARTY_SERVICE_ACTION_SONG_PAUSE -> CoroutineScope(Dispatchers.IO).launch {
                    webSocket?.send(pauseMusicChangeData())
                    if (isActive) cancel()
                }
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
//        val roomId = intent?.getStringExtra(Intent.EXTRA_TEXT)
//        setRoomId(roomId)

        IntentFilter().apply {
            addAction(PARTY_SERVICE_ACTION_SONG_CHANGE)
            addAction(PARTY_SERVICE_ACTION_SONG_PLAY)
            addAction(PARTY_SERVICE_ACTION_SONG_PAUSE)
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY
            ContextCompat.registerReceiver(
                this@SongPartyService, receiver, this, ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }


        connectWebSocket()

        return START_STICKY
    }


    inner class WebSocketServiceListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                webSocket.send(generateAndSendFirstTime())

                delay(2.seconds)
                webSocket.send(joinedMessage())
                if (isActive) cancel()
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            CoroutineScope(Dispatchers.IO).launch {
                joinedUserDetails(text)
                songListeningSync(text, youtubeAPIImplInterface)
                playPausePartySync(text)
                if (isActive) cancel()
            }
            Log.d("TAG", "Received message: $text")
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

    private fun connectWebSocket() {
        webSocket?.close(1000, "Closing or Reconnect WebSocket connection")

        val client = OkHttpClient()
        val request = Request.Builder().url(FREE_4_WEB_SOCKET).build()
        webSocket = client.newWebSocket(request, WebSocketServiceListener())
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "Closing WebSocket connection")
    }
}