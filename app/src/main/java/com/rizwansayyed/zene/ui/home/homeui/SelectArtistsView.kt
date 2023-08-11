package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.shortTextForView
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
fun TrendingSongsViewShortText(artists: TopArtistsSongs) {
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
                .size(160.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandSemiBold(artists.name?.shortTextForView(17) ?: "", size = 17)

        QuickSandLight(artists.artist?.shortTextForView(24) ?: "", size = 12)
    }
}

@Composable
fun RecentPlayedItemView(recent: RecentPlayedEntity) {
    Card(
        Modifier
            .padding(6.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - 140.dp)
            .clickable {
                "runnn".showToast()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlackLight),
    ) {
        Box {
            Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    recent.img, recent.name,
                    Modifier
                        .size(100.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(13.dp))
                )
                Column {
                    QuickSandRegular(recent.name, size = 17, maxLine = 1)
                    Spacer(modifier = Modifier.height(5.dp))
                    QuickSandLight(recent.artists, size = 11, maxLine = 1)
                }
            }

            LinearProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(2.dp),
                trackColor = Color.Transparent,
                color = Color.Red,
                progress = 0.7f
            )
        }
    }
}