package com.rizwansayyed.zene.service.player.notificationservice

import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerNotificationManager
import coil.ImageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.service.player.utils.Utils.downloadImageAsBitmap

@UnstableApi
class SimpleMediaNotificationAdapter(
    private val pendingIntent: PendingIntent?
) : PlayerNotificationManager.MediaDescriptionAdapter {

    override fun getCurrentContentTitle(player: Player): CharSequence =
        player.mediaMetadata.title ?: ""

    override fun createCurrentContentIntent(player: Player): PendingIntent? =
        pendingIntent

    override fun getCurrentContentText(player: Player): CharSequence =
        player.mediaMetadata.artist ?: ""

    override fun getCurrentLargeIcon(
        player: Player, callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        downloadImageAsBitmap(player.mediaMetadata.artworkUri) {
            callback.onBitmap(it)
        }
        return null
    }
}
