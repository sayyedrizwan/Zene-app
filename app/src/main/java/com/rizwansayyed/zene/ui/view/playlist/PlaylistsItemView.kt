package com.rizwansayyed.zene.ui.view.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.formatDurationsForVideo
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PodcastItemView(data: ZeneMusicData, info: MusicPlayerData?, list: ZeneMusicDataList) {
    Row(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .combinedClickable(onLongClick = {
            NavigationUtils.triggerInfoSheet(data)
        }, onClick = {
            startMedia(data, list)
        })
        .padding(horizontal = 15.dp, vertical = 25.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            TextViewSemiBold(data.name ?: "", 16, line = 3)
            Spacer(Modifier.height(10.dp))
            TextViewNormal(data.artists ?: "", 13, line = 3)
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Start, Alignment.CenterVertically) {
                Spacer(Modifier.width(2.dp))
                ImageIcon(R.drawable.ic_clock, 16)
                Spacer(Modifier.width(5.dp))
                TextViewNormal(data.timeAgo(), 15)

                Spacer(Modifier.weight(1f))
                ImageIcon(R.drawable.ic_play, 17)
                Spacer(Modifier.width(5.dp))
                TextViewNormal(formatDurationsForVideo(data.extraInfo?.toFloatOrNull() ?: 0f), 15)
            }
        }

        if (info?.data?.id == data.id) GlideImage(
            R.raw.song_playing_wave, "", Modifier.size(24.dp), contentScale = ContentScale.Crop
        )
        else ImageIcon(R.drawable.ic_play, 25)
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistsItemView(data: ZeneMusicData, info: MusicPlayerData?, list: ZeneMusicDataList) {
    Row(Modifier
        .padding(top = 15.dp)
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(13.dp))
        .background(BlackGray)
        .combinedClickable(onLongClick = {
            NavigationUtils.triggerInfoSheet(data)
        }, onClick = {
            startMedia(data, list)
        })
        .padding(horizontal = 15.dp, vertical = 25.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        GlideImage(
            data.thumbnail,
            data.name,
            Modifier
                .padding(end = 10.dp)
                .size(60.dp),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.weight(1f), Arrangement.Center, Alignment.Start) {
            TextViewSemiBold(data.name ?: "", 15, line = 3)
            Spacer(Modifier.height(2.dp))
            TextViewNormal(data.artists ?: "", 12, line = 1)
        }

        if (info?.data?.id == data.id) GlideImage(
            R.raw.song_playing_wave, "", Modifier.size(24.dp), contentScale = ContentScale.Crop
        )
        else ImageIcon(R.drawable.ic_play, 25)
    }
}
