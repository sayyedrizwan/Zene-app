package com.rizwansayyed.zene.service

import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContentNotificationRecommender(private val zeneAPI: ZeneAPIImplementation) {
    init {
        start()
    }

    fun start() = CoroutineScope(Dispatchers.IO).launch {
        zeneAPI.notificationRecommendation().catch { }.collectLatest { }
    }
}