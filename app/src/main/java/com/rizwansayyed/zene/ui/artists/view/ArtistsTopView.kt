package com.rizwansayyed.zene.ui.artists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush

@Composable
fun ArtistsTopView(v: ZeneArtistsInfoResponse) {
    var fullDesc by remember { mutableStateOf(false) }

    Column {
        LazyRow(Modifier.fillMaxWidth()) {
            items(v.img ?: emptyList()) {
                ArtistsTopImagesView(it, v.name, false)
            }
        }

        Spacer(Modifier.height(30.dp))
        Row(Modifier.padding(horizontal = 10.dp)) {
            TextPoppins(v.name ?: "", size = 30)
        }
        Spacer(Modifier.height(10.dp))
        Row(
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .clickable { fullDesc = !fullDesc }) {
            TextPoppins(
                v.desc?.trim() ?: "",
                size = 13,
                limit = if (fullDesc) 10000 else 3
            )
        }
    }
}

@Composable
fun ArtistsTopViewLoading() {
    Column {
        LazyRow(Modifier.fillMaxWidth()) {
            items(30) {
                ArtistsTopImagesView("", "", true)
            }
        }

        Spacer(Modifier.height(30.dp))
        Spacer(
            Modifier
                .padding(horizontal = 5.dp)
                .size(100.dp, 10.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}