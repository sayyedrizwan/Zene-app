package com.rizwansayyed.zene.ui.view.playlist

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.service.player.PlayerForegroundService.Companion.getPlayerS
import com.rizwansayyed.zene.ui.theme.proximanOverFamily
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType.PODCAST
import com.rizwansayyed.zene.utils.share.MediaContentUtils.TEMP_ZENE_MUSIC_DATA_LIST
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia


@Composable
fun AddSongToQueue(data: PodcastPlaylistResponse, close: () -> Unit) {
    AlertDialog(title = {
        TextViewNormal(stringResource(R.string.add_to_queue), 17, line = 2, center = false)
    }, text = {
        TextViewNormal(stringResource(R.string.queue_desc), 16, center = false)
    }, onDismissRequest = {
        close()
    }, confirmButton = {
        Row {
            TextButton(onClick = {
                close()
                TEMP_ZENE_MUSIC_DATA_LIST.clear()
                TEMP_ZENE_MUSIC_DATA_LIST.addAll(data.list?.toTypedArray() ?: emptyArray())
                if (getPlayerS() == null)
                    startMedia(data.list?.first(), data.list?.toList() ?: emptyList())
                else
                    getPlayerS()?.addListsToNext(data.list?.toList() ?: emptyList())
            }) {
                TextViewNormal(stringResource(R.string.play_next), 15)
            }
            Spacer(Modifier.width(10.dp))
            TextButton(onClick = {
                close()
                TEMP_ZENE_MUSIC_DATA_LIST.clear()
                TEMP_ZENE_MUSIC_DATA_LIST.addAll(data.list?.toTypedArray() ?: emptyArray())

                if (getPlayerS() == null)
                    startMedia(data.list?.first(), data.list?.toList() ?: emptyList())
                else
                    getPlayerS()?.addListsToQueue(data.list?.toList() ?: emptyList())
            }) {
                TextViewNormal(stringResource(R.string.add_to_queue), 15)
            }
        }
    }, dismissButton = {
        TextButton(onClick = {
            close()
        }) {
            TextViewNormal(stringResource(R.string.cancel), 15)
        }
    })
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlaylistTopView(v: ZeneMusicData, type: PlaylistsType) {
    val width = (LocalConfiguration.current.screenWidthDp / 1.5).dp
    var fullDesc by remember { mutableStateOf(false) }
    var shouldShowArrow by remember { mutableStateOf(false) }

    GlideImage(
        v.thumbnail, v.name,
        modifier = Modifier
            .size(width)
            .clip(RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Crop
    )
    Spacer(Modifier.height(15.dp))
    TextViewBoldBig(v.name ?: "", 40, center = true)
    Spacer(Modifier.height(15.dp))
    when (type) {
        PODCAST -> TextViewSemiBold(stringResource(R.string.podcast), 17, center = true)
        else -> {
            TextViewSemiBold(
                stringResource(
                    if (v.type() == MusicDataTypes.ALBUMS) R.string.album else R.string.playlist
                ), 17, center = true
            )

            if (v.type() == MusicDataTypes.ALBUMS) {
                Spacer(Modifier.height(15.dp))
                TextViewNormal(v.extra ?: "", 17, center = true)
            }
        }
    }

    if ((v.artists?.trim()?.length ?: 0) > 5) {
        Spacer(Modifier.height(15.dp))
        Text(
            v.artists ?: "",
            Modifier
                .fillMaxWidth()
                .animateContentSize(),
            Color.White, 14.sp, null, FontWeight.Normal, proximanOverFamily,
            textAlign = TextAlign.Center, maxLines = if (fullDesc) 1000 else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                shouldShowArrow = textLayoutResult.lineCount > 2
            },
        )

        Spacer(Modifier.height(5.dp))

        if (shouldShowArrow) Box(Modifier
            .rotate(if (fullDesc) 180f else 0f)
            .clickable { fullDesc = !fullDesc }) {
            ImageIcon(R.drawable.ic_arrow_down, 28)
        }
    }
    Spacer(Modifier.height(30.dp))
}
