package com.rizwansayyed.zene.ui.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.TopSponsorAds
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HomeSponsorAdsView(ads: TopSponsorAds?, navViewModel: NavigationViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(
                indication = null, interactionSource = remember { MutableInteractionSource() }) {
                if (ads?.link != null) MediaContentUtils.openCustomBrowser(ads.link)
            }) {

        TopSponsorHeader(ads)
        Spacer(Modifier.height(14.dp))
        if (ads?.media?.isNotEmpty() == true) LazyRow(Modifier.fillMaxWidth()) {
            items(ads.media) {
                if (it.contains(".mp4")) PlayAdsVideo(it, navViewModel)
                else GlideImage(
                    it, ads.title, Modifier
                        .padding(horizontal = 5.dp)
                        .height(250.dp)
                )
            }

            item {
                Spacer(Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun TopSponsorHeader(ads: TopSponsorAds?) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                TextViewSemiBold(stringResource(R.string.sponsor), 20)

                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(androidx.compose.ui.graphics.Color.White)
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    Arrangement.Center, Alignment.CenterVertically
                ) {
                    if ((ads?.button?.trim()?.length ?: 0) >= 2) {
                        TextViewBold(
                            ads?.button.toString(),
                            14,
                            androidx.compose.ui.graphics.Color.Black,
                            center = false
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                    ImageIcon(
                        R.drawable.ic_arrow_right,
                        18,
                        androidx.compose.ui.graphics.Color.Black
                    )
                }
            }
            ads?.title?.let {
                Spacer(Modifier.height(4.dp))
                TextViewNormal(it, 15)
            }
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayAdsVideo(url: String, viewModel: NavigationViewModel) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    if (player != null) PlayerSurface(
        player = player!!,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(250.dp)
            .aspectRatio(0.5f),
    )

    DisposableEffect(Unit) {
        player = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            volume = 0f
            videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            repeatMode = Player.REPEAT_MODE_ONE
            playWhenReady = true
            prepare()
            play()
        }
        onDispose {
            player?.playWhenReady = false
            player?.release()
        }
    }
}