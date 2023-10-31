package com.rizwansayyed.zene.di

import android.app.Application
import android.content.Intent
import com.google.firebase.ktx.Firebase
import com.rizwansayyed.zene.service.PlayerService
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.ifPlayerServiceNotRunning
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
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

        if (!ifPlayerServiceNotRunning()){
            context.startService(Intent(context, PlayerService::class.java))
        }


//        FirebaseApp.initializeApp(this)
    }

}