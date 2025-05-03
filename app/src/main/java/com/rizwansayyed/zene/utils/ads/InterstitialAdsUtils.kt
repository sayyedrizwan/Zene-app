package com.rizwansayyed.zene.utils.ads

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.datastore.DataStorageManager.lastInterstitialLoadTimeDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInMinutes
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

class InterstitialAdsUtils(
    val activity: Activity,
    private val showSimple: Boolean = false,
) {

    private val adID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-2941808068005217/1745328219"

    private val simpleAdID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-2941808068005217/3297767085"

    companion object {
        var interstitialAdsJob: Job? = null
    }

    init {
        interstitialAdsJob?.cancel()
        interstitialAdsJob = CoroutineScope(Dispatchers.IO).launch {
            delay(1.seconds)
            startAds()

            if (isActive) cancel()
        }
    }

    private val listener = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(ad: InterstitialAd) {
            super.onAdLoaded(ad)

            CoroutineScope(Dispatchers.Main).launch {
                val time = withContext(Dispatchers.IO) { lastInterstitialLoadTimeDB.firstOrNull() }
                if (time == null) {
                    ad.show(activity)
                    lastInterstitialLoadTimeDB = flowOf(System.currentTimeMillis())
                } else if (timeDifferenceInMinutes(time) >= 5) {
                    ad.show(activity)
                    lastInterstitialLoadTimeDB = flowOf(System.currentTimeMillis())
                }

                if (isActive) cancel()
            }
        }
    }

    private fun startAds() = CoroutineScope(Dispatchers.Main).launch {
        val isLoggedIn = withContext(Dispatchers.IO) { userInfo.firstOrNull()?.isLoggedIn() }
        if (BuildConfig.DEBUG) return@launch
        if (isLoggedIn == false) return@launch
        val isPremium = withContext(Dispatchers.IO) { isPremiumDB.firstOrNull() }
        if (isPremium == true) return@launch

        val request = AdRequest.Builder().build()
        InterstitialAd.load(context, if (showSimple) simpleAdID else adID, request, listener)
    }
}