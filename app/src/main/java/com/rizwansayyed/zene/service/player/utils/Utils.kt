package com.rizwansayyed.zene.service.player.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.media.audiofx.AudioEffect
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import coil.ImageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM_NO_PLAY
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_AT_END_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.ADD_PLAY_NEXT_ITEM
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_ACTION
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAYER_SERVICE_TYPE
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_LIVE_RADIO
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_PAUSE_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SEEK_TO_TIMESTAMP
import com.rizwansayyed.zene.service.player.utils.Utils.PlayerNotificationAction.SONG_MEDIA_POSITION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object Utils {

    object PlayerNotification {
        const val PLAYER_NOTIFICATION_ID = 350
        const val PLAYER_NOTIFICATION_CHANNEL_NAME = "Player Notification"
        const val PLAYER_NOTIFICATION_CHANNEL_ID = "player_notification_id"
    }

    object PlayerNotificationAction {
        val PLAYER_SERVICE_ACTION = "${context.packageName}_player_service_action"
        const val PLAYER_SERVICE_TYPE = "player_service_type"

        const val ADD_ALL_PLAYER_ITEM = "add_all_player_item"
        const val ADD_ALL_PLAYER_ITEM_NO_PLAY = "add_all_player_item_no_play"
        const val ADD_PLAY_NEXT_ITEM = "add_play_next_item"
        const val ADD_PLAY_AT_END_ITEM = "add_play_at_end_item"
        const val PLAY_PAUSE_MEDIA = "play_pause_media"
        const val PLAY_LIVE_RADIO = "play_live_radio"
        const val SEEK_TO_TIMESTAMP = "seek_to_timestamp"


        const val PLAY_SONG_MEDIA = "play_song_media"
        const val SONG_MEDIA_POSITION = "song_media_position"


        const val OPEN_MUSIC_PLAYER = "open_music_player"
    }

    fun MusicData.toMediaItem(url: String): MediaItem {
        val metadata = MediaMetadata.Builder().setTitle(this.name)
            .setDisplayTitle(this.name).setArtist(this.artists)
            .setArtworkUri(this.thumbnail?.toUri()).build()

        return MediaItem.Builder()
            .setUri(Uri.parse(url))
            .setMimeType("audio/mp3")
            .setMediaId(this.pId ?: (123..9999).random().toString())
            .setMediaMetadata(metadata)
            .build()
    }

    fun addAllPlayer(l: Array<MusicData?>?, p: Int) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, ADD_ALL_PLAYER_ITEM)
            putExtra(PLAY_SONG_MEDIA, moshi.adapter(Array<MusicData?>::class.java).toJson(l))
            putExtra(SONG_MEDIA_POSITION, p)
            context.sendBroadcast(this)
        }
    }

    fun addAllPlayerNotPlay(l: Array<MusicData?>?, p: Int) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, ADD_ALL_PLAYER_ITEM_NO_PLAY)
            putExtra(PLAY_SONG_MEDIA, moshi.adapter(Array<MusicData?>::class.java).toJson(l))
            putExtra(SONG_MEDIA_POSITION, p)
            context.sendBroadcast(this)
        }
    }

    fun playNextPlayer(l: MusicData?) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, ADD_PLAY_NEXT_ITEM)
            putExtra(PLAY_SONG_MEDIA, moshi.adapter(MusicData::class.java).toJson(l))
            context.sendBroadcast(this)
        }
    }

    fun addToEndPlayer(l: MusicData?) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, ADD_PLAY_AT_END_ITEM)
            putExtra(PLAY_SONG_MEDIA, moshi.adapter(MusicData::class.java).toJson(l))
            context.sendBroadcast(this)
        }
    }

    fun playOrPauseMedia(doPlay: Boolean) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, PLAY_PAUSE_MEDIA)
            putExtra(PLAY_SONG_MEDIA, doPlay)
            context.sendBroadcast(this)
        }
    }

    fun playRadioOnPlayer(radio: OnlineRadioResponseItem) {
        val mediaData = moshi.adapter(OnlineRadioResponseItem::class.java).toJson(radio)
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, PLAY_LIVE_RADIO)
            putExtra(PLAY_SONG_MEDIA, mediaData)
            context.sendBroadcast(this)
        }
    }

    fun seekToTimestamp(t: Long) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, SEEK_TO_TIMESTAMP)
            putExtra(SONG_MEDIA_POSITION, t)
            context.sendBroadcast(this)
        }
    }

    fun openSettingsPermission(v: String?) {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)

        v?.toast()
    }

    fun downloadImageAsBitmap(uri: Uri?, done: (Bitmap) -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            val request = ImageRequest.Builder(context).data(uri)
                .target(onSuccess = { result ->
                    done((result as BitmapDrawable).bitmap)
                }).build()
            ImageLoader.Builder(context).crossfade(true).build().enqueue(request)

            if (isActive) cancel()
        }

    fun openEqualizer() {
        val mediaPlayer = MediaPlayer()
        val sessionID = mediaPlayer.audioSessionId

        val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionID)
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
        context.startActivity(intent)
    }


    object PlayerActionService {
        val PLAYER_SERVICE_ACTION = "${context.packageName}_player_service_action"

        fun playerActionIntentFilter() {

        }
    }
}