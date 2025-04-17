package com.rizwansayyed.zene.utils.ads

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInMinutes
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.ads.OpenAppAdsUtils.Companion.lastAppOpenLoadTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InterstitialAdsUtils(
    val activity: Activity,
    private val showSimple: Boolean = false,
    private val forceShow: Boolean = false,
) {

    private val adID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-2941808068005217/1745328219"

    private val simpleAdID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1033173712" else "ca-app-pub-2941808068005217/3297767085"


    init { startAds() }

    private val listener = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(ad: InterstitialAd) {
            super.onAdLoaded(ad)
            if (forceShow) {
                ad.show(activity)
                return
            }

            if (lastAppOpenLoadTime == null) {
                ad.show(activity)
                lastAppOpenLoadTime = System.currentTimeMillis()
            } else if (timeDifferenceInMinutes(lastAppOpenLoadTime!!) >= 4) {
                ad.show(activity)
                lastAppOpenLoadTime = System.currentTimeMillis()
            }
        }
    }

    private fun startAds() = CoroutineScope(Dispatchers.Main).launch {
        if (BuildConfig.DEBUG) return@launch
        val isPremium = withContext(Dispatchers.IO) { isPremiumDB.firstOrNull() }
        if (isPremium == true) return@launch

        val request = AdRequest.Builder().build()
        InterstitialAd.load(context, if (showSimple) simpleAdID else adID, request, listener)
    }
}