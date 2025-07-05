package com.rizwansayyed.zene.utils.ads

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.rizwansayyed.zene.BuildConfig

val nativeAdUnit =
    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1044960115" else "ca-app-pub-2941808068005217/1646716011"

val bannerAdUnit =
    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/9214589741" else "ca-app-pub-2941808068005217/1624020934"

val nativeAdsMap = HashMap<String, AdView?>()
val nativeAdsAndroidViewMap = HashMap<String, AdView?>()

@Composable
fun NativeViewAdsCard(id: String?) {
    Column {
        (4 downTo 1).forEach { index ->
            key(index) {
                BannerAd(bannerAdUnit = bannerAdUnit)
            }
//            AndroidView(
//                modifier = Modifier
//                    .width(220.dp)
//                    .height(50.dp)
//                    .padding(bottom = 5.dp),
//                factory = { context ->
//                    AdView(context).apply {
//                        setAdSize(AdSize(220, 50))
//                        adUnitId = bannerAdUnit
//                        loadAd(AdRequest.Builder().build())
//                    }
//                }
//            )
        }
    }
}

@Composable
fun BannerAd(
    bannerAdUnit: String,
    widthDp: Dp = 220.dp,
    heightDp: Dp = 50.dp,
    bottomPadding: Dp = 5.dp
) {
    var isAdLoaded by remember { mutableStateOf(false) }
    val adHeight = if (isAdLoaded) heightDp else 2.dp
    val adWidth = if (isAdLoaded) widthDp else 2.dp
    val adPadding = if (isAdLoaded) bottomPadding else 0.dp

    AndroidView(
        modifier = Modifier
            .width(adWidth)
            .height(adHeight)
            .padding(bottom = adPadding),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize(widthDp.value.toInt(), heightDp.value.toInt()))
                adUnitId = bannerAdUnit
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        isAdLoaded = true
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        isAdLoaded = false
                    }
                }
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

//fun NativeViewAdsCard(id: String?) {
//    val context = LocalContext.current
//    val coroutine = rememberCoroutineScope()
//    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
//
//    LaunchedEffect(Unit) {
//        coroutine.launch(Dispatchers.IO) {
//            if (nativeAdsMap.getOrDefault(id, null) == null) {
//                val videoOption =
//                    VideoOptions.Builder().setClickToExpandRequested(false).setStartMuted(true)
//                        .build()
//
//                val builder = AdLoader.Builder(context, nativeAdUnit)
//                    .forNativeAd { ad: NativeAd ->
//                        nativeAd?.destroy()
//                        nativeAd = ad
//                        if (id != null) nativeAdsMap[id] = ad
//                    }.withAdListener(object : AdListener() {
//                        override fun onAdFailedToLoad(error: LoadAdError) { }
//                    }).withNativeAdOptions(
//                        NativeAdOptions.Builder()
//                            .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
//                            .setVideoOptions(videoOption).build()
//                    ).build()
//
//                builder.loadAd(AdRequest.Builder().build())
//            } else {
//                nativeAd = nativeAdsMap[id]
//            }
//        }
//    }
//
//    nativeAd?.let { ad ->
//        AndroidView(
//            factory = { ctx ->
//                val inflater = LayoutInflater.from(ctx)
//                val adView = inflater.inflate(R.layout.native_ad_view, null) as NativeAdView
//
//                fun getD(v: Int): Drawable? {
//                    return ContextCompat.getDrawable(context, v)
//                }
//
//                val mediaContainer = adView.findViewById<FrameLayout>(R.id.media_container)
//                mediaContainer.removeAllViews()
//                val mediaView = MediaView(ctx)
//                mediaContainer.addView(mediaView)
//                adView.mediaView = mediaView
//
//                adView.headlineView = adView.findViewById(R.id.ad_headline)
//                adView.bodyView = adView.findViewById(R.id.ad_body)
//                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
//                adView.iconView = adView.findViewById(R.id.ad_icon)
//                val icon: ImageView = adView.findViewById(R.id.ad_actions_icon)
//
//                (adView.headlineView as TextView).text = ad.headline
//                (adView.bodyView as TextView).text = ad.body
//                (adView.callToActionView as Button).text = ad.callToAction
//                (adView.iconView as ImageView).setImageDrawable(ad.icon?.drawable)
//
//                when {
//                    ad.callToAction?.lowercase() == "install" || ad.callToAction?.lowercase() == "download" ->
//                        icon.setImageDrawable(getD(R.drawable.ic_download))
//
//                    ad.callToAction?.lowercase()?.contains("more") == true ->
//                        icon.setImageDrawable(getD(R.drawable.ic_arrow_right))
//
//                    ad.callToAction?.lowercase()?.contains("shop") == true ||
//                            ad.callToAction?.lowercase()?.contains("buy") == true ||
//                            ad.callToAction?.lowercase()?.contains("order") == true ->
//                        icon.setImageDrawable(getD(R.drawable.ic_cart))
//
//                    ad.callToAction?.lowercase()?.contains("book") == true ||
//                            ad.callToAction?.lowercase()?.contains("open") == true ->
//                        icon.setImageDrawable(getD(R.drawable.ic_arrow_up_right))
//
//                    else -> icon.setImageDrawable(getD(R.drawable.ic_link))
//                }
//
//                ad.mediaContent?.let { mediaContent ->
//                    mediaView.mediaContent = mediaContent
//                    mediaContent.videoController.videoLifecycleCallbacks =
//                        object : VideoController.VideoLifecycleCallbacks() {
//                            override fun onVideoStart() {
//                                Log.d("AdMob", "Video started.")
//                            }
//
//                            override fun onVideoEnd() {
//                                Log.d("AdMob", "Video ended.")
//                            }
//                        }
//                }
//
//                adView.setNativeAd(ad)
//                adView
//            }, modifier = Modifier.fillMaxWidth()
//        )
//    }
//}