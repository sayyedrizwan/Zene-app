package com.rizwansayyed.zene.service

import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.getAllPhoneCode
import com.rizwansayyed.zene.utils.Utils.getContactName
import com.rizwansayyed.zene.utils.Utils.getCurrentPhoneCountryCode
import com.rizwansayyed.zene.utils.Utils.hasCountryCode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONObject
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
        if (message.data["type"] == "vibes") CoroutineScope(Dispatchers.IO).launch {
            generateVibes(message.data)
            if (isActive) cancel()
        }
    }


    private suspend fun generateVibes(message: MutableMap<String, String>) {
        val number = message["number"] ?: ""
        val toNumber = message["tonumber"] ?: ""

        if (toNumber != (userInfoDB.firstOrNull()?.phonenumber ?: "")) return
        val countryCodes = getAllPhoneCode()
        val phoneNumberCode = getCurrentPhoneCountryCode()
        val hasCountryCode = hasCountryCode(number, countryCodes)
        val finalNumber = if (hasCountryCode) number else "+${phoneNumberCode}${number}"
        if (number.length > 2) {
            val dbName = JSONObject(message["info"] ?: "{}").optString("name")
            val name = getContactName(finalNumber) ?: dbName
            NotificationUtils(
                "${message["title"]?.replace("{contact_name}", name)}",
                message["body"] ?: "", message["photo"]?.toUri()
            )
        }

        zeneAPI.getVibes().catch { }.collectLatest { }
    }
}