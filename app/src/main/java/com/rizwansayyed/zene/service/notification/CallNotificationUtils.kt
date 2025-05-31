@file:Suppress("DEPRECATION")

package com.rizwansayyed.zene.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_CALL_NOTIFICATION
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_CALL_NOTIFICATION_DESC
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.notificationManager
import com.rizwansayyed.zene.service.party.PartyServiceReceiver
import com.rizwansayyed.zene.ui.partycall.PartyCallActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.math.absoluteValue

const val FLAG = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
const val NOTIFICATION_TAG = "CALL_NOTIFICATION"

fun callNotification(
    name: String?, image: String?, email: String?, code: String?, context: Context
) {
    createNotificationChannel()

    val b = runBlocking(Dispatchers.IO) {
        try {
            Glide.with(context).asBitmap().load(image).submit(200, 200).get()
        } catch (e: Exception) {
            BitmapFactory.decodeResource(context.resources, R.drawable.zene_logo)
        }
    }

    val incomingCaller = Person.Builder()
        .setName(name).setIcon(IconCompat.createWithAdaptiveBitmap(b)).setImportant(true).build()

    val channelId = CONNECT_CALL_NOTIFICATION.lowercase().replace(" ", "_")

    val callerStyle = NotificationCompat.CallStyle.forScreeningCall(
        incomingCaller,
        generatePIDecline(email, image, name),
        generatePI(email, image, name, 1, code)
    )


    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(name)
        .setContentText(context.getString(R.string.wants_to_start_live_party_session))
        .setSmallIcon(R.drawable.zene_logo)
        .setContentIntent(generatePI(email, image, name, 0, code))
        .setStyle(callerStyle)
        .addPerson(incomingCaller)
        .setSound(getUriSoundFile(), AudioManager.STREAM_NOTIFICATION)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_CALL)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setFullScreenIntent(generatePI(email, image, name, 0, code), true)
        .setOngoing(true)
        .setAutoCancel(false)
        .setTimeoutAfter(20000)
        .build()


    notification.defaults = Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE
    notification.flags = Notification.FLAG_INSISTENT

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(NOTIFICATION_TAG, email.hashCode().absoluteValue, notification)
}

private fun generatePI(
    email: String?, image: String?, name: String?, i: Int, code: String?
): PendingIntent {
    val intent = Intent(context, PartyCallActivity::class.java).apply {
        putExtra(Intent.EXTRA_EMAIL, email)
        putExtra(Intent.EXTRA_USER, image)
        putExtra(Intent.EXTRA_PACKAGE_NAME, name)
        putExtra(Intent.EXTRA_MIME_TYPES, i)
        putExtra(Intent.EXTRA_TEXT, code)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    return PendingIntent.getActivity(context, name.hashCode(), intent, FLAG)
}

private fun generatePIDecline(email: String?, image: String?, name: String?): PendingIntent {
    val intent = Intent(context, PartyServiceReceiver::class.java).apply {
        putExtra(Intent.EXTRA_EMAIL, email)
        putExtra(Intent.EXTRA_USER, image)
        putExtra(Intent.EXTRA_PACKAGE_NAME, name)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    return PendingIntent.getBroadcast(context, (222..1234).random(), intent, FLAG)
}

private fun createNotificationChannel() {
    val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .build()

    val channelId = CONNECT_CALL_NOTIFICATION.lowercase().replace(" ", "_")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, CONNECT_CALL_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CONNECT_CALL_NOTIFICATION_DESC
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(getUriSoundFile(), attributes)
        }


        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}


fun clearCallNotification(email: String) {
    try {
        notificationManager.cancel(NOTIFICATION_TAG, email.hashCode().absoluteValue)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun getUriSoundFile() =
    (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.ring_phone_audio).toUri()
