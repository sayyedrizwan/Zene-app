package com.rizwansayyed.zene.service

import android.util.Log
import com.rizwansayyed.zene.data.implementation.ZeneAPIImplementation

class ContentNotificationRecommender(zeneAPI: ZeneAPIImplementation) {
    init { start() }

    fun start() {
        Log.d("TAG", "start: runnedd on 111")
    }
}