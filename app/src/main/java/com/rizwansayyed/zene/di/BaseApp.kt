package com.rizwansayyed.zene.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import com.facebook.FacebookSdk
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.service.MusicPlayService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds

@HiltAndroidApp
class BaseApp : Application() {

    companion object {
        @Volatile
        lateinit var context: BaseApp
    }


    override fun onCreate() {
        super.onCreate()
        context = this
        FacebookSdk.sdkInitialize(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }
}