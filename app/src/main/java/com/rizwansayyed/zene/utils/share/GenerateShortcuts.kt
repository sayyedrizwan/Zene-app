package com.rizwansayyed.zene.utils.share

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.glance.appwidget.updateAll
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.ui.main.home.ShortcutSelector
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_CONNECT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_ENTERTAINMENT
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_MY_LIBRARY
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_PODCASTS
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SEARCH
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL_SETTINGS
import com.rizwansayyed.zene.utils.share.ShareContentUtils.generateShareUrl
import com.rizwansayyed.zene.widgets.playingsongbig.PlayingSongWidget
import com.rizwansayyed.zene.widgets.playingsongsmall.PlayingSongWidgetSmall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


object GenerateShortcuts {

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateHomeScreenShortcut(data: ZeneMusicData?) = CoroutineScope(Dispatchers.IO).launch {
        val url = generateShareUrl(data)
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        if (shortcutManager?.isRequestPinShortcutSupported == true) {
            val shortcutIntent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                setData(url.toUri())
            }
            val bitmap = Glide.with(context).asBitmap().load(data?.thumbnail).submit().get()

            val shortcut = ShortcutInfo.Builder(context, data?.id)
                .setShortLabel(data?.name ?: "")
                .setLongLabel(data?.name ?: "")
                .setIcon(Icon.createWithAdaptiveBitmap(bitmap))
                .setIntent(shortcutIntent)
                .build()


            val homeScreen = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val successCallback = PendingIntent.getActivity(
                context, 0, homeScreen,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            shortcutManager.requestPinShortcut(shortcut, successCallback.intentSender)
        }

        if (isActive) cancel()
    }

    fun generateMainShortcuts(context: MainActivity) {
        ShortcutSelector.entries.forEach { s ->
            val urls = when (s) {
                ShortcutSelector.CONNECT -> ZENE_URL_CONNECT
                ShortcutSelector.SEARCH -> ZENE_URL_SEARCH
                ShortcutSelector.ENT -> ZENE_URL_ENTERTAINMENT
                ShortcutSelector.SETTINGS -> ZENE_URL_SETTINGS
                ShortcutSelector.PODCAST -> ZENE_URL_PODCASTS
                ShortcutSelector.MY_LIBRARY -> ZENE_URL_MY_LIBRARY
            }

            val label = when (s) {
                ShortcutSelector.CONNECT -> R.string.connect
                ShortcutSelector.SEARCH -> R.string.search
                ShortcutSelector.ENT -> R.string.entertainment_
                ShortcutSelector.SETTINGS -> R.string.settings_
                ShortcutSelector.PODCAST -> R.string.podcasts
                ShortcutSelector.MY_LIBRARY -> R.string.my_library
            }

            val shortcutIcon = when (s) {
                ShortcutSelector.CONNECT -> R.drawable.ic_hotspot
                ShortcutSelector.SEARCH -> R.drawable.ic_search
                ShortcutSelector.ENT -> R.drawable.ic_audio_book
                ShortcutSelector.SETTINGS -> R.drawable.ic_setting
                ShortcutSelector.PODCAST -> R.drawable.ic_podcast
                ShortcutSelector.MY_LIBRARY -> R.drawable.ic_library
            }

            val drawable = ContextCompat.getDrawable(context, shortcutIcon)?.mutate()
            drawable?.setTint(MainColor.toArgb())

            val bitmap = drawable?.toBitmap()
            val icon = bitmap?.let { IconCompat.createWithBitmap(it) }

            val intentConnect = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                data = "${ZENE_URL}${urls}".toUri()
            }
            val shortcutConnect = ShortcutInfoCompat.Builder(context, urls)
                .setShortLabel(context.resources.getString(label))
                .setLongLabel(context.resources.getString(label))
                .setIcon(icon)
                .setIntent(intentConnect)
                .build()

            ShortcutManagerCompat.pushDynamicShortcut(context, shortcutConnect)
        }

        CoroutineScope(Dispatchers.Main).launch {
            PlayingSongWidget().updateAll(context)
            PlayingSongWidgetSmall().updateAll(context)
            if (isActive) cancel()
        }
    }

}