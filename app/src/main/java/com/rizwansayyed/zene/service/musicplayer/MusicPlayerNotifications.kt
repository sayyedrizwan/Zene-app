package com.rizwansayyed.zene.service.musicplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadata
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.rizwansayyed.zene.MainActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.service.MusicServiceUtils.Commands.SEEK_DURATION_VIDEO
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.GO_TO_THE_NEXT_MUSIC
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.GO_TO_THE_PREVIOUS_MUSIC
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.MUSIC_BACKWARD_5S
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.MUSIC_FORWARD_5S
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.PAUSE_THE_MUSIC
import com.rizwansayyed.zene.service.musicplayer.MusicPlayerNotificationReceiver.Companion.PLAY_THE_MUSIC
import com.rizwansayyed.zene.utils.Utils.NotificationIDS.NOTIFICATION_M_P_CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL


class MusicPlayerNotifications(
    private val context: MusicPlayService,
    private val isPlaying: Boolean,
    private val name: String?,
    private val artists: String?,
    private val art: String?,
    private val duration: Int?,
    private val currentDuration: Int?
) {

    companion object {
        private val playPauseSongTxt = context.resources.getString(R.string.play_pause)
        private val previousSongTxt = context.resources.getString(R.string.play_previous)
        private val nextSongTxt = context.resources.getString(R.string.play_next)
        private val back5sTxt = context.resources.getString(R.string.back_5s)
        private val forward5sTxt = context.resources.getString(R.string.forward_5s)
        var mediaSession: MediaSessionCompat =
            MediaSessionCompat(context, "MEDIA_SESSION_TAG").apply {
                isActive = true
            }
    }

    private val openMusicPlayerIntent = Intent(context, MainActivity::class.java).let { i ->
        i.putExtra(Intent.ACTION_MAIN, "PLAYER")
        PendingIntent.getActivity(context, 99, i, PendingIntent.FLAG_IMMUTABLE)
    }

    private val callback = object : MediaSessionCompat.Callback() {
        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            val position = pos / 1000
            sendWebViewCommand(SEEK_DURATION_VIDEO, position.toInt())
        }
    }

    @SuppressLint("MissingPermission")
    fun generate() = CoroutineScope(Dispatchers.IO).launch {
        val imageBitmap: Bitmap? = art?.let { downloadImage(it) }

        val v = when (musicSpeedSettings.first()) {
            MusicSpeed.`025` -> 0.25
            MusicSpeed.`05` -> 0.5
            MusicSpeed.`1` -> 1.0
            MusicSpeed.`15` -> 1.5
            MusicSpeed.`20` -> 2.0
        }

        val mediaSession = prepareMediaSession(imageBitmap, v.toFloat()).sessionToken
        val style =
            androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(1, 2, 3)
                .setShowCancelButton(false).setMediaSession(mediaSession)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_M_P_CHANNEL_ID).apply {
            addAction(
                R.drawable.ic_go_backward_5sec, back5sTxt, pendingIntent(MUSIC_BACKWARD_5S)
            )
            addAction(
                R.drawable.ic_previous, previousSongTxt, pendingIntent(GO_TO_THE_PREVIOUS_MUSIC)
            )
            addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                playPauseSongTxt,
                if (isPlaying) pendingIntent(PAUSE_THE_MUSIC)
                else pendingIntent(PLAY_THE_MUSIC)
            )
            addAction(R.drawable.ic_next, nextSongTxt, pendingIntent(GO_TO_THE_NEXT_MUSIC))
            addAction(R.drawable.ic_go_forward_5sec, forward5sTxt, pendingIntent(MUSIC_FORWARD_5S))
            setStyle(style)
            setSmallIcon(R.drawable.ic_zene_logo_round)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSilent(true)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(openMusicPlayerIntent)
            setContentTitle(name)
            setContentText(artists)
            setSubText(name)
            setLargeIcon(imageBitmap)
        }

        generateChannel()

        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            else 0

        try {
            ServiceCompat.startForeground(context, 99, notification.build(), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun generateEmpty() = CoroutineScope(Dispatchers.IO).launch {
        val notification = NotificationCompat.Builder(context, NOTIFICATION_M_P_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_zene_logo_round)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSilent(true)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(openMusicPlayerIntent)
            setContentTitle("")
        }

        generateChannel()

        val type =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            else 0

        try {
            ServiceCompat.startForeground(context, 99, notification.build(), type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateChannel() {
        val name = context.resources.getString(R.string.zene_music_player)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_M_P_CHANNEL_ID, name, importance)
        channel.description = name

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private suspend fun prepareMediaSession(imageBitmap: Bitmap?, v: Float) =
        withContext(Dispatchers.Main) {
            val stateBuilder = PlaybackStateCompat.Builder()
                .setState(
                    if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    (currentDuration?.toLong() ?: 0) * 1000, v
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_PLAY_PAUSE)

            mediaSession.setCallback(callback)
            mediaSession.setPlaybackState(stateBuilder.build())

            val mediaData = MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, name)
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, art)
                .putString(MediaMetadata.METADATA_KEY_TITLE, name)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, artists)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, artists)
                .putBitmap(MediaMetadata.METADATA_KEY_ART, imageBitmap)
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, imageBitmap).putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION, (duration?.toLong() ?: 0) * 1000
                ).build()
            mediaSession.setMetadata(mediaData)
            return@withContext mediaSession
        }


    private fun pendingIntent(action: String): PendingIntent? {
        val brPlay = Intent(context, MusicPlayerNotificationReceiver::class.java).apply {
            putExtra(Intent.ACTION_MAIN, action)
        }

        return PendingIntent
            .getBroadcast(context, (11..888).random(), brPlay, PendingIntent.FLAG_MUTABLE)
    }

    private suspend fun downloadImage(path: String) = withContext(Dispatchers.IO) {
        try {
            val compressPath = path.substringBeforeLast("=w")
            val url = URL("$compressPath=w120-h120-l90-rj")
            val connection = url.openConnection()
                    as java.net.HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return@withContext bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}