package com.rizwansayyed.zene.ui.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


val imgHashmap = HashMap<String, Bitmap>()

const val CHANNEL_ID = "ZENE_MUSIC_PLAYER"

fun customPlayerNotification(context: Context) = CoroutineScope(Dispatchers.Main).launch {

    return@launch

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationLayout = RemoteViews(context.packageName, R.layout.player_notification_small)


    val bitmap = withContext(Dispatchers.IO) {
        if (imgHashmap["id"] != null) return@withContext imgHashmap["id"]

        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data("https://i.ytimg.com/vi/dwHgRW4brkE/sddefault.jpg")
            .allowHardware(false)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        if (bitmap != null) {
            imgHashmap["id"] = bitmap
        }
        return@withContext bitmap
    }

    notificationLayout.setImageViewBitmap(R.id.img, bitmap)

    val customNotification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_zene_logo_round)
        .setColor(MainColor.toArgb())
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//        .setCustomContentView(notificationLayout)
        .setOngoing(true)
        .setCustomBigContentView(notificationLayout)
        .build()

    createNotificationChannel(context)

    notificationManager.notify(1298, customNotification)
}

private fun createNotificationChannel(context: Context) {
    val name = context.getString(R.string.music_player)
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = context.getString(R.string.music_player_desc)
    }

    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}
