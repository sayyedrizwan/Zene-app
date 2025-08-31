package com.rizwansayyed.zene.di

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.ads.nativeAdsMap
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.stopAppService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val activityManager by lazy {
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }
    private var monitorJob: Job? = null
    val observer = object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            nativeAdsMap.clear()
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            monitorJob = CoroutineScope(Dispatchers.IO).safeLaunch {
                while (isActive) {
                    delay(2000)
                    val tasks = activityManager.appTasks
                    if (tasks.isEmpty()) {
                        stopAppService(context)
                        break
                    }
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
    }

}