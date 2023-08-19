package com.rizwansayyed.zene.service.musicplayer

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.presenter.model.VideoDataDownloader
import com.rizwansayyed.zene.utils.Algorithims.randomIds
import com.rizwansayyed.zene.utils.downloader.opensource.State
import com.rizwansayyed.zene.utils.downloader.opensource.YTExtractor
import com.rizwansayyed.zene.utils.downloader.opensource.bestQuality
import com.rizwansayyed.zene.utils.downloader.opensource.getAudioOnly
import com.rizwansayyed.zene.utils.downloader.opensource.getVideoOnly
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@UnstableApi
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

    fun playSong(media: MediaItem, newPlay: Boolean) = CoroutineScope(Dispatchers.IO).launch {
        if (controllerFuture == null) {
            controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            controllerFuture?.addListener({
                val controller = controllerFuture!!.get()
                controller.addListener(this@MediaPlayerObjects)
            }, ContextCompat.getMainExecutor(context))
        }

        if (!newPlay) {
            if (player.isPlaying) {
                player.addMediaItem(media)
                return@launch
            }
            if (player.mediaItemCount > 0) {
                player.addMediaItem(media)
                player.playWhenReady = true
                player.prepare()
                return@launch
            }
        }
        withContext(Dispatchers.Main) {
            player.setMediaItem(media)
            if (dataStoreManager.doMusicPlayerLoop.first())
                player.repeatMode = Player.REPEAT_MODE_ALL
            else
                player.repeatMode = Player.REPEAT_MODE_OFF

            player.playWhenReady = true
            player.prepare()
        }
    }

    fun setPlayerDuration(duration: Long) {
        player.seekTo(duration)
    }

    fun doPlayer(forceStop: Boolean = false) {
        if (forceStop) {
            player.pause()
            return
        }
        if (player.isPlaying)
            player.pause()
        else
            player.play()
    }

    fun restart() {
        player.seekTo(0)
        player.playWhenReady = true
        player.prepare()
    }

    fun repeatMode() = CoroutineScope(Dispatchers.Main).launch {
        if (dataStoreManager.doMusicPlayerLoop.first())
            player.repeatMode = Player.REPEAT_MODE_ALL
        else
            player.repeatMode = Player.REPEAT_MODE_OFF

        if (isActive) cancel()
    }

    fun getPlayerDuration(): Long {
        return player.currentPosition
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

    suspend fun mediaVideoPaths(id: String): VideoDataDownloader? {
        val yt = YTExtractor(con = context, CACHING = false, LOGGING = true, retryCount = 1).apply {
            extract(id)
        }

        if (yt.state == State.SUCCESS) {
            val data = VideoDataDownloader("", "", "", "")
            yt.getYTFiles()?.getVideoOnly()?.forEach {
                if (it.meta?.height == 720 && it.meta.ext == "webm") {
                    data.hdd = it.url ?: ""
                }
                if (it.meta?.height == 480 && it.meta.ext == "webm") {
                    data.hd = it.url ?: ""
                }
                if (it.meta?.height == 360 && it.meta.ext == "webm") {
                    data.sd = it.url ?: ""
                }
            }
            data.audio = yt.getYTFiles()?.getVideoOnly()?.bestQuality()?.url ?: ""
            return data
        }
        return null
    }

    override fun onEvents(player: Player, events: Player.Events) {
        val state = if (player.isPlaying) MusicPlayerState.PLAYING else MusicPlayerState.PAUSE
        val duration = player.duration
        val currentPosition = player.currentPosition
        CoroutineScope(Dispatchers.IO).launch {
            val musicData = dataStoreManager.musicPlayerData.first()
            musicData?.state = state
            musicData?.duration = duration
            musicData?.currentDuration = currentPosition

            dataStoreManager.musicPlayerData = flowOf(musicData)
            if (isActive) cancel()
        }
    }
}