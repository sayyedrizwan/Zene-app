package com.rizwansayyed.zene.di

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.ads.nativeAdsMap
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.seconds


@HiltAndroidApp
class ZeneBaseApplication : Application() {

    companion object {
        @Volatile
        lateinit var context: ZeneBaseApplication

        @Volatile
        var isPlayerLoaded = false

        fun startDefaultMedia() {
            if (!isPlayerLoaded) CoroutineScope(Dispatchers.IO).safeLaunch {
                delay(6.seconds)
                try {
                    val data = DataStorageManager.musicPlayerDB.firstOrNull()
                    if (data?.data != null)
                        MediaContentUtils.startMedia(data.data, data.lists ?: emptyList(), true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (isActive) cancel()
            }

            isPlayerLoaded = true
        }
    }


    val observer = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            nativeAdsMap.clear()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
    }

}