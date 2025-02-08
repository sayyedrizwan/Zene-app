@file:kotlin.OptIn(ExperimentalGlideComposeApi::class)

package com.rizwansayyed.zene.ui.connect_status.view

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ExoPlayerCache
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapLocation
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapNameLocation


@Composable
fun ConnectVibeItemView(item: ConnectFeedDataResponse?) {
    var showPlaceDialog by remember { mutableStateOf(false) }

    Spacer(Modifier.height(10.dp))

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 3.dp)
            .border(0.5.dp, Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 9.dp, vertical = 15.dp)
            .padding(bottom = 10.dp)
    ) {

        Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
            GlideImage(
                item?.userDetails?.profile_photo,
                item?.userDetails?.name,
                Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 9.dp), Arrangement.Center, Alignment.Start
            ) {
                Spacer(Modifier.height(6.dp))
                TextViewSemiBold(item?.userDetails?.name ?: "", 13, line = 1)
                item?.userDetails?.username?.let {
                    Box(Modifier.offset(y = (-5).dp)) {
                        TextViewNormal("@${it}", 12, line = 1)
                    }
                }
            }

            TextViewNormal(item?.ts() ?: "", 12, line = 1)
        }

        if (item?.media == null && item?.jazz_id == null && item?.jazz_name == null && item?.caption != null) {
            ConnectVibeItemOnlyCaption(item)
        } else if (item?.media != null) {
            ConnectVibeItemMedia(item)
        }

        Row(
            Modifier
                .padding(top = 10.dp, start = 3.dp, end = 3.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            if (item?.location_name != null) {
                Row(
                    Modifier.clickable { showPlaceDialog = true },
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    ImageIcon(R.drawable.ic_location, 18)
                    Spacer(Modifier.width(4.dp))
                    TextViewNormal(item.location_name!!, 14)
                }
            }
            Spacer(Modifier.weight(1f))

            ImageIcon(R.drawable.ic_comment, 18)
            if ((item?.comments ?: 0) > 0) {
                Spacer(Modifier.width(4.dp))
                TextViewNormal((item?.comments ?: 0).toString(), 14)
            }
        }
    }

    Spacer(Modifier.height(10.dp))

    if (showPlaceDialog) LocationSharingPlace(item) {
        showPlaceDialog = false
    }
}


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSharingPlace(data: ConnectFeedDataResponse?, close: () -> Unit) {
    ModalBottomSheet(
        { close() }, contentColor = MainColor, containerColor = MainColor
    ) {
        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(10.dp))
            TextViewSemiBold(data?.location_address ?: "", 16, center = true)
            Spacer(Modifier.height(20.dp))

            ButtonWithImageAndBorder(
                R.drawable.ic_location, R.string.open_location, Color.White, Color.White
            ) {
                val lat = data?.latitude?.toDoubleOrNull()
                val lon = data?.longitude?.toDoubleOrNull()

                if (lat != null && lon != null) openGoogleMapLocation(
                    false,
                    lat,
                    lon,
                    data.location_name ?: ""
                )
                else openGoogleMapNameLocation(data?.location_address ?: "")
            }
            Spacer(Modifier.height(50.dp))
        }
    }
}


@Composable
fun ConnectVibeItemOnlyCaption(data: ConnectFeedDataResponse?) {
    Spacer(Modifier.height(30.dp))
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        if (data?.caption?.trim() != null) {
            TextViewBold(data.caption ?: "", 20, center = true)

            if (data.emoji != null) Box(
                Modifier
                    .rotate(-20f)
                    .offset(y = 30.dp, x = (-10).dp)
                    .align(Alignment.BottomEnd)
            ) {
                TextViewBold(data.emoji ?: "", 50)
            }
        }
    }
    Spacer(Modifier.height(30.dp))
}

@OptIn(UnstableApi::class)
@Composable
fun ConnectVibeItemMedia(data: ConnectFeedDataResponse?) {
    var showMediaDialog by remember { mutableStateOf(false) }

    Spacer(Modifier.height(30.dp))
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Box(
            Modifier
                .fillMaxWidth()
                .clickable { showMediaDialog = true }, Alignment.Center
        ) {
            GlideImage(
                data?.media_thubnail,
                data?.caption,
                Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(9f / 16f)
                    .clipToBounds(),
                contentScale = ContentScale.Crop
            ) {
                it.diskCacheStrategy(DiskCacheStrategy.ALL)
            }

            if (data?.isMediaVideo() == false) Box(
                Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    Modifier
                        .padding(9.dp)
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.White),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    ImageIcon(R.drawable.ic_arrow_expand, 20, Color.Black)
                }
            }

            if (data?.isMediaVideo() == true) Row(
                Modifier
                    .padding(9.dp)
                    .align(Alignment.Center)
                    .size(45.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MainColor),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_play, 27, Color.White)
            }
        }

        if ((data?.caption?.length ?: 0) > 0) {
            Spacer(Modifier.height(15.dp))
            TextViewNormal(data?.caption ?: "", 16, center = true)
        }
    }
    Spacer(Modifier.height(30.dp))

    if (showMediaDialog) ConnectVibeMediaItemAlert(data) {
        showMediaDialog = false
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ConnectVibeMediaItemAlert(item: ConnectFeedDataResponse?, close: () -> Unit) {
    val context = LocalContext.current.applicationContext
    Dialog(
        close, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Black), Alignment.Center
        ) {
            if (item?.isMediaVideo() == true) {
                val exoPlayer = remember {
                    val exo = ExoPlayerCache.getInstance(context)

                    ExoPlayer.Builder(context).setTrackSelector(exo.first)
                        .setLoadControl(ExoPlayerCache.loadControl).build().apply {
                            val mediaSource = ProgressiveMediaSource.Factory(exo.second)
                                .createMediaSource(MediaItem.fromUri(item.media ?: ""))
                            setMediaSource(mediaSource)
                            setMediaSource(mediaSource)
                            prepare()
                            playWhenReady = true
                        }
                }

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            useController = true
                            exoPlayer.volume = 1f
                            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                            exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                            setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
                            player = exoPlayer
                            exoPlayer.prepare()
                            exoPlayer.play()
                        }
                    }, modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                )
                LifecycleResumeEffect(Unit) {
                    exoPlayer.play()
                    onPauseOrDispose { exoPlayer.pause() }
                }
            } else {
                GlideImage(
                    item?.media,
                    item?.caption,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                ) {
                    it.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                }
            }
        }
    }
}