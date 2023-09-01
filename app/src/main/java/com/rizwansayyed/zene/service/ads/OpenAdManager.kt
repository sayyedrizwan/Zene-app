package com.rizwansayyed.zene.service.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.utils.DateTime.isMoreThan25Minute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenAdManager(private val context: Activity) {

    private var appOpenAdManager: AppOpenAdManager = AppOpenAdManager()

    private val adUnitID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/3419835294" else "ca-app-pub-2941808068005217/7650500204"

    fun loadAd() = CoroutineScope(Dispatchers.IO).launch {
        if (dataStoreManager.lastOpenAdLoaded.first().isMoreThan25Minute())
            withContext(Dispatchers.Main) {
                appOpenAdManager.loadAd(context)
            }
    }


    inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false

        fun loadAd(context: Activity) {
            val request = AdRequest.Builder().build()

            val callback = object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    super.onAdLoaded(ad)
                    appOpenAd = ad
                    isLoadingAd = false
                    dataStoreManager.lastOpenAdLoaded = flowOf(System.currentTimeMillis())
                    ad.show(context)
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    isLoadingAd = false
                }
            }

            AppOpenAd.load(context, adUnitID, request, callback)
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null
        }
    }
}