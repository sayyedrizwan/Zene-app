package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.Utils.ytThumbnail

@Composable
fun SimpleCardsView(m: ZeneMusicDataItems) {
    Column(
        Modifier
            .padding(7.dp)
            .clickable { }) {
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
fun CardsViewDesc(m: ZeneMusicDataItems) {
    Column(
        Modifier
            .padding(7.dp)
            .clickable { }) {

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
fun VideoCardsViewWithSong(m: ZeneMusicDataItems) {
    Column(
        Modifier
            .padding(7.dp)
            .clickable { }) {

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
            .clickable {  }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextPoppins(m.name ?: "", true, size = 16, limit = 2)
    }
}