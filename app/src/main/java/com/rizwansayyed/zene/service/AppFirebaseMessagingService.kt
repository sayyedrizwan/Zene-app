package com.rizwansayyed.zene.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.db.DataStoreManager
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.NotificationIDS.NOTIFICATION_CHANNEL_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImpl

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            delay(3.seconds)
            logEvents(FirebaseLogEvents.FirebaseEvents.UPDATED_USER_INFO)
            zeneAPI.updateUser().catch { }.collectLatest { }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        CoroutineScope(Dispatchers.IO).launch {
            message.notification?.let {
                if (it.title != null) {
                    val name =
                        if (DataStoreManager.userInfoDB.firstOrNull()?.name == null) "Zene user" else DataStoreManager.userInfoDB.firstOrNull()?.name
                    val title = it.title!!.replace("[[user]]", name ?: "Zene user")
                    val body = it.body ?: "".replace("[[user]]", name ?: "Zene user")
                    logEvents(FirebaseLogEvents.FirebaseEvents.RECEIVED_NOTIFICATION)
                    NotificationUtils(title, body, it.imageUrl)
                }
            }
        }
    }
}