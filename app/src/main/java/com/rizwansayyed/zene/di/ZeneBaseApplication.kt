package com.rizwansayyed.zene.di

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.MediaContentUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@HiltAndroidApp
class ZeneBaseApplication : Application() {

    companion object {
        @Volatile
        lateinit var context: ZeneBaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        EmojiCompat.init(BundledEmojiCompatConfig(context, ContextCompat.getMainExecutor(this)))

        CoroutineScope(Dispatchers.IO).launch {
            delay(1.seconds)
            val data = DataStorageManager.musicPlayerDB.firstOrNull()
            if (data?.data != null) MediaContentUtils.startMedia(data.data, data.lists, true)
        }
    }
}