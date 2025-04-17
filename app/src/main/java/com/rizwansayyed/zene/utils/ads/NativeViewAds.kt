package com.rizwansayyed.zene.utils.ads

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val nativeAdUnit =
    if (BuildConfig.DEBUG) "ca-app-pub-3940256099942544/1044960115" else "ca-app-pub-2941808068005217/1646716011"

val nativeAdsMap = HashMap<String, NativeAd?>()
val nativeAdsAndroidViewMap = HashMap<String, NativeAdView?>()

@Composable
fun NativeViewAdsCard(id: String?) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            if (nativeAdsMap.getOrDefault(id, null) == null) {
                val videoOption =
                    VideoOptions.Builder().setClickToExpandRequested(false).setStartMuted(true)
                        .build()

                val builder = AdLoader.Builder(context, nativeAdUnit)
                    .forNativeAd { ad: NativeAd ->
                        nativeAd?.destroy()
                        nativeAd = ad
                        if (id != null) nativeAdsMap[id] = ad
                    }.withAdListener(object : AdListener() {
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
                nativeAd = nativeAdsMap[id]
            }
        }
    }

    nativeAd?.let { ad ->
        AndroidView(
            factory = { ctx ->
                if (nativeAdsAndroidViewMap.getOrDefault(id, null) != null) {
                    nativeAdsAndroidViewMap[id]
                } else {
                    val inflater = LayoutInflater.from(ctx)
                    val adView: NativeAdView =
                        inflater.inflate(R.layout.native_ad_view, null) as NativeAdView

                    fun getD(v: Int): Drawable? {
                        return ContextCompat.getDrawable(context, v)
                    }

                    val mediaContainer = adView.findViewById<FrameLayout>(R.id.media_container)
                    mediaContainer.removeAllViews()
                    val mediaView = MediaView(ctx)
                    mediaContainer.addView(mediaView)
                    adView.mediaView = mediaView


                    adView.headlineView = adView.findViewById(R.id.ad_headline)
                    adView.bodyView = adView.findViewById(R.id.ad_body)
                    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
                    adView.iconView = adView.findViewById(R.id.ad_icon)
                    val icon: ImageView = adView.findViewById(R.id.ad_actions_icon)

                    (adView.headlineView as TextView).text = ad.headline
                    (adView.bodyView as TextView).text = ad.body
                    (adView.callToActionView as Button).text = ad.callToAction
                    (adView.iconView as ImageView).setImageDrawable(ad.icon?.drawable)

                    if (ad.callToAction?.lowercase() == "install" || ad.callToAction?.lowercase() == "download") {
                        icon.setImageDrawable(getD(R.drawable.ic_download))
                    } else if (ad.callToAction?.lowercase()?.contains("more") == true) {
                        icon.setImageDrawable(getD(R.drawable.ic_arrow_right))
                    } else if (ad.callToAction?.lowercase()?.contains("shop") == true ||
                        ad.callToAction?.lowercase()?.contains("buy") == true ||
                        ad.callToAction?.lowercase()?.contains("order") == true
                    ) {
                        icon.setImageDrawable(getD(R.drawable.ic_cart))
                    } else if (ad.callToAction?.lowercase()?.contains("book") == true ||
                        ad.callToAction?.lowercase()?.contains("open") == true
                    ) {
                        icon.setImageDrawable(getD(R.drawable.ic_arrow_up_right))
                    } else
                        icon.setImageDrawable(getD(R.drawable.ic_link))

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

                    if (id != null) nativeAdsAndroidViewMap[id] = adView
                    adView.setNativeAd(ad)
                    adView
                }!!
            }, modifier = Modifier.fillMaxWidth()
        )
    }
}