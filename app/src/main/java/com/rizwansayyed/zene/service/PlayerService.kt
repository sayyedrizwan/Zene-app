package com.rizwansayyed.zene.service

import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType
import com.rizwansayyed.zene.service.Utils.startPlayerNotification
import com.rizwansayyed.zene.service.Utils.toMediaItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerService : MediaSessionService() {

    @Inject
    lateinit var player: ExoPlayer

    @Inject
    lateinit var mediaSession: MediaSession

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) = mediaSession

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startPlayerNotification(player, this)

        CoroutineScope(Dispatchers.Main).launch {
            val music = MusicData(
                "https://wallpaperaccess.com/download/manifest-tv-show-8350796",
                "the name",
                "the artiststssss",
                "233",
                MusicType.MUSIC
            )

            player.apply {
                setMediaItem(music.toMediaItem("https://rs1.seedr.cc/ff_get/1653950113/02%20-%20Right%20Heres%20The%20Spot.mp3?st=udLh1c2SzDBFG2VXV2bORA&e=1698590797"))
                playWhenReady = true
                prepare()
                play()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        player.release()
        mediaSession.release()
        super.onDestroy()
    }


}