package com.rizwansayyed.zene.service

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.utils.ChatTempDataUtils.addAMessage
import com.rizwansayyed.zene.utils.ChatTempDataUtils.addAName
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class FirebaseAppMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var zeneAPI: ZeneAPIImplementation

    private val c: Context by lazy { this }

    companion object {
        const val CONNECT_LOCATION_SHARING_TYPE = "CONNECT_LOCATION_SHARE"
        const val CONNECT_OPEN_PROFILE_TYPE = "CONNECT_OPEN_PROFILE"
        const val CONNECT_SEND_FRIEND_REQUEST = "CONNECT_SEND_FRIEND_REQUEST"
        const val CONNECT_ACCEPTED_FRIEND_REQUEST = "CONNECT_ACCEPTED_FRIEND_REQUEST"
        const val CONNECT_SEND_CHAT_MESSAGE = "CONNECT_SEND_CHAT_MESSAGE"
        const val FCM_NAME = "name"
        const val FCM_TITLE = "title"
        const val FCM_BODY = "body"
        const val FCM_IMAGE = "image"
        const val FCM_EMAIL = "email"
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
            accessNewToken()

            if (type == CONNECT_LOCATION_SHARING_TYPE) connectLocationAlert(message.data)
            if (type == CONNECT_SEND_FRIEND_REQUEST) connectFriendRequestAlert(message.data)
            if (type == CONNECT_ACCEPTED_FRIEND_REQUEST) connectAcceptedRequestAlert(message.data)
            if (type == CONNECT_SEND_CHAT_MESSAGE) connectChatMessageAlert(message.data)
        }
    }

    private fun accessNewToken() = CoroutineScope(Dispatchers.IO).launch {
        zeneAPI.updateUser().catch {}.collectLatest {
            DataStorageManager.userInfo = flowOf(it)
        }
    }

    private fun connectLocationAlert(data: MutableMap<String, String>) {
        val name = data[FCM_NAME]
        val body = data[FCM_BODY]
        val type = data[FCM_TYPE]
        val lat = data[FCM_LAT]
        val lon = data[FCM_LON]

        val title = String.format(
            Locale.getDefault(),
            context.getString(R.string.shared_their_current_location_tap_to_view),
            name
        )
        NotificationUtils(title, body ?: "").apply {
            val intent = Intent(c, MainActivity::class.java).apply {
                putExtra(FCM_TITLE, title)
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

    private fun connectFriendRequestAlert(data: MutableMap<String, String>) {
        val name = data[FCM_NAME]
        val image = data[FCM_IMAGE]
        val email = data[FCM_EMAIL]

        val title = String.format(
            Locale.getDefault(), context.getString(R.string.sent_you_a_friend_request), name
        )

        val body = context.getString(R.string.tap_to_view_their_friend_request)

        NotificationUtils(title, body).apply {
            val intent = Intent(c, MainActivity::class.java).apply {
                putExtra(Intent.ACTION_SENDTO, CONNECT_OPEN_PROFILE_TYPE)
                putExtra(FCM_EMAIL, email)
            }
            channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC)
            setIntent(intent)
            setSmallImage(image)
            generate()
        }
    }

    private fun connectAcceptedRequestAlert(data: MutableMap<String, String>) {
        val name = data[FCM_NAME]
        val image = data[FCM_IMAGE]
        val email = data[FCM_EMAIL]

        val title = String.format(
            Locale.getDefault(), context.getString(R.string.is_now_your_friend), name
        )

        val body = context.getString(R.string.is_now_your_friend_desc)

        NotificationUtils(title, body).apply {
            val intent = Intent(c, MainActivity::class.java).apply {
                putExtra(Intent.ACTION_SENDTO, CONNECT_OPEN_PROFILE_TYPE)
                putExtra(FCM_EMAIL, email)
            }
            channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC)
            setIntent(intent)
            setSmallImage(image)
            generate()
        }
    }

    private fun connectChatMessageAlert(data: MutableMap<String, String>) {
        val name = data[FCM_NAME]
        val body = data[FCM_BODY]
        val image = data[FCM_IMAGE]
        val email = data[FCM_EMAIL]

//        if (email == currentOpenedChatProfile) return

        addAMessage(email, body)
        addAName(email, name)

        val title = String.format(
            Locale.getDefault(), context.getString(R.string.send_you_a_message), name
        )

        NotificationUtils(title, body ?: "").apply {
            val intent = Intent(c, MainActivity::class.java).apply {
                putExtra(Intent.ACTION_SENDTO, CONNECT_SEND_CHAT_MESSAGE)
                putExtra(FCM_EMAIL, email)
            }
            channel(CONNECT_UPDATES_NAME, CONNECT_UPDATES_NAME_DESC)
            setIntent(intent)
            setSmallImage(image)
            generateMessageConv(email)
            generate()
        }
    }
}