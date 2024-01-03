package com.rizwansayyed.zene.service.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.OFFERS_CHANNEL
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.OFFERS_CHANNEL_ID

class FirebaseMessagingReceiver : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        RegisterOnFCM()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            it.title ?: return

            registerEvent(FirebaseEvents.FirebaseEvent.RECEIVED_FCM_NOTIFICATION)

            NotificationViewManager(this)
                .title(it.title!!).body(it.body ?: "")
                .image(if (it.imageUrl != null) it.imageUrl?.path else null)
                .nIds(OFFERS_CHANNEL_ID, OFFERS_CHANNEL).generate()
        }
    }
}