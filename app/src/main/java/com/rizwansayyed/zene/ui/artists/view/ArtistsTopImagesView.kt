package com.rizwansayyed.zene.ui.artists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush

@Composable
fun ArtistsTopImagesView(img: String?, name: String?, justLoading: Boolean) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        Modifier
            .width((screenWidth.value / 1.3).dp)
            .height((screenHeight.value / 1.8).dp)
    ) {
        Spacer(
            Modifier
                .align(Alignment.Center)
                .width((screenWidth.value / 1.3).dp)
                .height((screenHeight.value / 1.8).dp)
                .background(shimmerEffectBrush())
        )

        if (!justLoading)
            AsyncImage(
                imgBuilder(img), name,
                Modifier
                    .align(Alignment.Center)
                    .width((screenWidth.value / 1.3).dp)
                    .height((screenHeight.value / 1.8).dp),
                contentScale = ContentScale.Crop
            )
    }
}