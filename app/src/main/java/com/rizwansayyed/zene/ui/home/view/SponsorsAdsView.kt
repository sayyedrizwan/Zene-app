package com.rizwansayyed.zene.ui.home.view

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.sponsorsAdsDB
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsLight
import com.rizwansayyed.zene.utils.Utils

@Composable
fun GifAsyncImage(
    modifier: Modifier, path: String?
) {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }

            .build()

    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = path).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier
            .clip(RoundedCornerShape(9))
            .background(Color.DarkGray),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun SponsorsAdsView() {
    val ads by sponsorsAdsDB.collectAsState(initial = null)

    if (ads?.showSponserAds == true) {
        Column(Modifier.padding(vertical = 60.dp)) {
            Column(Modifier.padding(horizontal = 10.dp)) {
                TextPoppinsLight(stringResource(R.string.today_sponsor), size = 17)
                TextPoppins(ads!!.ads?.top?.title ?: "", size = 20)
            }
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            ) {
                items(ads!!.ads?.top?.imgs ?: emptyList()) {
                    GifAsyncImage(
                        Modifier
                            .size(290.dp, 340.dp)
                            .padding(horizontal = 7.dp)
                            .clickable {
                                it?.link?.let { Utils.openBrowser(it) }
                            }, path = it?.img
                    )
                }
            }
        }
    }
}