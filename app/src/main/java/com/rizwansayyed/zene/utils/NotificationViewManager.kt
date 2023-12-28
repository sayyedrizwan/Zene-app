package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.utils.Utils.printStack

class NotificationViewManager(private val context: Context) {

    companion object {
        const val DEFAULT_CHANNEL_ID = "channel_notification_id"
        const val DEFAULT_CHANNEL = "channel_notification"


        const val CRASH_CHANNEL_ID = "crash_channel_notification_id"
        const val CRASH_CHANNEL = "crash_channel_notification"


        const val ALARM_CHANNEL_ID = "alarm_channel_notification_id"
        const val ALARM_DEFAULT_CHANNEL = "alarm_channel_notification"


        const val flags = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
    }

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private var intentInternal: Intent = Intent(context, MainActivity::class.java)
    private var titleInternal: String? = null
    private var bodyInternal: String? = null
    private var channelId: String = DEFAULT_CHANNEL_ID
    private var channelName: String? = DEFAULT_CHANNEL

    fun <T> intentClass(c: Class<T>) = apply {
        intentInternal = Intent(context, c)
    }

    fun nIds(id: String, name: String) = apply {
        channelId = id
        channelName = name
    }

    fun title(s: String) = apply {
        titleInternal = s
    }

    fun body(b: String) = apply {
        bodyInternal = b
    }


    @SuppressLint("MissingPermission")
    fun generate() {
        val p =
            PendingIntent.getActivity(context, 1, intentInternal, flags)

        val b = NotificationCompat.Builder(context, CRASH_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.logo_color)
            setContentTitle(titleInternal)
            setContentText(bodyInternal)
            setAutoCancel(false)
            setStyle(NotificationCompat.BigTextStyle().bigText(bodyInternal))
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            setContentIntent(p)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        createNotificationChannel()

        with(NotificationManagerCompat.from(context)) {
            try {
                notify((4..999).random(), b.build())
            } catch (e: Exception) {
                e.printStack()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CRASH_CHANNEL_ID, CRASH_CHANNEL, importance)

            notificationManager.createNotificationChannel(mChannel)
        }
    }
}