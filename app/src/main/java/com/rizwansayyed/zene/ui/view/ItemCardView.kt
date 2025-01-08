package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemSmallCardView(data: ZeneMusicData?) {
    Column(
        Modifier
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
                data?.thumbnail,
                data?.name,
                Modifier.size(105.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(4.dp))
        TextViewNormal(data?.name ?: "", 14, line = 1)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemYoutubeCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .padding(horizontal = 4.dp)
            .width(255.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            GlideImage(data?.thumbnail, data?.name, Modifier.width(250.dp).clip(RoundedCornerShape(13.dp)))
        }
        Spacer(Modifier.height(4.dp))
        TextViewNormal(data?.name ?: "", 14, line = 1)
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCardView(data: ZeneMusicData?) {
    Column(
        Modifier
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
                data?.thumbnail,
                data?.name,
                Modifier.size(175.dp),
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemArtistsCardView(data: ZeneMusicData?) {
    Column(
        Modifier
            .padding(horizontal = 9.dp)
            .width(175.dp)
    ) {
        Box(Modifier.fillMaxWidth()) {
            Spacer(
                Modifier
                    .size(170.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.DarkGray)
            )

            GlideImage(
                data?.thumbnail,
                data?.name,
                Modifier
                    .size(175.dp)
                    .clip(RoundedCornerShape(100.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(9.dp))
        TextViewBold(data?.name ?: "", 15, center = true, line = 1)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PodcastViewItems(data: ZeneMusicData?) {
    Box(
        Modifier
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