package com.rizwansayyed.zene.service.musicplayer


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.utils.Algorithims.randomIds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MediaPlayerService : MediaSessionService(), MediaSession.Callback {

    companion object {
        fun isMusicPlayerServiceIsRunning(): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (MediaPlayerService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun startMedaPlayerService() {
            Intent(context, MediaPlayerService::class.java).apply {
                context.startService(this)
            }
        }
    }

    @Inject
    lateinit var mediaPlayerObjects: MediaPlayerObjects

    private var mediaSession: MediaSession? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPlayingSong()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun startPlayingSong() {
        mediaSession =
            MediaSession.Builder(this, mediaPlayerObjects.player).setId(randomIds()).build()
    }


}