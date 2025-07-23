package com.rizwansayyed.zene.service.player.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.CATEGORY_APP_MUSIC
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.datastore.model.YoutubePlayerState
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_ID
import com.rizwansayyed.zene.service.notification.EmptyServiceNotification.CHANNEL_MUSIC_PLAYER_NAME
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.service.player.PlayerMediaButtonReceiver
import com.rizwansayyed.zene.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaSessionPlayerNotification(private val context: PlayerForegroundService) {
    private var mediaSession: MediaSessionCompat? = null
    private val storedBitmap = HashMap<String, Bitmap>()
    private var doLoop = false
    private var doShuffle = false
    private var isAppInForeground = true

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
        CoroutineScope(Dispatchers.Main).launch {
            mediaSession = MediaSessionCompat(context, CHANNEL_MUSIC_PLAYER_NAME).apply {
                isActive = true
            }
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
            } catch (_: Exception) {
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

        val stateActions =
            PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                    PlaybackStateCompat.ACTION_SEEK_TO

        val playback =
            PlaybackStateCompat.Builder().setActions(showSeek).setState(state, d, speed.toFloat())

        playback.setActions(stateActions)

        if (context.currentPlayingSong?.type() != MusicDataTypes.RADIO)
            playback.addCustomAction(
                PlaybackStateCompat.CustomAction.Builder(
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

        playback.addCustomAction(
            PlaybackStateCompat.CustomAction.Builder(
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
        override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
            val intentAction = mediaButtonEvent?.action
            if (Intent.ACTION_MEDIA_BUTTON == intentAction) {
                val event = mediaButtonEvent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
                if (event != null) {
                    when (event.keyCode) {
                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            ServiceStopTimerManager.cancelTimer()
                            PlayerForegroundService.getPlayerS()?.play()
                            return true
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            PlayerForegroundService.getPlayerS()?.pause()
                            return true
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            PlayerForegroundService.getPlayerS()?.toNextSong()
                            return true
                        }

                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            PlayerForegroundService.getPlayerS()?.toBackSong()
                            return true
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (musicPlayerDB.firstOrNull()?.isPlaying() == true)
                                    PlayerForegroundService.getPlayerS()?.play()
                                else
                                    PlayerForegroundService.getPlayerS()?.pause()

                                if (isActive) cancel()
                            }
                            return true
                        }
                    }
                }
            }
            return super.onMediaButtonEvent(mediaButtonEvent)
        }

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

        val playIntent = getMediaButtonPendingIntent(PlaybackStateCompat.ACTION_PLAY)
        val pauseIntent = getMediaButtonPendingIntent(PlaybackStateCompat.ACTION_PAUSE)
        val skipBackIntent =
            getMediaButtonPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        val skipForwardIntent = getMediaButtonPendingIntent(PlaybackStateCompat.ACTION_SKIP_TO_NEXT)


        val openPlayer = Intent(context, MainActivity::class.java).apply {
            putExtra(Intent.ACTION_SENDTO, CATEGORY_APP_MUSIC)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 10, openPlayer,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_MUSIC_PLAYER_ID).apply {
            setOngoing(false)
            setAutoCancel(true)
            setContentTitle(mediaSession?.controller?.metadata?.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
            setContentText(mediaSession?.controller?.metadata?.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
            setLargeIcon(mediaSession?.controller?.metadata?.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART))
            setSmallIcon(R.drawable.zene_logo)
            priority = NotificationCompat.PRIORITY_HIGH
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setContentIntent(pendingIntent)

            addAction(NotificationCompat.Action(R.drawable.ic_backward, "Previous", skipBackIntent))

            if (isPlaying == YoutubePlayerState.PLAYING)
                addAction(NotificationCompat.Action(R.drawable.ic_pause, "Pause", pauseIntent))
            else
                addAction(NotificationCompat.Action(R.drawable.ic_play, "Play", playIntent))

            addAction(NotificationCompat.Action(R.drawable.ic_forward, "Next", skipForwardIntent))

            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession?.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true)
            )
        }.build()

        val serviceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        } else {
            0
        }

        try {
            ServiceCompat.startForeground(context, 1, notification, serviceType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun getMediaButtonPendingIntent(action: Long): PendingIntent? {
        val intent = Intent().apply {
            component = ComponentName(context, PlayerMediaButtonReceiver::class.java)
            putExtra(Intent.ACTION_VIEW, action)
        }

        return PendingIntent.getBroadcast(
            context, action.hashCode(),
            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun forceStop() {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                mediaSession?.setPlaybackState(
                    PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_STOPPED, 0, 0f)
                        .build()
                )
                mediaSession?.isActive = false
                mediaSession?.release()
                mediaSession = null
            }

            CoroutineScope(Dispatchers.IO).launch {
                delay(500)
                context.stopSelf()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}