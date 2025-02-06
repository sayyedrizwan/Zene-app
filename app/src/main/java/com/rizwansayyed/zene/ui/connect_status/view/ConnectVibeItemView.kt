@file:kotlin.OptIn(ExperimentalGlideComposeApi::class)

package com.rizwansayyed.zene.ui.connect_status.view

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
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
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun ConnectVibeItemView(item: ConnectFeedDataResponse?, showCaption: Boolean) {
    Spacer(Modifier.height(10.dp))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            if (item?.media != null) {
                Box(Modifier.weight(6f)) {
                    ConnectVibeMediaItem(item, Modifier.align(Alignment.Center))

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

                    Row(
                        Modifier
                            .padding(9.dp)
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .clickable { }
                            .clip(RoundedCornerShape(100))
                            .background(Color.White),
                        Arrangement.Center,
                        Alignment.CenterVertically) {
                        ImageIcon(R.drawable.ic_arrow_expand, 20, Color.Black)
                    }
                }
            }
            if ((item?.jazz_name != null && item.jazz_id != null) || item?.emoji != null) Column(
                Modifier
                    .weight(4f)
                    .padding(horizontal = 1.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                if (item.jazz_name != null && item.jazz_id != null) GlideImage(
                    item.jazz_thumbnail,
                    item.jazz_name,
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.padding(top = 12.dp))

                if (item.emoji != null) TextViewBold(item.emoji ?: "", 50)
            }
        }

        if (item?.location_name != null) {
            Row(
                Modifier
                    .padding(top = 25.dp, start = 3.dp)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_location, 18)
                Spacer(Modifier.width(4.dp))
                TextViewNormal(item.location_name!!, 14)
                Spacer(Modifier.weight(1f))
            }
        }

        if (showCaption && item?.caption != null) Box(Modifier.padding(top = 19.dp)) {
            TextViewNormal(item.caption ?: "", 15)
        }
    }

    Spacer(Modifier.height(10.dp))
}

@OptIn(UnstableApi::class)
@Composable
fun ConnectVibeMediaItem(item: ConnectFeedDataResponse, modifier: Modifier) {
    val context = LocalContext.current.applicationContext

    if (item.isMediaVideo()) {
        val exoPlayer = ExoPlayer.Builder(context).build()
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    exoPlayer.repeatMode = REPEAT_MODE_ONE
                    exoPlayer.volume = 1f
                    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    val mediaItem = MediaItem.fromUri(item.media ?: "")
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }
            }, modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clipToBounds()
        )
        LifecycleResumeEffect(Unit) {
            exoPlayer.play()
            onPauseOrDispose { exoPlayer.pause() }
        }
    } else {
        GlideImage(
            item.media,
            "",
            modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clipToBounds(),
            contentScale = ContentScale.Crop
        ) {
            it.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
        }
    }
}