package com.rizwansayyed.zene.ui.main.home

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
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
            TextViewSemiBold(stringResource(R.string.sponsor), 20)
            ads?.title?.let {
                Spacer(Modifier.height(2.dp))
                TextViewNormal(it, 15)
            }
        }

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
            ImageIcon(R.drawable.ic_arrow_right, 18, androidx.compose.ui.graphics.Color.Black)
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun PlayAdsVideo(url: String, viewModel: NavigationViewModel) {
    val context = LocalContext.current
    val exoPlayer = remember { viewModel.getPlayer(context, url) }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                setShutterBackgroundColor(Color.TRANSPARENT)

                player = exoPlayer
                exoPlayer.seekTo(exoPlayer.currentPosition)
            }
        }, modifier = Modifier
            .padding(horizontal = 5.dp)
            .height(250.dp)
    )
}