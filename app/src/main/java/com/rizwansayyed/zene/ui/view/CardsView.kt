package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType.*
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MOOD
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_PLAYLISTS
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.convertItToMoney
import com.rizwansayyed.zene.utils.Utils.openBrowser
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.utils.Utils.ytThumbnail

@Composable
fun SimpleCardsView(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable { openSpecificIntent(m, list) }) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .width(220.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }
    }
}


@Composable
fun CardsViewDesc(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                openSpecificIntent(m, list)
            }) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .width(220.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }

        Row(
            Modifier
                .width(220.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppinsThin(m.artists ?: " ", false, size = 14, limit = 1)
        }
    }
}


@Composable
fun VideoCardsViewWithSong(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable {
                openSpecificIntent(m, list)
            }) {

        Box(Modifier.size(width = 280.dp, height = 170.dp)) {
            AsyncImage(
                imgBuilder(ytThumbnail(m.extra ?: "")),
                m.name,
                Modifier
                    .align(Alignment.Center)
                    .size(width = 280.dp, height = 170.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            ImageView(
                R.drawable.ic_youtube,
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)
                    .size(20.dp)
            )
        }

        Row(
            Modifier
                .width(280.dp)
                .padding(top = 9.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppins(m.name ?: " ", false, size = 16, limit = 1)
        }

        Row(
            Modifier
                .width(280.dp)
                .padding(horizontal = 7.dp)
        ) {
            TextPoppinsThin(m.artists ?: " ", false, size = 14, limit = 1)
        }
    }
}


@Composable
fun CardRoundTextOnly(m: ZeneMusicDataItems) {
    Row(
        Modifier
            .padding(6.dp)
            .width(200.dp)
            .clip(RoundedCornerShape(10))
            .background(MainColor)
            .bouncingClickable { openSpecificIntent(m, emptyList()) },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        TextPoppins(m.name ?: "", true, size = 16, limit = 2)
    }
}

@Composable
fun CardSmallWithListeningNumber(m: ZeneMusicDataItems, list: ZeneMusicDataResponse) {
    val listeners = stringResource(R.string.listeners)

    Row(
        Modifier
            .padding(6.dp)
            .width(300.dp)
            .bouncingClickable {
                openSpecificIntent(m, list)
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(start = 9.dp, end = 4.dp)
        ) {
            TextPoppins(m.name ?: "", false, size = 16, limit = 1)
            Spacer(Modifier.height(4.dp))
            TextPoppinsThin(m.artists ?: "", false, size = 16, limit = 1)
            Spacer(Modifier.height(4.dp))
            Row(Modifier, Arrangement.Center) {
                Box(Modifier.offset(y = 4.dp)) {
                    ImageIcon(R.drawable.ic_play, 15)
                }
                Spacer(Modifier.width(4.dp))
                TextPoppinsThin(
                    "${convertItToMoney(m.extra ?: "")} $listeners", size = 14, limit = 1
                )
            }
        }
    }
}

@Composable
fun ArtistsCardView(m: ZeneMusicDataItems, data: ZeneMusicDataResponse) {
    Column(
        Modifier
            .padding(7.dp)
            .bouncingClickable { openSpecificIntent(m, data) },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 5.dp)
                .width(170.dp)
        ) {
            TextPoppinsThin(m.name ?: "", true, size = 17, limit = 1)
        }
    }
}


@Composable
fun SongDynamicCards(m: ZeneMusicDataItems, list: ZeneMusicDataResponse) {
    Column(
        Modifier
            .padding(bottom = 15.dp)
            .padding(7.dp)
            .bouncingClickable {
                openSpecificIntent(m, list)
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(m.thumbnail),
            m.name,
            Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.DarkGray),
            contentScale = ContentScale.Crop
        )

        Row(
            Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 2.dp)
        ) {
            TextPoppins(m.name ?: "", true, size = 17, limit = 2)
        }
        Row(
            Modifier
                .padding(top = 2.dp)
                .padding(horizontal = 2.dp)
        ) {
            TextPoppinsThin(m.artists ?: "", true, size = 15, limit = 2)
        }
    }
}

fun openSpecificIntent(m: ZeneMusicDataItems, list: List<ZeneMusicDataItems>) {
    when (m.type()) {
        SONGS -> sendWebViewCommand(m, list)
        PLAYLIST -> m.id?.let { sendNavCommand(NAV_PLAYLISTS.replace("{id}" , it)) }
        ALBUMS -> m.id?.let { sendNavCommand(NAV_PLAYLISTS.replace("{id}" , it)) }
        ARTISTS -> {}
        VIDEO -> openVideoPlayer(m.extra)
        MOOD -> m.id?.let { sendNavCommand(NAV_MOOD.replace("{id}" , it)) }
        STORE -> m.id?.let { openBrowser(it) }
        NONE -> {}
    }
}