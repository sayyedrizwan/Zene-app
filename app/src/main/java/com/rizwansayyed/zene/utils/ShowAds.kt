package com.rizwansayyed.zene.utils

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.lastAdsTimestamp
import com.rizwansayyed.zene.utils.Utils.IDs.AD_INTERSTITIAL_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.IDs.AD_UNIT_ID
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.Instant


class ShowAdsOnAppOpen(private val activity: Activity) {

    private val listener = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(p0: AppOpenAd) {
            super.onAdLoaded(p0)
            p0.show(activity)
            lastAdsTimestamp = flowOf(System.currentTimeMillis())
        }
    }
    private val iListener = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(p0: InterstitialAd) {
            super.onAdLoaded(p0)
            p0.show(activity)
            lastAdsTimestamp = flowOf(System.currentTimeMillis())
        }
    }

    fun interstitialAds() = CoroutineScope(Dispatchers.Main).launch {
        if (BuildConfig.DEBUG) return@launch
        val doShowAds = isMoreThanThreeMinutesAds()
        if (!doShowAds) return@launch

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, AD_INTERSTITIAL_UNIT_ID, adRequest, iListener)
    }

    fun showAds() = CoroutineScope(Dispatchers.Main).launch {
        if (BuildConfig.DEBUG) return@launch
        val doShowAds = isMoreThanThreeMinutesAds()
        if (!doShowAds) return@launch

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, AD_UNIT_ID, request, listener)
    }
}

fun isMoreThanThreeMinutesAds(): Boolean = runBlocking(Dispatchers.IO) {
    val timestamp = lastAdsTimestamp.firstOrNull() ?: TS_LAST_DATA
    val now = Instant.now().toEpochMilli()
    val duration =
        Duration.between(Instant.ofEpochMilli(timestamp), Instant.ofEpochMilli(now))
    return@runBlocking duration.toMinutes() > 4
}