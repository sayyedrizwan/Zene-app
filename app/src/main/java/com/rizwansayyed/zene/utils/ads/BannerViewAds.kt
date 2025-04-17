package com.rizwansayyed.zene.utils.ads

import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.TextViewBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val bannerAdUnit =
    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else "ca-app-pub-2941808068005217/1624020934"


val nativeAdsBannerMap = HashMap<String, NativeAd?>()
val nativeAdsAndroidBannerViewMap = HashMap<String, NativeAdView?>()

@Composable
fun BannerNativeViewAds(id: Int) {

//    AndroidView(
//        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
//        factory = { context ->
//            AdView(context).apply {
//                setAdSize(AdSize.BANNER)
//                adUnitId = bannerAdUnit
//                loadAd(AdRequest.Builder().build())
//            }
//        }
//    )
//    Column(
//        Modifier
//            .padding(top = 25.dp)
//            .fillMaxWidth(), Arrangement.Center, Alignment.End
//    ) {
//        Box(
//            Modifier
//                .padding(5.dp)
//                .clip(RoundedCornerShape(10.dp))
//                .background(Color.Black)
//                .padding(horizontal = 9.dp, vertical = 5.dp)
//        ) {
//            TextViewBold(stringResource(R.string.ads), 14)
//        }
//
//        LazyVerticalGrid(
//            GridCells.Fixed(2),
//            Modifier
//                .fillMaxWidth()
//                .heightIn(max = 320.dp)
//        ) {
//            items(1) {
//                BannerNativeViewAdsItem("${id}_$it")
//            }
//        }
//    }
}

@Composable
fun BannerNativeViewAdsItem(id: String) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            if (nativeAdsBannerMap.getOrDefault(id, null) == null) {
                val videoOption =
                    VideoOptions.Builder().setClickToExpandRequested(false).setStartMuted(true)
                        .build()
                val builder = AdLoader.Builder(context, nativeAdUnit).forNativeAd { ad: NativeAd ->
                    nativeAd?.destroy()
                    nativeAd = ad
                    nativeAdsBannerMap[id] = ad
                }.withAdListener(object : AdListener() {
                    override fun onAdLoaded() {
                        super.onAdLoaded()
                        coroutine.launch {
                            val videoController = nativeAd?.mediaContent?.videoController
                            videoController?.play()
                        }
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e("AdMob", "Native video ad failed: ${error.message}")
                    }
                }).withNativeAdOptions(
                    NativeAdOptions.Builder()
                        .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
                        .setVideoOptions(videoOption).build()
                ).build()
                builder.loadAd(AdRequest.Builder().build())
            } else {
                nativeAd = nativeAdsBannerMap[id]
            }
        }
    }

    nativeAd?.let { ad ->
        AndroidView(
            factory = { ctx ->
                if (nativeAdsAndroidBannerViewMap.getOrDefault(id, null) != null) {
                    nativeAdsAndroidBannerViewMap[id]
                } else {
                    val inflater = LayoutInflater.from(ctx)
                    val adView =
                        inflater.inflate(R.layout.banner_native_ad_view, null) as NativeAdView

                    val mediaContainer = adView.findViewById<FrameLayout>(R.id.media_container)
                    val mediaView = MediaView(ctx)
                    mediaContainer.addView(mediaView)
                    adView.mediaView = mediaView

                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_icon)

                    (adView.headlineView as TextView).text = ad.headline
                    (adView.bodyView as TextView).text = ad.body
                    (adView.callToActionView as TextView).text = ad.callToAction
                    (adView.iconView as ImageView).setImageDrawable(ad.icon?.drawable)

                    ad.mediaContent?.let { mediaContent ->
                        mediaView.mediaContent = mediaContent

                        val videoController = mediaContent.videoController
                        videoController.videoLifecycleCallbacks =
                            object : VideoController.VideoLifecycleCallbacks() {
                                override fun onVideoStart() {
                                    Log.d("AdMob", "Video started.")
                                }

                                override fun onVideoEnd() {
                                    Log.d("AdMob", "Video ended.")
                                }
                            }
                    }
                    nativeAdsAndroidBannerViewMap[id] = adView
                    adView.setNativeAd(ad)
                    adView
                }!!
            }, modifier = Modifier.fillMaxSize()
        )
    }
}