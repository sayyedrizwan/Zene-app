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
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        val request = ImageRequest.Builder(context).data(player.mediaMetadata.artworkUri)
            .target(onSuccess = { result ->
                callback.onBitmap((result as BitmapDrawable).bitmap)
            }).build()

        ImageLoader.Builder(context).crossfade(true).build().enqueue(request)
        return null
    }
}
