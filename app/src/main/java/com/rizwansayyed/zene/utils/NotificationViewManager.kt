package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import coil.ImageLoader
import coil.request.ImageRequest
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.utils.Utils.printStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationViewManager(private val context: Context) {

    companion object {
        const val DEFAULT_CHANNEL_ID = "channel_notification_id"
        const val DEFAULT_CHANNEL = "Zene Notification channel_notification"


        const val CRASH_CHANNEL_ID = "crash_channel_notification_id"
        const val CRASH_CHANNEL = "Crash Notification crash_channel_notification"


        const val OFFERS_CHANNEL_ID = "offers_channel_notification_id"
        const val OFFERS_CHANNEL = "Offer"


        const val ALARM_CHANNEL_ID = "alarm_channel_notification_id"
        const val ALARM_DEFAULT_CHANNEL = "Alarm"


        const val PARTY_CHANNEL_ID = "song_party_channel_notification_id"
        const val PARTY_DEFAULT_CHANNEL = "Song Party"


        const val FLAGS: Int = PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE

        val soundUri: Uri =
            Uri.parse("android.resource://${context.packageName}/${R.raw.notification_sound}")
    }

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private var notificationBuilder: NotificationCompat.Builder? = null
    private var intentInternal: Intent = Intent(context, MainActivity::class.java)
    private var titleInternal: String? = null
    private var bodyInternal: String? = null
    private var image: String? = null
    private var channelId: String = DEFAULT_CHANNEL_ID
    private var channelName: String? = DEFAULT_CHANNEL

    fun <T> intentClass(c: Class<T>) = apply {
        intentInternal = Intent(context, c)
    }

    fun nIds(id: String, name: String) = apply {
        channelId = id
        channelName = name
    }

    fun image(path: String?) = apply {
        image = path
    }

    fun title(s: String) = apply {
        titleInternal = s
    }

    fun body(b: String) = apply {
        bodyInternal = b
    }


    @SuppressLint("MissingPermission")
    fun generate() = CoroutineScope(Dispatchers.IO).launch {
        var bitmap: Bitmap? = null
        if (image != null) {
            val request = ImageRequest.Builder(context).data(image)
                .target(onSuccess = { result ->
                    bitmap = (result as BitmapDrawable).bitmap
                }).build()
            ImageLoader.Builder(ApplicationModule.context).crossfade(true).build().enqueue(request)
        }

        val p = PendingIntent.getActivity(context, 1, intentInternal, FLAGS)

        notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.logo_color)
            setContentTitle(titleInternal)
            setContentText(bodyInternal)
            setAutoCancel(false)
            bitmap?.let { setLargeIcon(it) }
            setStyle(NotificationCompat.BigTextStyle().bigText(bodyInternal))
            setSound(soundUri)
            setContentIntent(p)
            priority = NotificationCompat.PRIORITY_HIGH
        }

        createNotificationChannel()

        with(NotificationManagerCompat.from(context)) {
            try {
                notificationBuilder?.build()?.let { notify((4..999).random(), it) }
            } catch (e: Exception) {
                e.printStack()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)

            notificationBuilder?.setDefaults(Notification.DEFAULT_VIBRATE)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            mChannel.setSound(soundUri, audioAttributes)
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}