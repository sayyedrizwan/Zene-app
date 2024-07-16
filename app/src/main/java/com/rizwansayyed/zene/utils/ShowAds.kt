package com.rizwansayyed.zene.utils

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.utils.Utils.IDs.AD_UNIT_ID
import javax.inject.Inject


class ShowAdsOnAppOpen @Inject constructor(private val activity: Activity) {

    init {
        MobileAds.initialize(activity) { }
    }

    private val listener = object : AppOpenAd.AppOpenAdLoadCallback() {
        override fun onAdLoaded(p0: AppOpenAd) {
            super.onAdLoaded(p0)
            p0.show(activity)
        }
    }

    fun showAds() {
        if (BuildConfig.DEBUG) return
        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, AD_UNIT_ID, request, listener)
    }
}