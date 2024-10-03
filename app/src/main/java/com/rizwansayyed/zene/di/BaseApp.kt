package com.rizwansayyed.zene.di

import android.app.Application
import android.webkit.WebView
import com.facebook.FacebookSdk
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.BuildConfig
import dagger.hilt.android.HiltAndroidApp

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
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
    }
}