package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Intent
import android.os.Build
import com.google.firebase.FirebaseApp
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.ifPlayerServiceNotRunningRun
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@HiltAndroidApp
class ApplicationModule : Application() {
    companion object {
        @Volatile
        lateinit var context: ApplicationModule
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        ifPlayerServiceNotRunningRun()

//        FirebaseApp.initializeApp(this)
    }

}