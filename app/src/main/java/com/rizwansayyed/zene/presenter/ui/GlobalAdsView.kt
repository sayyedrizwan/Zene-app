package com.rizwansayyed.zene.presenter.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.utils.Utils.AdsId.BANNER_ADS_ID
import com.rizwansayyed.zene.utils.Utils.AdsId.NATIVE_ADS_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun GlobalNativeFullAds() {
    val context = LocalContext.current as Activity
    val coroutine = rememberCoroutineScope()

    var ads by remember { mutableStateOf<NativeAd?>(null) }


    if (ads != null) Column(
        Modifier
            .fillMaxWidth()
    ) {
        AndroidView(
            { c: Context ->
                val view = LayoutInflater.from(c)
                    .inflate(R.layout.native_ads_ui, null, false) as NativeAdView

                val mediaView: MediaView = view.findViewById(R.id.ad_media)
                val txt: TextView = view.findViewById(R.id.txt)

                txt.text = ads?.headline

                view.mediaView = mediaView
                view.setNativeAd(ads!!)
                view
            },
            Modifier
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        )
    }

    DisposableEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            val adLoader = AdLoader.Builder(context, NATIVE_ADS_ID).forNativeAd {
                ads = it
            }.build()

            adLoader.loadAd(AdRequest.Builder().build())
        }
        onDispose {
            ads?.destroy()
        }
    }
}

@Composable
fun GlobalHiddenNativeAds(color: Color = DarkGreyColor) {
    var ads by remember { mutableStateOf<NativeAd?>(null) }

    val context = LocalContext.current as Activity

    if (ads != null) Box(Modifier.height(1.dp)) {
        AndroidView(
            factory = { c: Context ->
                val view =
                    LayoutInflater.from(c)
                        .inflate(R.layout.native_hidden_ads_ui, null, false) as NativeAdView
                val mediaView: MediaView = view.findViewById(R.id.ad_media)
                view.mediaView = mediaView
                view.setNativeAd(ads!!)
                view
            },
            modifier = Modifier.fillMaxWidth()
        )
    }


    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, NATIVE_ADS_ID).forNativeAd {
            ads = it
        }.build()

        adLoader.loadAd(AdRequest.Builder().build())
        onDispose {
            ads?.destroy()
        }
    }
}