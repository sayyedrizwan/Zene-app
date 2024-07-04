package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Context

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