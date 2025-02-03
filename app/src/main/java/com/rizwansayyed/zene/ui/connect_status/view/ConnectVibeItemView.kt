@file:kotlin.OptIn(ExperimentalGlideComposeApi::class)

package com.rizwansayyed.zene.ui.connect_status.view

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.ui.view.TextViewBold

@OptIn(UnstableApi::class)
@Composable
fun ConnectVibeItemView(item: ConnectFeedDataResponse?) {
    val context = LocalContext.current.applicationContext
    Spacer(Modifier.height(10.dp))

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
    ) {
        if (item?.media != null) {
            Box(Modifier.weight(6f)) {
                if (item.isMediaVideo()) {

                    val exoPlayer = ExoPlayer.Builder(context).build()
                    AndroidView(
                        factory = { ctx ->
                            PlayerView(ctx).apply {
                                useController = false
                                exoPlayer.repeatMode = REPEAT_MODE_ONE
                                exoPlayer.volume = 1f
                                exoPlayer.videoScalingMode =
                                    C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                                player = exoPlayer
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                val mediaItem = MediaItem.fromUri(item.media ?: "")
                                exoPlayer.setMediaItem(mediaItem)
                                exoPlayer.prepare()
                                exoPlayer.play()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clipToBounds()
                    )
                } else {
                    GlideImage(
                        item.media,
                        "",
                        Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clipToBounds(),
                        contentScale = ContentScale.Crop
                    ) {
                        it.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    }
                }

                if (item.isVibing == true) GlideImage(
                    R.drawable.just_vibing_sticker,
                    "",
                    Modifier
                        .align(Alignment.TopStart)
                        .width(80.dp)
                        .offset((-14).dp, (-28).dp)
                        .rotate(-20f),
                    contentScale = ContentScale.Fit
                )
            }
        }
        if ((item?.jazzName != null && item.jazzId != null) || item?.emoji != null) Column(
            Modifier
                .weight(4f)
                .padding(horizontal = 1.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            if (item.jazzName != null && item.jazzId != null) GlideImage(
                item.jazzThumbnail, item.jazzName,
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Spacer(Modifier.padding(top = 12.dp))

            if (item.emoji != null) TextViewBold(item.emoji ?: "", 50)
        }
    }

    Spacer(Modifier.height(10.dp))
}