package com.rizwansayyed.zene.ui.home.view

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.ImageLoader
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

@UnstableApi
@Composable
fun SponsorsAdsBottomView() {
    val context = LocalContext.current.applicationContext
    val ads by sponsorsAdsDB.collectAsState(initial = null)

    if (ads?.showSponserAds == true) {
        Column(
            Modifier
                .padding(vertical = 60.dp)
                .clickable {
                    ads!!.ads?.bottom?.link?.let { l -> Utils.openBrowser(l) }
                }) {
            Column(Modifier.padding(horizontal = 10.dp)) {
                TextPoppinsLight(stringResource(R.string.today_sponsor), size = 17)
            }

            if (ads!!.ads?.bottom?.media?.contains(".mp4") == true) {
                val playerMain = ExoPlayer.Builder(context).build()

                AndroidView(
                    {
                        PlayerView(context).apply {
                            player = playerMain
                            useController = false
                            val uri = Uri.parse(ads!!.ads?.bottom?.media)
                            val mediaItem = MediaItem.fromUri(uri)
                            playerMain.setMediaItem(mediaItem)
                            playerMain.repeatMode = Player.REPEAT_MODE_ALL
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            playerMain.playWhenReady = false
                            playerMain.prepare()
                            playerMain.play()
                        }
                    },
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 7.dp)
                        .clip(RoundedCornerShape(9))
                )

                DisposableEffect(Unit) {
                    onDispose {
                        playerMain.pause()
                    }
                }

            } else ads!!.ads?.bottom?.media?.let {
                GifAsyncImage(
                    Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 7.dp), path = it
                )
            }
            TextPoppins(ads!!.ads?.top?.title ?: "", true, size = 14)
        }
    }
}