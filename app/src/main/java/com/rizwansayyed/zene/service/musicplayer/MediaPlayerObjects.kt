package com.rizwansayyed.zene.service.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.MainThread
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.utils.Algorithims.randomIds
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.downloader.opensource.State
import com.rizwansayyed.zene.utils.downloader.opensource.YTExtractor
import com.rizwansayyed.zene.utils.downloader.opensource.bestQuality
import com.rizwansayyed.zene.utils.downloader.opensource.getAudioOnly
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MediaPlayerObjects @Inject constructor(@ApplicationContext private val context: Context) {

    private val audioAttributes by lazy {
        AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    val player by lazy {
        ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true).setHandleAudioBecomingNoisy(true)
            .setWakeMode(C.WAKE_MODE_LOCAL).build()
    }

    val sessionToken by lazy {
        SessionToken(context, ComponentName(context, MediaPlayerService::class.java))
    }

    fun mediaItems(
        id: String?, url: String?, title: String?, artists: String?, thumbnail: String?
    ): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setTitle(title).setArtist(artists).setArtworkUri(thumbnail?.toUri()).build()

        return MediaItem.Builder()
            .setUri(url).setMediaId(id ?: randomIds())
            .setMediaMetadata(mediaMetaData).build()
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun playSong(media: MediaItem) = CoroutineScope(Dispatchers.Main).launch {
        if (player.isPlaying) {
            player.addMediaItem(media)
            return@launch
        }

        val controllerFuture =
            MediaController.Builder(context, sessionToken).buildAsync()

        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())

        player.setMediaItem(media)
        player.playWhenReady = true
        player.prepare()
    }


    suspend fun mediaAudioPaths(id: String): String? {
        val yt = YTExtractor(con = context, CACHING = true, LOGGING = true, retryCount = 3).apply {
            extract(id)  //"sfJDnua1cB4"
        }

        if (yt.state == State.SUCCESS) {
            val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
            return files?.url
        }

        return null
    }
}