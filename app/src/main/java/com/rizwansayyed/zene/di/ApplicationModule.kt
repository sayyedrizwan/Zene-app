package com.rizwansayyed.zene.di

import android.app.Application
import com.google.firebase.FirebaseApp
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

//        FirebaseApp.initializeApp(this)
    }

}