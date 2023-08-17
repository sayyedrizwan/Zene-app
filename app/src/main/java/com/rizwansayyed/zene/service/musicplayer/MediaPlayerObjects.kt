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
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class MediaPlayerObjects @Inject constructor(@ApplicationContext private val context: Context) :
    Player.Listener {

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

    private val sessionToken by lazy {
        SessionToken(context, ComponentName(context, MediaPlayerService::class.java))
    }

    private var controllerFuture: ListenableFuture<MediaController>? = null

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
    fun playSong(media: MediaItem, newPlay: Boolean) = CoroutineScope(Dispatchers.Main).launch {
        if (controllerFuture == null) {
            controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture?.addListener({
                val controller = controllerFuture!!.get()
                controller.addListener(this@MediaPlayerObjects)
            }, MoreExecutors.directExecutor())
        }
        withContext(Dispatchers.Main) {
            if (!newPlay) {
                if (player.isPlaying) {
                    player.addMediaItem(media)
                    return@withContext
                }
                if (player.mediaItemCount > 0) {
                    player.addMediaItem(media)
                    player.playWhenReady = true
                    player.prepare()
                    return@withContext
                }
            }

            player.setMediaItem(media)
            player.playWhenReady = true
            player.prepare()
        }
    }

    suspend fun mediaAudioPaths(id: String): String? {
        val yt = YTExtractor(con = context, CACHING = false, LOGGING = true, retryCount = 1).apply {
            extract(id)
        }

        if (yt.state == State.SUCCESS) {
            val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
            return files?.url
        }
        val ytRetry =
            YTExtractor(con = context, CACHING = false, LOGGING = true, retryCount = 1).apply {
                extract(id)
            }

        if (ytRetry.state == State.SUCCESS) {
            val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
            return files?.url
        }

        return null
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)) {
            player.currentMediaItem?.let { mediaItem ->
//                nowPlaying.update {
//                    mediaItem
//                }
            }
        }

        if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
//            isPlaying.update {
//                player.isPlaying
//            }
        }

        if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION) ||
            events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED) ||
            events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)
        ) {
            if (player.duration > 0) {
//                duration.update {
//                    player.duration
//                }
            }
        }
    }
}