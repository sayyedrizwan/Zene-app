package com.rizwansayyed.zene.di

import android.app.Application
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@HiltAndroidApp
class ZeneBaseApplication : Application() {

    companion object {
        @Volatile
        lateinit var context: ZeneBaseApplication
    }

    val observer = object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)

        val config = BundledEmojiCompatConfig(this, ContextCompat.getMainExecutor(this))
        EmojiCompat.init(config)

        CoroutineScope(Dispatchers.IO).launch {
            delay(2.seconds)
            try {
                val data = DataStorageManager.musicPlayerDB.firstOrNull()
                if (data?.data != null)
                    MediaContentUtils.startMedia(data.data, data.lists ?: emptyList(), true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (isActive) cancel()
        }
    }

}