package com.rizwansayyed.zene.presenter.util

import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.player.utils.Utils.downloadImageAsBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UtilsWallpaperImage(private val image: String?) {
    fun homeScreenWallpaper() = CoroutineScope(Dispatchers.IO).launch {
        image ?: return@launch

        downloadImageAsBitmap(image.toUri()) {
            makeWallpaper(it)
        }
    }

    fun lockScreenWallpaper() = CoroutineScope(Dispatchers.IO).launch {
        image ?: return@launch

        downloadImageAsBitmap(image.toUri()) {
            makeLockScreenWallpaper(it)
        }
    }

    fun makeWallpaper(bitmap: Bitmap) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)

            wallpaperManager.setBitmap(
                bitmap, null, true, WallpaperManager.FLAG_SYSTEM
            )
        } catch (e: Exception) {
            e.message
        }
    }

    fun makeLockScreenWallpaper(bitmap: Bitmap) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)

            wallpaperManager.setBitmap(
                bitmap, null, true, WallpaperManager.FLAG_LOCK
            )
        } catch (e: Exception) {
            e.message
        }
    }
}