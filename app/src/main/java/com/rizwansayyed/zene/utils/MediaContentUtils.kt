package com.rizwansayyed.zene.utils

import android.content.Intent
import com.rizwansayyed.zene.data.model.MusicDataTypes.AI_MUSIC
import com.rizwansayyed.zene.data.model.MusicDataTypes.ALBUMS
import com.rizwansayyed.zene.data.model.MusicDataTypes.ARTISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.MOVIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.NEWS
import com.rizwansayyed.zene.data.model.MusicDataTypes.NONE
import com.rizwansayyed.zene.data.model.MusicDataTypes.PLAYLISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_CATEGORIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.SONGS
import com.rizwansayyed.zene.data.model.MusicDataTypes.TEXT
import com.rizwansayyed.zene.data.model.MusicDataTypes.VIDEOS
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.videoplayer.VideoPlayerActivity

object MediaContentUtils {

    fun startMedia(data: ZeneMusicData?) {
        when (data?.type()) {
            NONE -> {}
            SONGS -> {}
            VIDEOS -> Intent(context, VideoPlayerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(Intent.ACTION_VIEW, data.id)
                context.startActivity(this)
            }

            PLAYLISTS -> {}
            ALBUMS -> {}
            ARTISTS -> {}
            PODCAST -> {}
            PODCAST_CATEGORIES -> {}
            NEWS -> {}
            MOVIES -> {}
            AI_MUSIC -> {}
            TEXT -> {}
            null -> {}
        }
    }

}