package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Context
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
    }
}