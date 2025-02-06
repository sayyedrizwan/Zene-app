package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
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
    private val title: String, private val desc: String
) {
    companion object {
        val OTHER_NOTIFICATION = context.resources.getString(R.string.other_notification)
        val OTHER_NOTIFICATION_DESC = context.resources.getString(R.string.other_notification_desc)

        val CONNECT_UPDATES_NAME = context.resources.getString(R.string.connect_updates_name)
        val CONNECT_UPDATES_NAME_DESC =
            context.resources.getString(R.string.connect_updates_name_desc)
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private var channelName = OTHER_NOTIFICATION
    private var channelDesc = OTHER_NOTIFICATION_DESC

    fun setIntent(v: Intent) = apply { intent = v }
    fun channel(name: String, desc: String) = apply {
        channelName = name
        channelDesc = desc
    }

    @SuppressLint("MissingPermission")
    fun generate() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            (11..999).random(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = channelName.lowercase().replace(" ", "_")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = channelDesc
            }
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.zene_logo)
            .setContentTitle(title).setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(desc))

        with(NotificationManagerCompat.from(context)) {
            notify(
                (11..999).random(), builder.build()
            )
        }
    }

}