package com.rizwansayyed.zene.presenter.ui.home.artists

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@Composable
fun TopArtistsImageView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    val height = (LocalConfiguration.current.screenHeightDp / 1.3).dp

    Box(
        Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        when (val v = artistsViewModel.artistsImage) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading ->
                Spacer(
                    Modifier
                        .fillMaxSize()
                        .background(shimmerBrush())
                )

            is DataResponse.Success -> AsyncImage(
                v.item, "", Modifier.fillMaxSize(), contentScale = ContentScale.Crop
            )
        }

        Spacer(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, DarkGreyColor.copy(0.5f), DarkGreyColor),
                        startY = 0.0f,
                        endY = 100.0f
                    )
                )
        )
    }
}


@SuppressLint("UnsafeOptInUsageError")
@Composable
fun ArtistsSongURL(videoLink: String) {
    val content = LocalContext.current.applicationContext
    val height = (LocalConfiguration.current.screenHeightDp / 1.3).dp

    val exoPlayer = ExoPlayer.Builder(content).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
        volume = 0f
    }

    val mediaItem = MediaItem.Builder().setMediaId("artists_video").setUri(videoLink).build()

    Box(
        Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        AndroidView(
            { ctx ->
                PlayerView(ctx).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    player = exoPlayer
                    player?.setMediaItem(mediaItem)
                    useController = false
                    setKeepContentOnPlayerReset(true)
                    setShutterBackgroundColor(Color.Transparent.toArgb())
                    player?.prepare()
                    player?.playWhenReady = true
                }
            }, Modifier.fillMaxSize()
        )

        Spacer(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, DarkGreyColor.copy(0.5f), DarkGreyColor),
                        startY = 0.0f,
                        endY = 100.0f
                    )
                )
        )
    }

    LaunchedEffect(exoPlayer.currentPosition) {
        Log.d("TAG", "ArtistsSongURL: running ${exoPlayer.currentPosition}")
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.pause()
        }
    }
}