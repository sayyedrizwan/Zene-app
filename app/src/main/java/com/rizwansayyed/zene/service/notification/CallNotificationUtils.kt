@file:Suppress("DEPRECATION")

package com.rizwansayyed.zene.service.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.service.FirebaseAppMessagingService
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_CALL_NOTIFICATION
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.CONNECT_CALL_NOTIFICATION_DESC
import com.rizwansayyed.zene.service.partycall.PartyCallActivity

const val FLAG = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

fun callNotification(
    name: String?, image: String?, email: String?, context: FirebaseAppMessagingService
) {
    createNotificationChannel()

    val b = Glide.with(context).asBitmap().load(image).submit(200, 200).get()

    val random = (222..1234).random()


    val incomingCaller = Person.Builder()
        .setName(name).setIcon(IconCompat.createWithAdaptiveBitmap(b)).setImportant(true).build()
    val channelId = CONNECT_CALL_NOTIFICATION.lowercase().replace(" ", "_")

    val callerStyle = NotificationCompat.CallStyle.forIncomingCall(
        incomingCaller, generatePI(random, email, 1), generatePI(random, email, 2)
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(name)
        .setContentText(context.getString(R.string.wants_to_start_live_party_session))
        .setSmallIcon(R.drawable.zene_logo)
        .setContentIntent(generatePI(random, email, 0))
        .setStyle(callerStyle)
        .addPerson(incomingCaller)
        .setSound(getUriSoundFile())
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(NotificationCompat.CATEGORY_CALL)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setFullScreenIntent(generatePI(random, email, 0), true)
        .setOngoing(true)
        .setAutoCancel(false)
        .setTimeoutAfter(20000)
        .build()


    notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
    notification.flags = Notification.FLAG_INSISTENT
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(random, notification)
}

private fun generatePI(random: Int, email: String?, i: Int): PendingIntent {
    val intent = Intent(context, PartyCallActivity::class.java).apply {
        putExtra(Intent.EXTRA_UID, random)
        putExtra(Intent.EXTRA_EMAIL, email)
        putExtra(Intent.EXTRA_MIME_TYPES, i)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    return PendingIntent.getActivity(context, random, intent, FLAG)
}

private fun createNotificationChannel() {
    val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
        .setFlags(Notification.FLAG_INSISTENT)
        .build()

    val channelId = CONNECT_CALL_NOTIFICATION.lowercase().replace(" ", "_")

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, CONNECT_CALL_NOTIFICATION, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CONNECT_CALL_NOTIFICATION_DESC
            setShowBadge(false)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(getUriSoundFile(), audioAttributes)
        }


        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}


private fun getUriSoundFile() =
    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.ring_phone_audio)
