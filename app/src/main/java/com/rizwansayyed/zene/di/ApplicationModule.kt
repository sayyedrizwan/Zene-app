package com.rizwansayyed.zene.di

import android.app.Application
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ApplicationModule : Application() {
    companion object {
        @Volatile
        lateinit var context: ApplicationModule
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}