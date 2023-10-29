package com.rizwansayyed.zene.service

import android.content.Intent
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.FileDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.hls.HlsMediaSource
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.Utils.PlayerNotificationAction.ADD_ALL_PLAYER_ITEM
import com.rizwansayyed.zene.service.Utils.PlayerNotificationAction.PLAYER_SERVICE_ACTION
import com.rizwansayyed.zene.service.Utils.PlayerNotificationAction.PLAYER_SERVICE_TYPE
import com.rizwansayyed.zene.service.Utils.PlayerNotificationAction.PLAY_SONG_MEDIA
import com.rizwansayyed.zene.service.Utils.PlayerNotificationAction.SONG_MEDIA_POSITION
import java.io.File

object Utils {

    object PlayerNotification {
        const val PLAYER_NOTIFICATION_ID = 350
        const val PLAYER_NOTIFICATION_CHANNEL_NAME = "start_player_notification"
        const val PLAYER_NOTIFICATION_CHANNEL_ID = "start_player_notification_id"
    }

    object PlayerNotificationAction {
        const val PLAYER_SERVICE_ACTION = "player_service_action"
        const val PLAYER_SERVICE_TYPE = "player_service_type"

        const val ADD_ALL_PLAYER_ITEM = "add_all_player_item"

        const val PLAY_SONG_MEDIA = "play_song_media"
        const val SONG_MEDIA_POSITION = "song_media_position"
    }

    fun MusicData.toMediaItem(url: String): MediaItem {
        val metadata = MediaMetadata.Builder().setTitle(this.name)
            .setDisplayTitle(this.name).setArtist(this.artists)
            .setArtworkUri(this.thumbnail?.toUri()).build()

        return MediaItem.Builder()
            .setUri(url)
            .setMediaId(this.pId ?: (123..9999).random().toString())
            .setMediaMetadata(metadata)
            .build()
    }

    fun addAllPlayer(l: Array<MusicData>?, p: Int) {
        Intent(PLAYER_SERVICE_ACTION).apply {
            putExtra(PLAYER_SERVICE_TYPE, ADD_ALL_PLAYER_ITEM)
            putExtra(PLAY_SONG_MEDIA, moshi.adapter(Array<MusicData>::class.java).toJson(l))
            putExtra(SONG_MEDIA_POSITION, p)
            context.sendBroadcast(this)
        }
    }
}