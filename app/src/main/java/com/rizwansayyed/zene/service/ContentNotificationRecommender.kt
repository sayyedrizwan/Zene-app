package com.rizwansayyed.zene.service

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import com.rizwansayyed.zene.service.notification.NotificationUtils
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.OTHER_NOTIFICATION
import com.rizwansayyed.zene.service.notification.NotificationUtils.Companion.OTHER_NOTIFICATION_DESC
import com.rizwansayyed.zene.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContentNotificationRecommender(
    private val context: Context, private val zeneAPI: ZeneAPIImplementation
) {
    init {
        start()
    }

    fun start() = CoroutineScope(Dispatchers.IO).launch {
        zeneAPI.notificationRecommendation().catch { }.collectLatest {
            it.title ?: return@collectLatest
            it.body ?: return@collectLatest

            NotificationUtils(it.title, it.body).apply {
                val intent = Intent(context, MainActivity::class.java).apply {
                    data = it.url?.toUri()
                }
                channel(OTHER_NOTIFICATION, OTHER_NOTIFICATION_DESC)
                setIntent(intent)
                generate()
            }
        }
    }
}