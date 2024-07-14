package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems

@Composable
fun SimpleCardsView(m: ZeneMusicDataItems, click: () -> Unit) {
    Column(
        Modifier
            .padding(7.dp)
            .clickable { click() }) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current).data(m.thumbnail)
                .memoryCachePolicy(CachePolicy.ENABLED).build(),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray)
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
fun CardsViewDesc(m: ZeneMusicDataItems, click: () -> Unit) {
    Column(
        Modifier
            .padding(7.dp)
            .clickable { click() }) {
        AsyncImage(
            ImageRequest.Builder(LocalContext.current).data(m.thumbnail)
                .memoryCachePolicy(CachePolicy.ENABLED).build(),
            m.name,
            Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray)
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
                .padding(horizontal = 7.dp)) {
            TextPoppinsThin(m.artists ?: " ", false, size = 14, limit = 1)
        }
    }
}