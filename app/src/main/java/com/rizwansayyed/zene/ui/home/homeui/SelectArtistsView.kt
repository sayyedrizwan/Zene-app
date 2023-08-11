package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.showToast

@Composable
fun ArtistsView(artists: TopArtistsSongs) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                "open artists".showToast()
            }
    ) {
        AsyncImage(
            model = artists.img,
            contentDescription = artists.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(50)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandLight(artists.name ?: "", size = 17)
    }
}

@Composable
fun TrendingSongsView(artists: TopArtistsSongs) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                "open song".showToast()
            }
    ) {
        AsyncImage(
            model = artists.img,
            contentDescription = artists.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandSemiBold(artists.name ?: "", size = 17)

        QuickSandLight(artists.artist ?: "", size = 12)
    }
}

@Composable
fun RecentPlayedItemView(recent: RecentPlayedEntity) {
    Card(
        Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 20.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlackLight),
    ) {
        QuickSandSemiBold(recent.artists, size = 17)
    }
}