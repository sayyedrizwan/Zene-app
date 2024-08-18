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
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.lastAdsTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.rewardsWatchedAds
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.rewards.updateTheAdsLogs
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.IDs.AD_INTERSTITIAL_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.IDs.AD_REWARDS_ID
import com.rizwansayyed.zene.utils.Utils.IDs.AD_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant


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
                    CoroutineScope(Dispatchers.IO).launch {
                        val ads = rewardsWatchedAds.firstOrNull() ?: 0
                        rewardsWatchedAds = flowOf(ads + 1)
                        if (isActive) cancel()
                    }
                    updateTheAdsLogs()
                }
            }
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            activity.resources.getString(R.string.no_ads_found)
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

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, AD_INTERSTITIAL_UNIT_ID, adRequest, iListener)
    }

    fun showAds() = CoroutineScope(Dispatchers.Main).launch {
        if (userInfoDB.firstOrNull()?.isLoggedIn() == false) return@launch
        if (BuildConfig.DEBUG) return@launch
        val doShowAds = isMoreThanTimeAds()
        if (!doShowAds) return@launch

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, AD_UNIT_ID, request, listener)
    }

    fun showRewardsAds() = CoroutineScope(Dispatchers.Main).launch {
        val request = AdRequest.Builder().build()
        RewardedAd.load(activity, AD_REWARDS_ID, request, rewardsListener)
    }
}

suspend fun isMoreThanTimeAds(): Boolean = withContext(Dispatchers.IO) {
    val timestamp = lastAdsTimestamp.firstOrNull() ?: TS_LAST_DATA
    val now = Instant.now().toEpochMilli()
    val duration =
        Duration.between(Instant.ofEpochMilli(timestamp), Instant.ofEpochMilli(now))
    return@withContext duration.toMinutes() > 6
}