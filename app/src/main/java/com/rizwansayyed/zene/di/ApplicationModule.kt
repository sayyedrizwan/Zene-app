package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Intent
import android.util.Log
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.presenter.MainActivity
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.utils.NotificationViewManager
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL
import com.rizwansayyed.zene.utils.NotificationViewManager.Companion.CRASH_CHANNEL_ID
import com.rizwansayyed.zene.utils.Utils.ifPlayerServiceNotRunning
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ApplicationModule : Application() {
    companion object {
        @Volatile
        lateinit var context: ApplicationModule
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        if (!ifPlayerServiceNotRunning()) {
            context.startService(Intent(context, PlayerService::class.java))
        }


        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            context.cacheDir.deleteRecursively()

            if (BuildConfig.DEBUG)
                NotificationViewManager(this)
                    .title("The App crash on ${thread.name} thread")
                    .body(e.message ?: "No Crash Registered")
                    .nIds(CRASH_CHANNEL_ID, CRASH_CHANNEL).generate()

            Log.d(packageName, "App Crash Log: ${e.message}")

            Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(this)
            }
        }

//        FirebaseApp.initializeApp(this)
    }

}