package com.rizwansayyed.zene.utils


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.main.MainActivity


class NotificationUtils(
    private val title: String, private val desc: String, private val intent: Intent? = null
) {

    companion object {
        val channelIdForUpdates = "${context.packageName}_zene_update_alert"
    }

    private val notificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private var sound =
        Uri.parse((ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName) + "/" + R.raw.notification_sound)

    private var attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()

    init {
        start()
    }

    fun start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.resources.getString(R.string.zene_update_alerts)
            val importance = NotificationManager.IMPORTANCE_MAX
            val mChannel = NotificationChannel(channelIdForUpdates, name, importance)
            mChannel.description = name
            mChannel.enableLights(true)
            mChannel.enableVibration(true)
            mChannel.setSound(sound, attributes)

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(context, channelIdForUpdates)
            .setSmallIcon(R.drawable.zene_logo)
            .setContentTitle(title).setContentText(desc)
            .setStyle(NotificationCompat.BigTextStyle().bigText(desc))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val mainIntent = intent ?: Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, (11..999).random(), mainIntent, PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notificationManager.notify((11..999).random(), builder.build())
        }
    }
}