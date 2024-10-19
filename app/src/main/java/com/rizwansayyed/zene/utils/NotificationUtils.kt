package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rizwansayyed.zene.MainActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.NotificationIDS.NOTIFICATION_CHANNEL_ID
import com.rizwansayyed.zene.utils.Utils.loadBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



@SuppressLint("MissingPermission")
class NotificationUtils(title: String, body: String, img: Uri?) {

    private var soundAudio: Uri =
        Uri.parse((ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName) + "/" + R.raw.custom_notification_tone)

    private val soundAttributes: AudioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_zene_logo_round)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSound(soundAudio, AudioManager.STREAM_NOTIFICATION)

            if (img != null) {
                val imgLoaded = loadBitmap(img.toString())
                if (imgLoaded != null) builder.setLargeIcon(imgLoaded)
            }

            createNotificationChannel()

            try {
                with(NotificationManagerCompat.from(context)) {
                    notify((1..77).random(), builder.build())
                }
            } catch (e: Exception) {
                e.message
            }
        }
    }

    private fun createNotificationChannel() {
        val name = context.resources.getString(R.string.zene_notification)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
        channel.description = name
        channel.vibrationPattern = longArrayOf(100, 1000, 100, 1000, 100, 1000, 100)
        channel.setSound(soundAudio, soundAttributes)


        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}