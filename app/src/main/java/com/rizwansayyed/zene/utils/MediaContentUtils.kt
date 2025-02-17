package com.rizwansayyed.zene.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
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
import com.rizwansayyed.zene.service.ForegroundService
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.VideoPlayerActivity
import com.rizwansayyed.zene.utils.MainUtils.moshi


object MediaContentUtils {

    fun startMedia(data: ZeneMusicData?, list: List<ZeneMusicData?> = emptyList()) {
        when (data?.type()) {
            NONE -> {}
            SONGS -> startAppService(context, data, list)
            VIDEOS -> Intent(context, VideoPlayerActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
                putExtra(Intent.ACTION_VIEW, data.id)
                context.startActivity(this)
            }

            PLAYLISTS -> {}
            ALBUMS -> {}
            ARTISTS -> {}
            PODCAST -> {}
            PODCAST_CATEGORIES -> {}
            NEWS -> {
                val toolbarColor =
                    CustomTabColorSchemeParams.Builder().setToolbarColor(MainColor.toArgb()).build()
                val builder = CustomTabsIntent.Builder().setDefaultColorSchemeParams(toolbarColor)
                    .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, toolbarColor)
                    .setUrlBarHidingEnabled(true).setShowTitle(true)
                    .setSendToExternalDefaultHandlerEnabled(true)

                val intent = builder.build().apply {
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
                }
                intent.launchUrl(context, Uri.parse(data.id))
            }

            MOVIES -> {}
            AI_MUSIC -> {}
            TEXT -> {}
            null -> {}
        }
    }


    private fun startAppService(context: Context, data: ZeneMusicData, list: List<ZeneMusicData?>) {
        val listJson =
            moshi.adapter(Array<ZeneMusicData?>::class.java).toJson(list.toTypedArray())

        Intent(context, ForegroundService::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.ACTION_VIEW, moshi.adapter(ZeneMusicData::class.java).toJson(data))
            putExtra(Intent.ACTION_RUN, listJson)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(this)
            else context.startService(this)
        }
    }

}