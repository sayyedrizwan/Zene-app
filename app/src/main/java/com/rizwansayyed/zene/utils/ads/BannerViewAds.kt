package com.rizwansayyed.zene.utils.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun BannerViewAds() {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth().height(120.dp),
        factory = { ctx ->
            AdView(ctx).apply {
                val adSize = AdSize.getCurrentOrientationInlineAdaptiveBannerAdSize(context, 320)

                val bannerView = AdView(context)
                bannerView.adUnitId = "ca-app-pub-3940256099942544/9214589741"
                bannerView.setAdSize(adSize)

// Step 3: Load an ad.
                val adRequest = AdRequest.Builder().build()
                bannerView.loadAd(adRequest)
            }
        }
    )
}