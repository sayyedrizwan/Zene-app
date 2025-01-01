package com.rizwansayyed.zene.di

import android.app.Application
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
    }
}