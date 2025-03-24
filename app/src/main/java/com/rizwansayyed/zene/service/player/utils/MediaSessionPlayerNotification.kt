package com.rizwansayyed.zene.service.player.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_ID
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_NAME
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaSessionPlayerNotification(private val context: PlayerForegroundService) {
    private var mediaSession: MediaSessionCompat? = null
    private val storedBitmap = HashMap<String, Bitmap>()
    private var doLoop = false
    private var doShuffle = false

    init {
        CoroutineScope(Dispatchers.IO).launch {
            DataStorageManager.isLoopDB.collectLatest {
                doLoop = it
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            DataStorageManager.isShuffleDB.collectLatest {
                doShuffle = it
            }
        }
    }

    private fun createMediaSession() {
        if (mediaSession != null) return
        mediaSession = MediaSessionCompat(context, CHANNEL_MUSIC_PLAYER_NAME).apply {
            isActive = true
        }
        CoroutineScope(Dispatchers.Main).launch {
            mediaSession?.setCallback(callback)
            if (isActive) cancel()
        }
    }

    private suspend fun loadBitmapFromUrl(imageUrl: String): Bitmap? {
        if (storedBitmap.contains(imageUrl)) return storedBitmap[imageUrl]

        return withContext(Dispatchers.IO) {
            try {
                val b = Glide.with(context).asBitmap().load(imageUrl).submit().get()
                storedBitmap[imageUrl] = b
                b
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun updateMetadata(data: ZeneMusicData?, duration: String) {
        createMediaSession()
        val d = if (data?.type() == MusicDataTypes.SONGS)
            (duration.toDoubleOrNull()?.times(1000))?.toLong() ?: 0L
        else duration.toLongOrNull() ?: 0L

        val bitmap = loadBitmapFromUrl(data?.thumbnail ?: "")

        mediaSession?.setMetadata(
            MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, data?.name)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, data?.artists)
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, d).build()
        )
    }

    private fun updatePlaybackState(
        isPlaying: YoutubePlayerState,
        currentTS: String,
        speed: String
    ) {
        val d = if (context.currentPlayingSong?.type() == MusicDataTypes.SONGS)
            (currentTS.toDoubleOrNull()?.times(1000))?.toLong() ?: 0L
        else
            currentTS.toLongOrNull() ?: 0L

        var showSeek =
            PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SEEK_TO

        if (context.currentPlayingSong?.type() == MusicDataTypes.RADIO) {
            showSeek =
                PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        }

        var state =
            if (isPlaying == YoutubePlayerState.PLAYING) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED

        if (isPlaying == YoutubePlayerState.BUFFERING) state = PlaybackStateCompat.STATE_BUFFERING

        val playback =
            PlaybackStateCompat.Builder().setActions(showSeek).setState(state, d, speed.toFloat())

        if (context.currentPlayingSong?.type() != MusicDataTypes.RADIO)
            playback.addCustomAction(PlaybackStateCompat.CustomAction.Builder(
                CATEGORY_APP_MUSIC,
                context.resources.getString(R.string.loop),
                if (doLoop) R.drawable.ic_repeat_one else R.drawable.ic_repeat
            ).run {
                val bundle = Bundle().apply {
                    putInt(Intent.ACTION_VIEW, PlaybackStateCompat.REPEAT_MODE_ONE)
                }
                setExtras(bundle)
                build()
            })

        playback.addCustomAction(PlaybackStateCompat.CustomAction.Builder(
            CATEGORY_APP_MUSIC,
            context.resources.getString(R.string.loop),
            if (doShuffle) R.drawable.ic_shuffle_square else R.drawable.ic_shuffle
        ).run {
            val bundle = Bundle().apply {
                putInt(Intent.ACTION_VIEW, PlaybackStateCompat.SHUFFLE_MODE_NONE)
            }
            setExtras(bundle)
            build()
        })

        mediaSession?.setPlaybackState(playback.build())
    }

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
            PlayerForegroundService.getPlayerS()?.play()
        }

        override fun onCustomAction(action: String?, extras: Bundle?) {
            super.onCustomAction(action, extras)
            if (action == CATEGORY_APP_MUSIC) {
                val repeat = extras?.getInt(Intent.ACTION_VIEW) ?: 0
                if (repeat == PlaybackStateCompat.REPEAT_MODE_ONE) {
                    CoroutineScope(Dispatchers.IO).launch {
                        DataStorageManager.isLoopDB = flowOf(!doLoop)
                        if (isActive) cancel()
                    }
                }

                if (repeat == PlaybackStateCompat.SHUFFLE_MODE_NONE) {
                    CoroutineScope(Dispatchers.IO).launch {
                        DataStorageManager.isShuffleDB = flowOf(!doShuffle)
                        if (isActive) cancel()
                    }
                }
            }

        }

        override fun onPause() {
            super.onPause()
            PlayerForegroundService.getPlayerS()?.pause()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            PlayerForegroundService.getPlayerS()?.toBackSong()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            PlayerForegroundService.getPlayerS()?.toNextSong()
        }

        override fun onStop() {
            super.onStop()
            PlayerForegroundService.getPlayerS()?.pause()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            if (context.currentPlayingSong?.type() == MusicDataTypes.SONGS)
                PlayerForegroundService.getPlayerS()?.seekTo(pos / 1000)
            else
                PlayerForegroundService.getPlayerS()?.seekTo(pos)
        }
    }

    fun showNotification(isPlaying: YoutubePlayerState, currentTS: String, speed: String) {
        updatePlaybackState(isPlaying, currentTS, speed)
        createNotificationChannel()

        val openPlayer = Intent(context, MainActivity::class.java).apply {
            putExtra(Intent.ACTION_SENDTO, CATEGORY_APP_MUSIC)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, (11..888).random(), openPlayer,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_MUSIC_PLAYER_ID).apply {
            setContentTitle(mediaSession?.controller?.metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
            setContentText(mediaSession?.controller?.metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
            setLargeIcon(mediaSession?.controller?.metadata?.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
            setSmallIcon(R.drawable.zene_logo).setPriority(NotificationCompat.PRIORITY_HIGH)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setContentIntent(pendingIntent)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
//                    .setShowActionsInCompactView(0, 1, 2)

            )
        }.build()

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }

        ServiceCompat.startForeground(context, 1, notification, serviceType)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_MUSIC_PLAYER_ID,
                CHANNEL_MUSIC_PLAYER_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}