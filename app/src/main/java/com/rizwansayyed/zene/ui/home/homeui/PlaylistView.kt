package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.shortTextForView

@Composable
fun PlaylistView(playlist: PlaylistEntity, openPlaylist: (PlaylistEntity) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                openPlaylist(playlist)
            }
    ) {
        if (playlist.image1.isEmpty())
            Image(
                painterResource(id = R.drawable.ic_playlist), "",
                Modifier
                    .size(160.dp)
                    .padding(50.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colorFilter = ColorFilter.tint(Color.White)
            )
        else
            AsyncImage(
                playlist.image1,
                contentDescription = playlist.name,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
            )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandLight(playlist.name.shortTextForView(24), size = 15)
    }
}