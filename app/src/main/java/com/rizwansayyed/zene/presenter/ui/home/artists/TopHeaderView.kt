package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
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
    var isMute by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .size(height)
    ) {
        artistsThumbnailPlayer.AlbumsArtistsVideo(videoLink)

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

        Image(
            painterResource(if (isMute) R.drawable.ic_volume_high else R.drawable.ic_volume_off),
            "",
            Modifier
                .padding(end = 6.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-55).dp)
                .border(1.dp, Color.White, RoundedCornerShape(50))
                .padding(6.dp)
                .size(18.dp)
                .clickable {
                    isMute = !isMute
                    artistsThumbnailPlayer.muteUnMute()
                },
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}