package com.rizwansayyed.zene.ui.playlists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.model.MusicType
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.ui.view.shimmerEffectBrush

@Composable
fun PlaylistAlbumTopView(v: ZeneMusicDataItems?) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    var fullDesc by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            imgBuilder(v?.thumbnail), v?.name,
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp))
        )

        Spacer(Modifier.height(15.dp))
        TextPoppins(v?.name ?: "", true, size = 25)
        Spacer(Modifier.height(5.dp))
        if (v?.type() == MusicType.ALBUMS) {
            TextPoppinsThin(v.artists ?: "", true, size = 17)
        }

        Spacer(Modifier.height(7.dp))

        TextPoppinsThin(
            v?.extra?.substringBefore("From Wikipedia")?.trim() ?: "",
            true, size = 15, limit = if (fullDesc) 10000 else 3
        )
        if ((v?.extra?.length ?: 0) > 350) {
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier
                    .rotate(if (fullDesc) 90f else -90f)
                    .clickable { fullDesc = !fullDesc }) {
                ImageIcon(R.drawable.ic_arrow_left, 30)
            }
        }
    }
}

@Composable
fun LoadingAlbumTopView(modifier: Modifier = Modifier) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    Column(
        modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp))
                .background(shimmerEffectBrush())
        )

        Spacer(Modifier.height(15.dp))

        Spacer(
            Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 5.dp)
                .size(160.dp, 13.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )

        Spacer(Modifier.height(7.dp))


        Spacer(
            Modifier
                .padding(top = 9.dp)
                .padding(horizontal = 5.dp)
                .size(120.dp, 13.dp)
                .clip(RoundedCornerShape(40))
                .background(shimmerEffectBrush())
        )
    }
}