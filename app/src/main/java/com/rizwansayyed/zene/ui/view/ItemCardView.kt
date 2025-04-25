package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemSmallCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(horizontal = 9.dp)
            .width(105.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .size(100.dp)
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail, data?.name, Modifier.size(105.dp), contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(4.dp))
        TextViewNormal(data?.name ?: "", 14, line = 1)
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemYoutubeCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(horizontal = 4.dp)
            .width(255.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            GlideImage(
                data?.thumbnail, data?.name, Modifier
                    .width(250.dp)
                    .clip(RoundedCornerShape(13.dp))
            )
        }
        Spacer(Modifier.height(4.dp))
        TextViewNormal(data?.name ?: "", 14, line = 1)
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun MoviesImageCard(data: ZeneMusicData?, p: Int? = null) {
    Box(
        Modifier.combinedClickable(
            onLongClick = { NavigationUtils.triggerInfoSheet(data) },
            onClick = { startMedia(data) })
    ) {
        GlideImage(
            data?.thumbnail,
            data?.name,
            Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(13.dp))
                .width(250.dp)
                .height(370.dp),
            contentScale = ContentScale.Crop
        )


        if (p != null) Row(Modifier.align(Alignment.BottomStart)) {
            TextViewBold("${p + 1}", 100)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MoviesImageCardConnect(data: ZeneMusicData?, click: () -> Unit) {
    Box(Modifier.clickable { click() }) {
        GlideImage(
            data?.thumbnail,
            data?.name,
            Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(13.dp))
                .width(250.dp)
                .height(370.dp),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(6.dp)
                .padding(end = 6.dp)
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.Black),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_plus_sign, 20)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun NewsItemCard(data: ZeneMusicData?) {
    Row(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(vertical = 20.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        GlideImage(
            data?.thumbnail,
            data?.name,
            Modifier
                .padding(horizontal = 10.dp)
                .size(100.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Color.White),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxWidth()) {
            TextViewNormal(data?.name ?: "", 15, line = 3)
            TextViewNormal(data?.extra ?: "", 12)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemCardView(data: ZeneMusicData?, list: List<ZeneMusicData?> = emptyList()) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data, list) })
            .padding(horizontal = 9.dp)
            .width(175.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .size(170.dp)
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail, data?.name, Modifier.size(175.dp), contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 16, line = 1)
        if (data?.type() != MusicDataTypes.PLAYLISTS) {
            if (data?.type() == MusicDataTypes.PODCAST) {
                TextViewLight(data.artists ?: "", 13, line = 3)
            } else Box(Modifier.offset(y = (-2).dp)) {
                TextViewLight(data?.artists ?: "", 14, line = 1)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemCardViewDynamic(data: ZeneMusicData?, list: List<ZeneMusicData?> = emptyList()) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data, list) })
            .padding(horizontal = 9.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier.fillMaxWidth().aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 16, line = 1)
        if (data?.type() != MusicDataTypes.PLAYLISTS) {
            if (data?.type() == MusicDataTypes.PODCAST) {
                TextViewLight(data.artists ?: "", 13, line = 3)
            } else Box(Modifier.offset(y = (-2).dp)) {
                TextViewLight(data?.artists ?: "", 14, line = 1)
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ItemArtistsCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(horizontal = 5.dp)
            .width(155.dp)
    ) {
        Box(Modifier.fillMaxWidth(), Alignment.Center) {
            Spacer(
                Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(9.dp))
        TextViewBold(data?.name ?: "", 15, center = true, line = 1)
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PodcastViewItems(data: ZeneMusicData?) {
    Box(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .fillMaxWidth()
            .padding(3.dp)
    ) {
        GlideImage(data?.thumbnail, data?.name, Modifier.fillMaxWidth())

        if ((data?.extra ?: "").length > 3) Row(
            Modifier
                .padding(bottom = 4.dp, end = 4.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(80))
                .background(if (data?.podcastTimestamp() == true) Color.Red else Color.Gray)
                .padding(horizontal = 6.dp)
        ) {
            TextViewNormal(data?.extra ?: "", size = 14)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun VideoCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(horizontal = 9.dp)
            .padding(bottom = 19.dp)
            .width(245.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .width(245.dp)
        ) {
            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier
                    .size(240.dp, 150.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .padding(end = 5.dp)
            ) {
                TextViewBold(data?.extra ?: "", 13, line = 1)
            }
        }
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 15, line = 1)
        Box(
            Modifier
                .fillMaxWidth()
                .offset(y = (-3).dp)
        ) {
            TextViewLight(data?.artists ?: "", 12, line = 1)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun FullVideoCardView(data: ZeneMusicData?) {
    Row(
        Modifier
            .combinedClickable(
                onLongClick = { NavigationUtils.triggerInfoSheet(data) },
                onClick = { startMedia(data) })
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        Box(
            Modifier
                .weight(3f)
                .width(145.dp)
        ) {
            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier
                    .size(140.dp, 100.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .padding(end = 5.dp)
            ) {
                TextViewBold(data?.extra ?: "", 13, line = 1)
            }
        }

        Column(
            Modifier
                .weight(7f)
                .padding(start = 10.dp, end = 10.dp)
                .height(100.dp),
            Arrangement.Center
        ) {
            TextViewNormal(data?.name ?: "", 15, line = 2)
            TextViewLight(data?.artists ?: "", 13, line = 1)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCardViewConnect(data: ZeneMusicData?, click: () -> Unit) {
    Column(
        Modifier
            .clickable { click() }
            .padding(horizontal = 9.dp)
            .width(175.dp)) {
        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .size(170.dp)
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail, data?.name, Modifier.size(175.dp), contentScale = ContentScale.Crop
            )

            Row(
                Modifier
                    .padding(4.dp)
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_plus_sign, 20)
            }
        }
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 16, line = 1)
        if (data?.type() != MusicDataTypes.PLAYLISTS) {
            if (data?.type() == MusicDataTypes.PODCAST) {
                TextViewLight(data.artists ?: "", 13, line = 3)
            } else Box(Modifier.offset(y = (-2).dp)) {
                TextViewLight(data?.artists ?: "", 14, line = 1)
            }
        }
    }
}


// connect items


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun VideoCardViewConnect(data: ZeneMusicData?, click: () -> Unit) {
    Column(
        Modifier
            .clickable { click() }
            .padding(horizontal = 9.dp)
            .padding(bottom = 19.dp)
            .width(245.dp)) {
        Box(
            Modifier
                .fillMaxWidth()
                .width(245.dp)
        ) {
            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier
                    .size(240.dp, 150.dp)
                    .clip(RoundedCornerShape(5.dp)),
                contentScale = ContentScale.Crop
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(5.dp)
                    .padding(end = 5.dp)
            ) {
                TextViewBold(data?.extra ?: "", 13, line = 1)
            }

            Row(
                Modifier
                    .padding(9.dp)
                    .align(Alignment.BottomEnd)
                    .size(24.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ImageIcon(R.drawable.ic_plus_sign, 20)
            }
        }
        Spacer(Modifier.height(9.dp))
        TextViewNormal(data?.name ?: "", 15, line = 1)
        Box(
            Modifier
                .fillMaxWidth()
                .offset(y = (-3).dp)
        ) {
            TextViewLight(data?.artists ?: "", 12, line = 1)
        }
    }
}
