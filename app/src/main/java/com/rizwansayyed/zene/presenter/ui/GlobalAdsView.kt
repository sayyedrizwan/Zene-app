package com.rizwansayyed.zene.presenter.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.FirebaseEvents
import com.rizwansayyed.zene.utils.FirebaseEvents.registerEvent
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
            .padding(top = 20.dp)
            .padding(5.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .fillMaxWidth()
            .padding(4.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AndroidView(
            { c: Context ->
                val view = LayoutInflater.from(c)
                    .inflate(R.layout.native_ads_ui, null, false) as NativeAdView

                val composeView: ComposeView = view.findViewById(R.id.compose_view)
                val mediaView: MediaView = view.findViewById(R.id.media_view)

                composeView.apply {
                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                    setContent {
                        NativeAdView(ads!!)
                    }
                }
                view.callToActionView = composeView
                view.mediaView = mediaView
                view.setNativeAd(ads!!)
                view
            },
            Modifier
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        )

        LaunchedEffect(Unit){
            registerEvent(FirebaseEvents.FirebaseEvent.LOADED_AD)
        }
    }

    DisposableEffect(Unit) {
        coroutine.launch(Dispatchers.IO) {
            val adLoader = AdLoader.Builder(context, NATIVE_ADS_ID).forNativeAd {
                ads = it
            }.withAdListener(object : AdListener() {
                override fun onAdClicked() {
                    super.onAdClicked()
                    registerEvent(FirebaseEvents.FirebaseEvent.CLICK_AD)
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
        onDispose {
            ads?.destroy()
        }
    }
}

@Composable
fun NativeAdView(ads: NativeAd) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(Modifier.height(3.dp))
        TextSemiBold(
            v = ads.headline ?: "",
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            size = 14
        )
        Spacer(Modifier.height(6.dp))
        TextThin(
            v = stringResource(id = R.string.zene_ads),
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            size = 13
        )
        Spacer(Modifier.height(6.dp))
        Row(
            Modifier
                .padding(5.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(100))
                .padding(vertical = 9.dp, horizontal = 14.dp)
        ) {
            TextThin(ads.callToAction ?: "")
        }
    }
}

//
//@Composable
//fun GlobalHiddenNativeAds(color: Color = DarkGreyColor) {
////    var ads by remember { mutableStateOf<NativeAd?>(null) }
////
////    val context = LocalContext.current as Activity
////
////    if (ads != null) Box(Modifier.height(1.dp)) {
////        AndroidView(
////            factory = { c: Context ->
////                val view =
////                    LayoutInflater.from(c)
////                        .inflate(R.layout.native_hidden_ads_ui, null, false) as NativeAdView
////                val mediaView: MediaView = view.findViewById(R.id.ad_media)
////                view.mediaView = mediaView
////                view.setNativeAd(ads!!)
////                view
////            },
////            modifier = Modifier.fillMaxWidth()
////        )
////    }
////
////
////    DisposableEffect(Unit) {
////        val adLoader = AdLoader.Builder(context, NATIVE_ADS_ID).forNativeAd {
////            ads = it
////        }.build()
////
////        adLoader.loadAd(AdRequest.Builder().build())
////        onDispose {
////            ads?.destroy()
////        }
////    }
//}