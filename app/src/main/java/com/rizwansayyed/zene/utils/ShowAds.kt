package com.rizwansayyed.zene.utils

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.DataStoreManager.lastAdsTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.sponsorsAdsDB
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.IDs.AD_INTERSTITIAL_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.IDs.AD_REWARDS_ID
import com.rizwansayyed.zene.utils.Utils.IDs.AD_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInMinutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class ShowAdsOnAppOpen(private val activity: Activity) {

    private val listener = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(p0: AppOpenAd) {
            super.onAdLoaded(p0)
            logEvents(FirebaseLogEvents.FirebaseEvents.LOADED_APP_OPEN_ADS)
            p0.show(activity)
            lastAdsTimestamp = flowOf(System.currentTimeMillis())
        }
    }

    private val rewardsListener = object : RewardedAdLoadCallback() {
        override fun onAdLoaded(p0: RewardedAd) {
            super.onAdLoaded(p0)
            logEvents(FirebaseLogEvents.FirebaseEvents.LOADED_REWARDS_ADS)
            p0.show(activity) {
                if (it.amount > 0) {
                    Log.d("TAG", "onAdLoaded:")
                }
            }
        }
    }
    private val iListener = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            logEvents(FirebaseLogEvents.FirebaseEvents.LOADED_INTERSTITIAL_ADS)
            p0.show(activity)
            lastAdsTimestamp = flowOf(System.currentTimeMillis())
        }
    }

    fun interstitialAds() = CoroutineScope(Dispatchers.Main).launch {
        if (userInfoDB.firstOrNull()?.isLoggedIn() == false) return@launch
        if (BuildConfig.DEBUG) return@launch
        val doShowAds = isMoreThanTimeAds()
        if (!doShowAds) return@launch

        val showViaSponsorAds =
            withContext(Dispatchers.IO) { sponsorsAdsDB.firstOrNull()?.showAds ?: true }

        if (!showViaSponsorAds) return@launch

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, AD_INTERSTITIAL_UNIT_ID, adRequest, iListener)
    }

    fun showAds() = CoroutineScope(Dispatchers.Main).launch {
        if (userInfoDB.firstOrNull()?.isLoggedIn() == false) return@launch
        if (BuildConfig.DEBUG) return@launch
        val doShowAds = isMoreThanTimeAds()
        if (!doShowAds) return@launch

        val showViaSponsorAds =
            withContext(Dispatchers.IO) { sponsorsAdsDB.firstOrNull()?.showAds ?: true }

        if (!showViaSponsorAds) return@launch

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, AD_UNIT_ID, request, listener)
    }

    fun showRewardsAds() = CoroutineScope(Dispatchers.Main).launch {
        val request = AdRequest.Builder().build()
        RewardedAd.load(activity, AD_REWARDS_ID, request, rewardsListener)
    }
}

suspend fun isMoreThanTimeAds(): Boolean = withContext(Dispatchers.IO) {
    val timestamp = timeDifferenceInMinutes(lastAdsTimestamp.firstOrNull())
    return@withContext timestamp >= 5
}