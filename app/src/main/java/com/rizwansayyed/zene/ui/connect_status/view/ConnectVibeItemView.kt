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
import com.rizwansayyed.zene.data.model.MusicDataTypes.ALBUMS
import com.rizwansayyed.zene.data.model.MusicDataTypes.ARTISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.MOVIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.NEWS
import com.rizwansayyed.zene.data.model.MusicDataTypes.NONE
import com.rizwansayyed.zene.data.model.MusicDataTypes.PLAYLISTS
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST
import com.rizwansayyed.zene.data.model.MusicDataTypes.PODCAST_CATEGORIES
import com.rizwansayyed.zene.data.model.MusicDataTypes.SONGS
import com.rizwansayyed.zene.ui.main.connect.connectview.openConnectUserProfile
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ExoPlayerCache

@Composable
fun ConnectVibeItemView(item: ConnectFeedDataResponse?) {
    var showPlaceDialog by remember { mutableStateOf(false) }
    var showEmojiDialog by remember { mutableStateOf(false) }
    var showMusicMediaDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }

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
            Column(Modifier
                .clickable { item?.email?.let { openConnectUserProfile(it) } }
                .padding(horizontal = 9.dp), Arrangement.Center, Alignment.Start) {
                Spacer(Modifier.height(6.dp))
                TextViewSemiBold(item?.userDetails?.name ?: "", 13, line = 1)
                item?.userDetails?.username?.let {
                    Box(Modifier.offset(y = (-5).dp)) {
                        TextViewNormal("@${it}", 12, line = 1)
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            TextViewNormal(item?.ts() ?: "", 12, line = 1)
        }

        if (item?.media != null) ConnectVibeItemMedia(item) {
            showEmojiDialog = true
        }
        else ConnectVibeItemOnlyCaption(item, {
            showEmojiDialog = true
        }) {
            showMusicMediaDialog = true
        }

        Row(
            Modifier
                .padding(top = 10.dp, start = 3.dp, end = 3.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), Arrangement.Center, Alignment.Start) {
                if (item?.getMusicData() != null && item.media != null) {
                    Row(
                        Modifier.clickable { showMusicMediaDialog = true },
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        when (item.getMusicData()?.type()) {
                            NONE -> {}
                            SONGS -> ImageIcon(R.drawable.ic_music_note, 18)
                            PLAYLISTS, ALBUMS -> ImageIcon(R.drawable.ic_playlist, 18)
                            ARTISTS -> ImageIcon(R.drawable.ic_black_microphone, 18)
                            PODCAST, PODCAST_CATEGORIES -> ImageIcon(R.drawable.ic_mic, 18)
                            NEWS -> ImageIcon(R.drawable.ic_news, 18)
                            MOVIES -> ImageIcon(R.drawable.ic_camera_video, 18)
                            null -> {}
                        }
                        Spacer(Modifier.width(4.dp))
                        TextViewNormal("${item.jazz_name} - ${item.jazz_artists}", 14, line = 1)
                    }
                }

                if (item?.location_name != null) {
                    Row(
                        Modifier
                            .padding(top = 5.dp)
                            .clickable { showPlaceDialog = true },
                        Arrangement.Center,
                        Alignment.CenterVertically
                    ) {
                        ImageIcon(R.drawable.ic_location, 18)
                        Spacer(Modifier.width(4.dp))
                        TextViewNormal(item.location_name!!, 14, line = 1)
                    }
                }
            }

            Row(Modifier.clickable { showCommentDialog = true }) {
                ImageIcon(R.drawable.ic_comment, 19)
                if ((item?.comments ?: 0) > 0) {
                    Spacer(Modifier.width(4.dp))
                    TextViewNormal((item?.comments ?: 0).toString(), 14)
                }
            }
        }
    }

    Spacer(Modifier.height(10.dp))

    if (showPlaceDialog) LocationSharingPlace(item) {
        showPlaceDialog = false
    }
    if (showEmojiDialog) EmojiValueSheet(item) {
        showEmojiDialog = false
    }
    if (showMusicMediaDialog) MusicDataSheet(item) {
        showMusicMediaDialog = false
    }
    if (showCommentDialog) ConnectVibeCommentsView(item) {
        showCommentDialog = false
    }
}

@Composable
fun ConnectVibeItemOnlyCaption(
    data: ConnectFeedDataResponse?, openEmoji: () -> Unit, openSong: () -> Unit
) {
    Spacer(Modifier.height(30.dp))
    Box(Modifier.fillMaxWidth(), Alignment.Center) {
        if (data?.caption?.trim() != null) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                if (data.getMusicData() != null) {
                    Box(Modifier
                        .width(170.dp)
                        .clickable { openSong() }, Alignment.Center) {
                        Spacer(
                            Modifier
                                .size(170.dp)
                                .background(Color.DarkGray)
                        )

                        GlideImage(
                            data.getMusicData()?.thumbnail,
                            data.getMusicData()?.name,
                            Modifier.size(175.dp),
                            contentScale = ContentScale.Crop
                        )

                        Row(
                            Modifier
                                .offset(x = 10.dp)
                                .align(Alignment.TopEnd),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            ImageIcon(R.drawable.ic_pin, 24)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    TextViewNormal(data.getMusicData()?.name ?: "", 16, line = 1, center = true)
                    Spacer(Modifier.height(20.dp))
                }

                TextViewBold(data.caption ?: "", 20, center = true)
            }
        }

        if (data?.emoji != null) Box(Modifier
            .rotate(-20f)
            .offset(y = 30.dp, x = (-10).dp)
            .clickable { openEmoji() }
            .align(Alignment.BottomEnd)) {
            TextViewBold(data.emoji ?: "", 50)
        }

    }
    Spacer(Modifier.height(30.dp))
}

@OptIn(UnstableApi::class)
@Composable
fun ConnectVibeItemMedia(data: ConnectFeedDataResponse?, openEmoji: () -> Unit) {
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

            if (data?.emoji != null) Box(Modifier
                .rotate(-20f)
                .offset(y = 30.dp, x = (-10).dp)
                .clickable { openEmoji() }
                .align(Alignment.BottomEnd)) {
                TextViewBold(data.emoji ?: "", 50)
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