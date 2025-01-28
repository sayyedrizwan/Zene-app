package com.rizwansayyed.zene.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.datastore.DataStorageManager
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

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            delay(3.seconds)
            val data = DataStorageManager.userInfo.firstOrNull()
            if (data?.isLoggedIn() == false) return@launch

            zeneAPI.updateUser(data?.email ?: "", data?.name ?: "", data?.photo ?: "")
                .catch { }.collectLatest { }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("TAG", "onMessageReceived: dd ${message.data}")
    }
}