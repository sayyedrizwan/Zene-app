package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.service.MusicPlayService
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
        startMusicService()
    }


    private fun startMusicService() {
        Intent(this, MusicPlayService::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startService(this)
        }
    }
}