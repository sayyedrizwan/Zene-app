package com.rizwansayyed.zene.service.musicplayer

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.domain.datastore.MusicSpeedEnum
import com.rizwansayyed.zene.domain.datastore.MusicSpeedEnum.*
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.presenter.model.MusicPlayerState
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.BUFF_PLAYBACK
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.MAX_BUFF
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.MIN_BUFF
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.exoPlayerGlobal
import com.rizwansayyed.zene.service.musicplayer.MediaPlayerBuffer.isExoPlayerGlobalInitialized
import com.rizwansayyed.zene.utils.Algorithims.randomIds
import com.rizwansayyed.zene.utils.Utils.showToast
import com.rizwansayyed.zene.utils.downloader.opensource.State
import com.rizwansayyed.zene.utils.downloader.opensource.YTExtractor
import com.rizwansayyed.zene.utils.downloader.opensource.bestQuality
import com.rizwansayyed.zene.utils.downloader.opensource.getAudioOnly
import com.rizwansayyed.zene.utils.downloader.opensource.getVideoOnly
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@UnstableApi
class MediaPlayerObjects @Inject constructor(
    @ApplicationContext private val context: Context,
    private val roomDBImpl: RoomDBImpl
) : Player.Listener {


    private val loadControl = DefaultLoadControl.Builder()
        .setBufferDurationsMs(MIN_BUFF, MAX_BUFF, BUFF_PLAYBACK, BUFF_PLAYBACK)
        .setPrioritizeTimeOverSizeThresholds(true)


    private val audioAttributes by lazy {
        AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA)
            .build()
    }


    val player by lazy {
        ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true).setLoadControl(loadControl.build())
            .setHandleAudioBecomingNoisy(true).setWakeMode(C.WAKE_MODE_LOCAL).build()
    }

    private val sessionToken by lazy {
        SessionToken(context, ComponentName(context, MediaPlayerService::class.java))
    }

    private var controllerFuture: ListenableFuture<MediaController>? = null

    fun mediaItems(
        id: String?, url: String?, title: String?, artists: String?, thumbnail: String?
    ): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setTitle(title).setArtist(artists).setComposer(id).setArtworkUri(thumbnail?.toUri())
            .build()

        return MediaItem.Builder()
            .setUri(url).setMediaId(id ?: randomIds())
            .setMediaMetadata(mediaMetaData).build()
    }

    fun mediaItems(
        id: String?, url: File?, title: String?, artists: String?, thumbnail: File?
    ): MediaItem {
        val mediaMetaData = MediaMetadata.Builder()
            .setTitle(title).setArtist(artists).setComposer(id).setArtworkUri(thumbnail?.toUri())
            .build()

        return MediaItem.Builder()
            .setUri(url?.toUri()).setMediaId(id ?: randomIds())
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
            try {
                player.setMediaItem(media)
                if (dataStoreManager.doMusicPlayerLoop.first())
                    player.repeatMode = Player.REPEAT_MODE_ALL
                else
                    player.repeatMode = Player.REPEAT_MODE_OFF

                player.playWhenReady = true
                player.prepare()
                playbackSpeed()

            }catch (e: Exception){
                e.message
            }
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

    fun playbackSpeed() = CoroutineScope(Dispatchers.IO).launch {
        val speed = dataStoreManager.musicPlaySpeed.first()

        withContext(Dispatchers.Main) {

            when (speed) {
                ZERO_FIVE.v -> player.setPlaybackSpeed(0.5f)
                ONE.v -> player.setPlaybackSpeed(1.0f)
                ONE_FIVE.v -> player.setPlaybackSpeed(1.5f)
                TWO.v -> player.setPlaybackSpeed(2.0f)
                TWO_FIVE.v -> player.setPlaybackSpeed(2.5f)
                THREE.v -> player.setPlaybackSpeed(3.0f)
            }

            if (isActive) cancel()
        }
        if (isActive) cancel()
    }

    fun restart() {
        player.seekTo(0)
        player.playWhenReady = true
        player.prepare()
    }

    fun forwardSec(s: Int) {
        player.seekTo(player.currentPosition + s.seconds.inWholeMilliseconds)
    }

    fun backwardSec(s: Int) {
        player.seekTo(player.currentPosition - s.seconds.inWholeMilliseconds)
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
        val yt = YTExtractor(con = context).apply { extract(id) }

        if (yt.state == State.SUCCESS) {
            val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
            return files?.url
        }
        val ytRetry = YTExtractor(con = context).apply { extract(id) }

        if (ytRetry.state == State.SUCCESS) {
            val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
            return files?.url
        }

        return null
    }

    override fun onEvents(player: Player, events: Player.Events) {
        val state = if (player.isPlaying) MusicPlayerState.PLAYING else MusicPlayerState.PAUSE
        val duration = player.duration
        val currentPosition = player.currentPosition
        val composer = player.mediaMetadata.composer.toString()
        if (isExoPlayerGlobalInitialized()) {
            exoPlayerGlobal.pause()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val musicData = dataStoreManager.musicPlayerData.first()
            musicData?.state = state
            musicData?.duration = duration
            musicData?.currentDuration = currentPosition

            dataStoreManager.musicPlayerData = flowOf(musicData)
            if (isActive) cancel()
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = roomDBImpl.getRecentData(composer).first()

            data?.playerDuration = duration
            data?.lastListenDuration = currentPosition
            if (data != null) {
                roomDBImpl.insertWhole(data).collect()
            }

            if (isActive) cancel()
        }
    }
}