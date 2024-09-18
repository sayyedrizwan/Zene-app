package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.rizwansayyed.zene.data.db.DataStoreManager.sponsorsAdsDB
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.IDs.AD_BANNER_ID

@Composable
fun AdsBannerView() {
    val ads by sponsorsAdsDB.collectAsState(initial = null)

    if (ads?.showAds != false) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = AD_BANNER_ID
                    loadAd(AdRequest.Builder().build())
                    logEvents(FirebaseLogEvents.FirebaseEvents.BANNER_AD_VIEW)
                }
            }
        )

        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = AD_BANNER_ID
                    loadAd(AdRequest.Builder().build())
                    logEvents(FirebaseLogEvents.FirebaseEvents.BANNER_AD_VIEW)
                }
            }
        )
    }
}