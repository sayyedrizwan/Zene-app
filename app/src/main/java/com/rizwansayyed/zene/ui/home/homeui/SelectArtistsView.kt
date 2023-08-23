package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.ui.theme.BlackTransparentLight
import com.rizwansayyed.zene.utils.QuickSandLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.progressBarStatus
import com.rizwansayyed.zene.utils.Utils.shortTextForView
import com.rizwansayyed.zene.utils.Utils.showToast
import java.lang.Float

@Composable
fun ArtistsView(artists: TopArtistsSongs, search: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                search(artists.artist ?: "")
            }
    ) {
        AsyncImage(
            model = artists.img,
            contentDescription = artists.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandLight(artists.name ?: "", size = 17)
    }
}


@Composable
fun ArtistsViewSmallView(artists: TopArtistsSongs, search: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                search(artists.name ?: "")
            }
    ) {
        AsyncImage(
            model = artists.img,
            contentDescription = artists.name,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandLight(artists.name?.shortTextForView(17) ?: "", size = 17)
    }
}

@Composable
fun TrendingSongsView(songs: TopArtistsSongs, search: (String, String, String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                search(songs.img ?: "", songs.name ?: "", songs.artist ?: "")
            }
    ) {
        AsyncImage(
            model = songs.img,
            contentDescription = songs.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandSemiBold(songs.name ?: "", size = 17)

        QuickSandLight(songs.artist ?: "", size = 12)
    }
}


@Composable
fun TrendingSongsViewShortText(songs: TopArtistsSongs, search: (String, String, String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                search(songs.img ?: "", songs.name ?: "", songs.artist ?: "")
            }
    ) {
        AsyncImage(
            model = songs.img,
            contentDescription = songs.name,
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickSandSemiBold(songs.name?.shortTextForView(17) ?: "", size = 17)

        QuickSandLight(songs.artist?.shortTextForView(24) ?: "", size = 12)
    }
}

@Composable
fun RecentPlayedItemView(recent: RecentPlayedEntity, search: (String, String, String) -> Unit) {
    Card(
        Modifier
            .padding(6.dp)
            .width(LocalConfiguration.current.screenWidthDp.dp - 140.dp)
            .clickable {
                search(recent.img, recent.name, recent.artists)
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
                        .clip(RoundedCornerShape(13.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    QuickSandRegular(
                        recent.name.trim(), size = 17, maxLine = 1, align = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    QuickSandLight(
                        recent.artists.trim(), size = 11, maxLine = 1, align = TextAlign.Start
                    )
                }
            }

            val progress = progressBarStatus(recent.playerDuration, recent.lastListenDuration)

            if (progress?.isNaN() == false)
                LinearProgressIndicator(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .align(alignment = BottomCenter)
                        .fillMaxWidth()
                        .height(2.dp),
                    trackColor = Color.Transparent,
                    color = Color.Red,
                    progress = progress
                )
        }
    }
}

@Composable
fun FullCardSongView(song: TopArtistsSongs, search: (String, String, String) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable {
                search(song.img ?: "", song.name ?: "", song.artist ?: "")
            }) {
        AsyncImage(
            model = song.img,
            contentDescription = "",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .height(LocalConfiguration.current.screenHeightDp.dp / 2 + 130.dp)
                .align(Center),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .padding(17.dp)
                .clip(RoundedCornerShape(18.dp))
                .align(BottomCenter)
                .background(BlackTransparentLight)
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(13.dp))
            QuickSandSemiBold(
                song.name ?: "",
                size = 17,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(3.dp))
            QuickSandLight(
                song.artist ?: "",
                size = 12,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(13.dp))
        }

    }
}