package com.rizwansayyed.zene.di

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import dagger.hilt.android.HiltAndroidApp

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
    }
}