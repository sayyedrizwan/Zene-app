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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bumptech.glide.request.RequestOptions
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun ConnectVibeItemView(item: ConnectFeedDataResponse?, showCaption: Boolean) {
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

        if (item?.location_name != null) {
            Row(
                Modifier
                    .padding(top = 10.dp, start = 3.dp, end = 3.dp)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_location, 18)
                Spacer(Modifier.width(4.dp))
                TextViewNormal(item.location_name!!, 14)
                Spacer(Modifier.weight(1f))

                ImageIcon(R.drawable.ic_comment, 18)
                if ((item.comments ?: 0) > 0) {
                    Spacer(Modifier.width(4.dp))
                    TextViewNormal((item.comments ?: 0).toString(), 14)
                }
            }
        }


//        Row(Modifier.fillMaxWidth()) {
//            if (item?.media != null) {
//                Box(Modifier.weight(6f)) {
//                    ConnectVibeMediaItem(item, Modifier.align(Alignment.Center))
//
//                    if (item.isVibing == true) GlideImage(
//                        R.drawable.just_vibing_sticker,
//                        "",
//                        Modifier
//                            .align(Alignment.TopStart)
//                            .width(80.dp)
//                            .offset((-14).dp, (-28).dp)
//                            .rotate(-20f),
//                        contentScale = ContentScale.Fit
//                    )
//
//                    Row(
//                        Modifier
//                            .padding(9.dp)
//                            .align(Alignment.BottomEnd)
//                            .size(24.dp)
//                            .clickable { }
//                            .clip(RoundedCornerShape(100))
//                            .background(Color.White),
//                        Arrangement.Center,
//                        Alignment.CenterVertically) {
//                        ImageIcon(R.drawable.ic_arrow_expand, 20, Color.Black)
//                    }
//                }
//            }
//
//            if ((item?.jazz_name != null && item.jazz_id != null) || item?.emoji != null) Column(
//                Modifier
//                    .weight(4f)
//                    .padding(horizontal = 1.dp),
//                Arrangement.Center,
//                Alignment.CenterHorizontally
//            ) {
//                if (item.jazz_name != null && item.jazz_id != null) GlideImage(
//                    item.jazz_thumbnail,
//                    item.jazz_name,
//                    Modifier
//                        .fillMaxWidth()
//                        .aspectRatio(1f),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(Modifier.padding(top = 12.dp))
//
//                if (item.emoji != null) TextViewBold(item.emoji ?: "", 50)
//            }
//        }
//
//        if (item?.location_name != null) {
//            Row(
//                Modifier
//                    .padding(top = 25.dp, start = 3.dp)
//                    .fillMaxWidth(),
//                Arrangement.Center,
//                Alignment.CenterVertically
//            ) {
//                ImageIcon(R.drawable.ic_location, 18)
//                Spacer(Modifier.width(4.dp))
//                TextViewNormal(item.location_name!!, 14)
//                Spacer(Modifier.weight(1f))
//            }
//        }
//
//        if (showCaption && item?.caption != null) Box(Modifier.padding(top = 19.dp)) {
//            TextViewNormal(item.caption ?: "", 15)
//        }
    }

    Spacer(Modifier.height(10.dp))
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
                .clickable { }, Alignment.Center
        ) {
            GlideImage(
                data?.media,
                data?.caption,
                Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(9f / 16f)
                    .clipToBounds(),
                contentScale = ContentScale.Crop
            ) {
                val options = RequestOptions().frame(3000)
                it.apply(options)

//                it.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).apply(requestOptions)
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
//        if (data?.isMediaVideo() == true) {
//            val cache = ExoPlayerCache.getInstance(context)
//            val dataSourceFactory = CacheDataSource.Factory().setCache(cache)
//                .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
//
//            val exoPlayer = remember {
//                val trackSelector = DefaultTrackSelector(context).apply {
//                    setParameters(
//                        buildUponParameters()
//                            .setMaxVideoSize(854, 480)
//                            .setMaxVideoBitrate(500_000)
//                            .setForceLowestBitrate(true)
//                    )
//                }
//                val loadControl = DefaultLoadControl.Builder()
//                    .setBufferDurationsMs(
//                        3000,  // Min buffer before playback starts (3s)
//                        10000, // Max buffer size (10s, enough for the whole video)
//                        1500,  // Buffer before rebuffering (1.5s)
//                        3000   // Back buffer size (3s)
//                    )
//                    .setTargetBufferBytes(-1) // Use default heuristics
//                    .setPrioritizeTimeOverSizeThresholds(true) // Prioritize buffering for short video
//                    .build()
//
//                ExoPlayer.Builder(context)
//                    .setTrackSelector(trackSelector)
//                    .setLoadControl(loadControl)
//                    .build().apply {
//                        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                            .createMediaSource(MediaItem.fromUri(data.media ?: ""))
////                        setMediaItem(MediaItem.fromUri(data.media ?: ""))
//                        setMediaSource(mediaSource)
//                        prepare()
//                        playWhenReady = true
//                    }
//            }
//
//
//            AndroidView(
//                factory = { ctx ->
//                    PlayerView(ctx).apply {
//                        useController = false
//                        exoPlayer.repeatMode = REPEAT_MODE_ONE
//                        exoPlayer.volume = 0f
////                        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
//                        player = exoPlayer
////                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
//                        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                            .createMediaSource(MediaItem.fromUri(data.media ?: ""))
//                        setShowBuffering(PlayerView.SHOW_BUFFERING_ALWAYS)
////                        exoPlayer.setMediaSource(mediaSource)
////                        exoPlayer.setMediaItem(MediaItem.fromUri(data.media ?: ""))
//                        exoPlayer.prepare()
//                        exoPlayer.play()
//                    }
//                }, modifier = Modifier
//                    .fillMaxWidth(0.6f)
//                    .aspectRatio(9f / 16f)
//                    .clipToBounds()
//            )
//
//
//            DisposableEffect(Unit) {
//                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                    .createMediaSource(MediaItem.fromUri(data.media ?: ""))
//                exoPlayer.setMediaSource(mediaSource)
//                exoPlayer.prepare()
//                exoPlayer.play()
//                onDispose { exoPlayer.pause() }
//            }
//        } else {

    }
//    }
    Spacer(Modifier.height(30.dp))
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