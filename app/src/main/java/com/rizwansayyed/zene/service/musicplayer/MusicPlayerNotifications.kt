package com.rizwansayyed.zene.service.musicplayer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadata
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.MainActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.musicSpeedSettings
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.NotificationIDS.NOTIFICATION_M_P_CHANNEL_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MusicPlayerNotifications(
    private val isPlaying: Boolean,
    private val name: String?,
    private val artists: String?,
    private val art: String?,
    private val duration: Int?,
    private val currentDuration: Int?
) {

    val piss =
        Intent(context, MainActivity::class.java).let { intent ->
            PendingIntent.getActivity(
                context, 99,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
        }

    private val playPauseSongTxt = context.resources.getString(R.string.play_pause)
    private val previousSongTxt = context.resources.getString(R.string.play_previous)
    private val nextSongTxt = context.resources.getString(R.string.play_next)
    private val back5sTxt = context.resources.getString(R.string.back_5s)
    private val forward5sTxt = context.resources.getString(R.string.forward_5s)

    @SuppressLint("MissingPermission")
    fun generate() = CoroutineScope(Dispatchers.IO).launch {
        var imageBitmap: Bitmap? = null

        runBlocking(Dispatchers.IO) {
            try {
                val request = ImageRequest.Builder(context).data(art).build()
                val drawable = context.imageLoader.execute(request).drawable
                imageBitmap = drawable?.toBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val v = when (musicSpeedSettings.first()) {
            MusicSpeed.`025` -> 0.25
            MusicSpeed.`05` -> 0.5
            MusicSpeed.`1` -> 1.0
            MusicSpeed.`15` -> 1.5
            MusicSpeed.`20` -> 2.0
        }

        val style = androidx.media.app.NotificationCompat.MediaStyle()
            .setShowActionsInCompactView(1, 2, 3)
            .setShowCancelButton(false)
            .setMediaSession(prepareMediaSession(imageBitmap, v.toFloat()).sessionToken)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_M_P_CHANNEL_ID).apply {
            addAction(R.drawable.ic_go_backward_5sec, back5sTxt, piss)
            addAction(R.drawable.ic_previous, previousSongTxt, piss)
            addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play, playPauseSongTxt, piss
            )
            addAction(R.drawable.ic_next, nextSongTxt, piss)
            addAction(R.drawable.ic_go_forward_5sec, forward5sTxt, piss)
            setStyle(style)
            setSmallIcon(R.drawable.ic_zene_logo_round)
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            setSilent(true)
            priority = NotificationCompat.PRIORITY_MAX
            setContentIntent(piss)
            setContentTitle(name)
            setContentText(artists)
            setSubText(name)
            setLargeIcon(imageBitmap)
        }

        generateChannel()

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(54, notification.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun generateChannel() {
        val name = context.resources.getString(R.string.zene_music_player_notification)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_M_P_CHANNEL_ID, name, importance)
        channel.description = name

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun prepareMediaSession(imageBitmap: Bitmap?, v: Float) =
        runBlocking(Dispatchers.Main) {
            val mediaSession = MediaSessionCompat(context, "MEDIA_SESSION_TAG")
            mediaSession.isActive = true

            val stateBuilder = PlaybackStateCompat.Builder()
                .setState(
                    if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    (currentDuration?.toLong() ?: 0) * 1000, v
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_PLAY_PAUSE)

            mediaSession.setPlaybackState(stateBuilder.build())

            val mediaData = MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, name)
                .putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, art)
                .putString(MediaMetadata.METADATA_KEY_TITLE, name)
                .putString(MediaMetadata.METADATA_KEY_ARTIST, artists)
                .putBitmap(MediaMetadata.METADATA_KEY_ART, imageBitmap)
                .putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    (duration?.toLong() ?: 0) * 1000
                )
                .build()
            mediaSession.setMetadata(mediaData)
            return@runBlocking mediaSession
        }

}