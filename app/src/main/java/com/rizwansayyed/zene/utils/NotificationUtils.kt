package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.utils.ChatTempDataUtils.getAGroupMessage
import com.rizwansayyed.zene.utils.ChatTempDataUtils.getNameGroupName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

class NotificationUtils(
    private val title: String, private val desc: String
) {
    companion object {
        val OTHER_NOTIFICATION = context.resources.getString(R.string.other_notification)
        val OTHER_NOTIFICATION_DESC = context.resources.getString(R.string.other_notification_desc)

        val CONNECT_UPDATES_NAME = context.resources.getString(R.string.connect_updates_name)
        val CONNECT_UPDATES_NAME_DESC =
            context.resources.getString(R.string.connect_updates_name_desc)

        val SLEEP_TIMER_NOTIFICATION =
            context.resources.getString(R.string.sleep_timer_notification)
        val SLEEP_TIMER_NOTIFICATION_DESC =
            context.resources.getString(R.string.sleep_timer_notification_desc)
    }

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    private var loadSmallImage: String? = null
    private var emailConv: String? = null

    private var channelName = OTHER_NOTIFICATION
    private var channelDesc = OTHER_NOTIFICATION_DESC

    fun setIntent(v: Intent) = apply { intent = v }
    fun setSmallImage(v: String?) = apply { loadSmallImage = v }
    fun generateMessageConv(v: String?) = apply { emailConv = v }
    fun channel(name: String, desc: String) = apply {
        channelName = name
        channelDesc = desc
    }

    @SuppressLint("MissingPermission")
    fun generate() = CoroutineScope(Dispatchers.IO).launch {
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

        val smallIconBitmap = loadImageFromURL(loadSmallImage)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.zene_logo)
            .setContentTitle(title).setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (emailConv == null) builder.setStyle(NotificationCompat.BigTextStyle().bigText(desc))
        else {
            val msgStyle = NotificationCompat.MessagingStyle(getNameGroupName(emailConv))

            getAGroupMessage(emailConv).forEach {
                val messageDetails = NotificationCompat.MessagingStyle.Message(
                    it, System.currentTimeMillis(), getNameGroupName(emailConv)
                )

                msgStyle.addMessage(messageDetails)
            }

            builder.setStyle(msgStyle)
        }

        if (smallIconBitmap != null) {
            builder.setLargeIcon(smallIconBitmap)
        }

        with(NotificationManagerCompat.from(context)) {
            if (emailConv == null) notify((11..999).random(), builder.build())
            else notify(emailConv.hashCode().absoluteValue, builder.build())
        }
    }

    private suspend fun loadImageFromURL(url: String?) = withContext(Dispatchers.IO) {
        if (url == null) return@withContext null
        return@withContext try {
            Glide.with(context).asBitmap().load(url).transform(CircleCrop()).submit().get()
        } catch (e: Exception) {
            null
        }
    }
}