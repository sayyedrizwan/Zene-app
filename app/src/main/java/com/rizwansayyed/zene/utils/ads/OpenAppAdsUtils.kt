package com.rizwansayyed.zene.utils.ads

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.datastore.DataStorageManager.isPremiumDB
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.utils.MainUtils.timeDifferenceInMinutes
import com.rizwansayyed.zene.utils.MainUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OpenAppAdsUtils(val activity: Activity) {

    private val adID =
        if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9257395921" else "ca-app-pub-2941808068005217/7650500204"

    companion object {
        var lastAppOpenLoadTime: Long? = null
    }

    init { startAds() }

    private val listener = object : AppOpenAdLoadCallback() {
        override fun onAdLoaded(ad: AppOpenAd) {
            super.onAdLoaded(ad)

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
        val isLoggedIn = withContext(Dispatchers.IO) { userInfo.firstOrNull()?.isLoggedIn() }
        if (BuildConfig.DEBUG) return@launch
        if (isLoggedIn == false) return@launch
        val isPremium = withContext(Dispatchers.IO) { isPremiumDB.firstOrNull() }
        if (isPremium == true) return@launch

        val request = AdRequest.Builder().build()
        AppOpenAd.load(context, adID, request, listener)
    }
}