package com.rizwansayyed.zene

import android.app.Application
import com.rizwansayyed.zene.datastore.DataStoreManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        @Volatile
        lateinit var context: BaseApplication

        @Volatile
        lateinit var dataStoreManager: DataStoreManager
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        dataStoreManager = DataStoreManager()
    }
}