package com.rizwansayyed.zene.service.musicplayer


import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.rizwansayyed.zene.utils.Utils.EXTRA.PLAY_URL_PATH

class MediaPlayerService : MediaLibraryService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession? {
        return null
    }


}