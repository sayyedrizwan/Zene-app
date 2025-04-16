package com.rizwansayyed.zene.utils.ads

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.coroutines.delay
import java.util.Arrays
import kotlin.math.roundToInt


@Composable
fun BannerViewAds() {
    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp
    val padding = 16
    var i by remember { mutableIntStateOf(0) }
    var containerWidth by remember { mutableStateOf<Int?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .padding(padding.dp)
            .fillMaxWidth()
            .onSizeChanged {
                containerWidth = it.width
            }
    ) {
        val items =
            listOf(
                "deviceCurrentWidth - 40" to deviceCurrentWidth - 40,
                "deviceCurrentWidth - padding * 2" to deviceCurrentWidth - padding * 2,
                "AdSize.FULL_WIDTH" to AdSize.FULL_WIDTH,
                "onSizeChanged" to with(LocalDensity.current) {
                    containerWidth?.let { containerWidth ->
                        (containerWidth / density).roundToInt()
                    }
                }
            )
        items.forEach {
            val (title, width) = it
            if (width == null) {
                return@forEach
            }

            Text(title)
            AndroidView(
                factory = { context ->
                    AdView(context).apply {
                        setAdSize( AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                            context,
                            width
                        ))
                        adUnitId = "ca-app-pub-2941808068005217/1624020934"
                        loadAd(AdRequest.Builder().build())
                    }
                },
                update = { adView ->
                    adView.loadAd(AdRequest.Builder().build())
                    i // needed to update view on i change
                },
            )
        }
        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                i++
            }
        }
    }
}