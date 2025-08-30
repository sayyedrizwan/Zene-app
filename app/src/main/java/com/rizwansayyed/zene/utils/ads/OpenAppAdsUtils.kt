package com.rizwansayyed.zene.utils.ads

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.datastore.DataStorageManager.lastLoadTimeDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInMinutes
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class OpenAppAdsUtils(val activity: Activity) {

    private val adID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else "ca-app-pub-2941808068005217/7650500204"

    companion object {
        var openAdsJob: Job? = null
    }

    init {
        openAdsJob?.cancel()
        openAdsJob = CoroutineScope(Dispatchers.IO).safeLaunch {
            delay(1.seconds)
            startAds()

            if (isActive) cancel()
        }
    }

    private val listener = object : AppOpenAdLoadCallback() {
        override fun onAdLoaded(ad: AppOpenAd) {
            super.onAdLoaded(ad)
            CoroutineScope(Dispatchers.Main).safeLaunch(Dispatchers.Main) {
                val time = withContext(Dispatchers.IO) { lastLoadTimeDB.firstOrNull() }
                if (time == null) {
                    ad.show(activity)
                    lastLoadTimeDB = flowOf(System.currentTimeMillis())
                } else if (timeDifferenceInMinutes(time) >= 3) {
                    ad.show(activity)
                    lastLoadTimeDB = flowOf(System.currentTimeMillis())
                }

                delay(1.seconds)
                MobileAds.setAppMuted(false)

                if (isActive) cancel()
            }
        }
    }

    private fun startAds() = CoroutineScope(Dispatchers.Main).safeLaunch(Dispatchers.Main) {
        val isLoggedIn = withContext(Dispatchers.IO) { userInfo.firstOrNull()?.isLoggedIn() }
        if (BuildConfig.DEBUG) return@safeLaunch
        if (isLoggedIn == false) return@safeLaunch
        val isPremium = withContext(Dispatchers.IO) { isPremiumDB.firstOrNull() }
        if (isPremium == true) return@safeLaunch

        val isMusicPlaying = withContext(Dispatchers.IO) {
            DataStorageManager.musicPlayerDB.firstOrNull()?.isPlaying() == true
        }
        MobileAds.setAppMuted(isMusicPlaying)

        val request = AdRequest.Builder().build()
        AppOpenAd.load(context, adID, request, listener)
    }
}