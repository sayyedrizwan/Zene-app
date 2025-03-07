package com.rizwansayyed.zene.service

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.CONNECT_UPDATES_NAME
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.CONNECT_UPDATES_NAME_DESC
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
class FirebaseAppMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    private val c: Context by lazy { this }

    companion object {
        const val CONNECT_LOCATION_SHARING_TYPE = "CONNECT_LOCATION_SHARE"
        const val FCM_TITLE = "title"
        const val FCM_BODY = "body"
        const val FCM_TYPE = "type"
        const val FCM_LAT = "lat"
        const val FCM_LON = "lon"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            delay(3.seconds)
            val data = DataStorageManager.userInfo.firstOrNull()
            if (data?.isLoggedIn() == false) return@launch

            zeneAPI.updateUser().catch { }.collectLatest { }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.data.let {
            val type = message.data[FCM_TYPE]
            if (type == CONNECT_LOCATION_SHARING_TYPE)
                connectLocationAlert(message.data)
        }
    }

    private fun connectLocationAlert(data: MutableMap<String, String>) {
        val name = data[FCM_TITLE]
        val body = data[FCM_BODY]
        val type = data[FCM_TYPE]
        val lat = data[FCM_LAT]
        val lon = data[FCM_LON]

        name?.let { it1 ->
            NotificationUtils(it1, body ?: "").apply {
                val intent = Intent(c, MainActivity::class.java).apply {
                    putExtra(FCM_TITLE, name)
                    putExtra(FCM_BODY, body)
                    putExtra(FCM_TYPE, type)
                    putExtra(FCM_LAT, lat)
                    putExtra(FCM_LON, lon)
                }
                channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC)
                setIntent(intent)
                generate()
            }
        }
    }
}