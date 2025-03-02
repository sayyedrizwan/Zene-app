package com.rizwansayyed.zene.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.toArgb
import com.rizwansayyed.zene.data.model.MusicDataTypes.*
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.player.PlayerForegroundService
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.VideoPlayerActivity
import com.rizwansayyed.zene.utils.MainUtils.moshi
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLIST_PAGE
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PODCAST_PAGE


object MediaContentUtils {

    fun openCustomBrowser(url: String?) {
        val toolbarColor =
            CustomTabColorSchemeParams.Builder().setToolbarColor(MainColor.toArgb()).build()
        val builder = CustomTabsIntent.Builder().setDefaultColorSchemeParams(toolbarColor)
            .setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_DARK, toolbarColor)
            .setUrlBarHidingEnabled(true).setShowTitle(true)
            .setSendToExternalDefaultHandlerEnabled(true)

        val intent = builder.build().apply {
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        intent.launchUrl(context, Uri.parse(url))
    }

    fun startMedia(
        data: ZeneMusicData?, l: List<ZeneMusicData?> = emptyList(), isNew: Boolean = false
    ) {
        val index = l.indexOfFirst { it?.id == data?.id }
        val list = getItemsAroundIndex(l, index)
        when (data?.type()) {
            NONE -> {}
            SONGS, AI_MUSIC, RADIO, PODCAST_AUDIO -> startAppService(context, data, list, isNew)
            VIDEOS -> Intent(context, VideoPlayerActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
                putExtra(Intent.ACTION_VIEW, data.id)
                context.startActivity(this)
            }

            PODCAST -> NavigationUtils.triggerHomeNav("$NAV_PODCAST_PAGE${data.id}")
            PLAYLISTS, ALBUMS -> NavigationUtils.triggerHomeNav("$NAV_PLAYLIST_PAGE${data.id}")

            ARTISTS -> {}
            PODCAST_CATEGORIES -> {}
            NEWS -> openCustomBrowser(data.id)
            MOVIES -> {}
            TEXT -> {}
            null -> {}
        }
    }


    private fun startAppService(
        context: Context, data: ZeneMusicData, list: List<ZeneMusicData?>, isNew: Boolean = false
    ) {
        val listJson = moshi.adapter(Array<ZeneMusicData?>::class.java).toJson(list.toTypedArray())

        Intent(context, PlayerForegroundService::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.ACTION_VIEW, moshi.adapter(ZeneMusicData::class.java).toJson(data))
            putExtra(Intent.ACTION_RUN, listJson)
            putExtra(Intent.ACTION_USER_PRESENT, isNew)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(this)
            else context.startService(this)
        }
    }

    private fun getItemsAroundIndex(
        list: List<ZeneMusicData?>, index: Int, limit: Int = 200
    ): List<ZeneMusicData?> {
        if (list.size <= limit) return list

        val halfLimit = limit / 2
        val startIndex = maxOf(0, index - halfLimit)
        val endIndex = minOf(list.size, startIndex + limit)
        return list.subList(startIndex, endIndex)
    }

}