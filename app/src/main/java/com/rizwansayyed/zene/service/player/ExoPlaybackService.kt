package com.rizwansayyed.zene.service.player

import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ExoPlaybackService(val context: PlayerForegroundService) {
    private var mediaSession: MediaSession? = null
    private var exoPlayer: ExoPlayer? = null

    private val listener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == STATE_ENDED) context.songEnded()
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            if (context.currentPlayingSong?.type() == MusicDataTypes.SONGS) return
            context.errorReRun += 1
            if (context.errorReRun <= 3) context.playSongs(false)
        }
    }

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayer!!.addListener(listener)
        mediaSession = MediaSession.Builder(context, exoPlayer!!).build()
    }

    fun startPlaying(path: String?) = CoroutineScope(Dispatchers.Main).launch {
        path ?: return@launch

        val mediaItem = MediaItem.fromUri(path)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        if (!context.isNew) exoPlayer?.play()

        if (isActive) cancel()
    }

    fun stop() = CoroutineScope(Dispatchers.Main).launch {
        exoPlayer?.pause()
        exoPlayer?.stop()
        exoPlayer?.clearMediaItems()
    }

    suspend fun playingStatus() {
        try {
            val duration = exoPlayer?.duration.toString()
            val currentTS = exoPlayer?.currentPosition.toString()
            val playSpeed = exoPlayer?.playbackState.toString()
            val state = if (context.currentPlayingSong?.type() == MusicDataTypes.RADIO) {
                if (exoPlayer?.isPlaying == true) YoutubePlayerState.PLAYING else YoutubePlayerState.PAUSE
            } else {
                if (exoPlayer?.isPlaying == true) YoutubePlayerState.PLAYING else if (exoPlayer?.isLoading == true) YoutubePlayerState.BUFFERING else YoutubePlayerState.PAUSE
            }

            context.visiblePlayerNotification(state, currentTS, duration, playSpeed)

            val playerInfo = musicPlayerDB.firstOrNull()
            playerInfo?.state = state
            playerInfo?.speed = playSpeed
            playerInfo?.currentDuration = currentTS
            playerInfo?.totalDuration = duration
            musicPlayerDB = flowOf(playerInfo)
        } catch (e: Exception) {
            e.message
        }
    }

    fun play() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun seekTo(v: Long) {
        exoPlayer?.seekTo(v)
    }

    fun playRate(v: String) {
        exoPlayer?.setPlaybackSpeed(v.toFloatOrNull() ?: 1f)
    }


    fun destroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
    }

}