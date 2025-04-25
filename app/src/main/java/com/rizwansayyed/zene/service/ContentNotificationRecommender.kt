package com.rizwansayyed.zene.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.datastore.DataStorageManager.lastNotificationGeneratedTSDB
import com.rizwansayyed.zene.datastore.DataStorageManager.lastNotificationSuggestedType
import com.rizwansayyed.zene.service.notification.NotificationUtils
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.OTHER_NOTIFICATION
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.OTHER_NOTIFICATION_DESC
import com.rizwansayyed.zene.ui.main.MainActivity
import com.rizwansayyed.zene.utils.FirebaseEvents.FirebaseEventsParams
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ContentNotificationRecommender(
    private val context: Context, private val zeneAPI: ZeneAPIImplementation
) {
    init {
        start()
    }

    fun start() = CoroutineScope(Dispatchers.IO).launch {
        zeneAPI.notificationRecommendation().catch { }.collectLatest {
            try {
                registerEvents(FirebaseEventsParams.GENERATED_CUSTOM_NOTIFICATION)
                NotificationUtils(it.title!!, it.body!!).apply {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        data = it.url?.toUri()
                    }
                    setSmallImage(it.img)
                    channel(OTHER_NOTIFICATION, OTHER_NOTIFICATION_DESC)
                    setIntent(intent)
                    generate()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    it.ts?.let { ts -> lastNotificationGeneratedTSDB = flowOf(ts) }
                    if (isActive) cancel()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    it.type?.let { ts -> lastNotificationSuggestedType = flowOf(ts) }
                    if (isActive) cancel()
                }

            } catch (e: Exception) {
                e.message
            }
        }
    }
}