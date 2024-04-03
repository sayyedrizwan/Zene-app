package com.rizwansayyed.zene.presenter.ui.home.ads

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun TopAdsGridLists() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    if (homeViewModel.appAds != null && homeViewModel.appAds?.doShow == true) {
        Spacer(Modifier.height(60.dp))

        TextThin(stringResource(R.string.sponsor), Modifier.padding(horizontal = 6.dp))
        Spacer(Modifier.height(5.dp))
        TextSemiBold(homeViewModel.appAds?.title ?: "", Modifier.padding(horizontal = 6.dp))

        LazyRow(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            items(homeViewModel.appAds?.homeGridTop ?: emptyList()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it?.img)
                        .decoderFactory(if (SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
                        .build(),
                    it?.link, Modifier
                        .padding(horizontal = 6.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(5))
                        .clickable {
                            Uri
                                .parse(it?.link)
                                ?.customBrowser()
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.height(30.dp))
    }

    LaunchedEffect(Unit) {
        homeViewModel.appAds()
    }
}

@OptIn(UnstableApi::class)
@Composable
fun HomeAdsVideoView() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    val context = LocalContext.current
    val exoPlayer = ExoPlayer.Builder(context).build()

    if (homeViewModel.appAds != null && homeViewModel.appAds?.doShow == true
        && (homeViewModel.appAds?.homeMiddleView?.img?.length ?: 0) > 2
    ) {
        if (homeViewModel.appAds?.homeMiddleView?.img?.contains(".mp4") == true) {
            val mediaSource = remember(homeViewModel.appAds?.homeMiddleView?.img) {
                MediaItem.fromUri(homeViewModel.appAds?.homeMiddleView?.img ?: "")
            }

            Spacer(Modifier.height(70.dp))

            TextThin(
                stringResource(R.string.sponsor),
                Modifier
                    .padding(horizontal = 6.dp)
                    .offset(x = 0.dp, y = 40.dp)
            )

            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        hideController()
                        controllerAutoShow = false
                        useController = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clickable {
                        Uri
                            .parse(homeViewModel.appAds?.homeMiddleView?.link)
                            .customBrowser()
                    }
            )

            Spacer(Modifier.height(30.dp))

            LaunchedEffect(mediaSource) {
                exoPlayer.setMediaItem(mediaSource)
                exoPlayer.volume = 0f
                exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL
                exoPlayer.playWhenReady = true
                exoPlayer.prepare()
                exoPlayer.play()
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(homeViewModel.appAds?.homeMiddleView?.img)
                    .decoderFactory(if (SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
                    .build(),
                homeViewModel.appAds?.homeMiddleView?.link, Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .clickable {
                        Uri
                            .parse(homeViewModel.appAds?.homeMiddleView?.link)
                            ?.customBrowser()
                    },
                contentScale = ContentScale.Inside
            )
        }
    }
}