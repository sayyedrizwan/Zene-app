package com.rizwansayyed.zene.utils.ads

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
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
import com.rizwansayyed.zene.R


@Composable
fun NativeViewAds() {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        val builder = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the error
                }
            })
            .build()

        builder.loadAd(AdRequest.Builder().build())
    }

    nativeAd?.let { ad ->
        AndroidView(
            factory = { ctx ->
                val inflater = LayoutInflater.from(ctx)
                val adView = inflater.inflate(R.layout.native_ad_view, null) as NativeAdView

                val mediaContainer = adView.findViewById<FrameLayout>(R.id.media_container)
                val mediaView = MediaView(ctx)
                mediaContainer.addView(mediaView)
                adView.mediaView = mediaView

                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

                (adView.headlineView as TextView).text = ad.headline
                ad.mediaContent?.let { mediaView.setMediaContent(it) }
                (adView.bodyView as TextView).text = ad.body
                (adView.callToActionView as Button).text = ad.callToAction

                adView.setNativeAd(ad)
                adView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun NativeViewAdsCard() {
    val context = LocalContext.current
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }

    LaunchedEffect(Unit) {
        val builder = AdLoader.Builder(context, "ca-app-pub-2941808068005217/1646716011")
            .forNativeAd { ad: NativeAd ->
                nativeAd?.destroy()
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e("AdMob", "Native video ad failed: ${error.message}")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_LANDSCAPE)
                    .setVideoOptions(VideoOptions.Builder().setStartMuted(true).build())
                    .build()
            )
            .build()

        builder.loadAd(AdRequest.Builder().build())
    }

    nativeAd?.let { ad ->
        AndroidView(
            factory = { ctx ->
                val inflater = LayoutInflater.from(ctx)
                val adView = inflater.inflate(R.layout.native_ad_view, null) as NativeAdView

                val mediaContainer = adView.findViewById<FrameLayout>(R.id.media_container)
                val mediaView = MediaView(ctx)
                mediaContainer.addView(mediaView)
                adView.mediaView = mediaView

                adView.headlineView = adView.findViewById(R.id.ad_headline)
                adView.bodyView = adView.findViewById(R.id.ad_body)
                adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)

                (adView.headlineView as TextView).text = ad.headline
                (adView.bodyView as TextView).text = ad.body
                (adView.callToActionView as Button).text = ad.callToAction

                ad.mediaContent?.let { mediaContent ->
                    mediaView.setMediaContent(mediaContent)

                    val videoController = mediaContent.videoController
                    videoController.videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                        override fun onVideoStart() {
                            Log.d("AdMob", "Video started.")
                        }

                        override fun onVideoEnd() {
                            Log.d("AdMob", "Video ended.")
                        }
                    }
                }

                adView.setNativeAd(ad)
                adView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}