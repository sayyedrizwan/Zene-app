package com.rizwansayyed.zene.service

import android.content.Intent
import android.util.Log
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.player.ExoPlayerManager
import com.rizwansayyed.zene.utils.Utils.IntentExtra.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.utils.Utils.IntentExtra.SONG_MEDIA_POSITION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SongPlayerService : MediaLibraryService() {

    @Inject
    lateinit var exoPlayerManager: ExoPlayerManager

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) =
        exoPlayerManager.mediaLibrarySession


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent ?: return START_NOT_STICKY

        val l = moshi.adapter(Array<MusicData>::class.java)
            .fromJson(intent.getStringExtra(PLAY_SONG_MEDIA)!!)
        exoPlayerManager.musicData = l
        val positionStartToPlay = intent.getIntExtra(SONG_MEDIA_POSITION, 0)

        exoPlayerManager.mediaLibrarySession =
            MediaLibrarySession.Builder(this, exoPlayerManager.player, exoPlayerManager.callback)
                .build()

        runBlocking(Dispatchers.IO) {
            exoPlayerManager.downloadAndStartPlaying(positionStartToPlay)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        exoPlayerManager.stop()
        super.onDestroy()
    }
}