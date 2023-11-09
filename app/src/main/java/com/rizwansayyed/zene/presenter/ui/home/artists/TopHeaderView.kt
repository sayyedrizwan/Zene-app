package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.ArtistsThumbnailVideoPlayer
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


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun ArtistsSongURL(videoLink: String, artistsThumbnailPlayer: ArtistsThumbnailVideoPlayer) {
    val height = (LocalConfiguration.current.screenHeightDp / 1.3).dp

    Box(
        Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        artistsThumbnailPlayer.AlbumsArtistsVideo(url = videoLink)

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