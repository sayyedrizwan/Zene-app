package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.R

@Composable
fun imgBuilder(path: String?): ImageRequest {
    return ImageRequest.Builder(LocalContext.current)
        .data(path ?: "https://www.zenemusic.co/logo820.png")
        .memoryCachePolicy(CachePolicy.ENABLED).build()
}


@Composable
fun ImageIcon(id: Int, click: () -> Unit) {
    Image(
        painterResource(id), "",
        Modifier
            .size(30.dp)
            .clickable { click() },
        colorFilter = ColorFilter.tint(Color.White)
    )
}

@Composable
fun ImageView(id: Int, modifier: Modifier = Modifier) {
    Image(
        painterResource(id), "", modifier, contentScale = ContentScale.Fit
    )
}