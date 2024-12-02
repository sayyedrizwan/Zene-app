package com.rizwansayyed.zene.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.facebook.FacebookSdk
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.ZeneAPIImpl
import com.rizwansayyed.zene.data.db.DataStoreManager.isUserPremiumDB
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltAndroidApp
class BaseApp : Application() {

    companion object {
        @Volatile
        lateinit var context: BaseApp
    }

    @Inject
    lateinit var zeneAPI: ZeneAPIImpl

    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
        override fun onActivityStarted(p0: Activity) {
            CoroutineScope(Dispatchers.IO).launch {
                delay(9.seconds)
                zeneAPI.isUserPremium().catch {
                    isUserPremiumDB = flowOf(false)
                }.collectLatest {
                    Log.d("TAG", "onActivityStarted: isPremium ${it.isPremium}")
                    isUserPremiumDB = flowOf(it.isPremium ?: false)
                }

                if (isActive) cancel()
            }
        }

        override fun onActivityResumed(p0: Activity) {}
        override fun onActivityPaused(p0: Activity) {}
        override fun onActivityStopped(p0: Activity) {}
        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
        override fun onActivityDestroyed(p0: Activity) {}

    }

    override fun onCreate() {
        super.onCreate()
        context = this
        FacebookSdk.sdkInitialize(this)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG

//        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

}