package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun SongYouMayTitle(recentPlayList: List<RecentPlayedEntity>) {
    Column {
        if (recentPlayList.isEmpty()) {
            TopInfoWithSeeMore(R.string.select_your_fav_artists, null) {}

            TextThin(
                stringResource(id = R.string.songs_you_may_like_desc),
                Modifier
                    .padding()
                    .fillMaxWidth(),
                true
            )

            Spacer(Modifier.height(20.dp))
        } else
            TopInfoWithSeeMore(R.string.songs_you_may_like, null) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveArtistsButton(m: Modifier) {
    Card(
        onClick = { /*TODO*/ }, modifier = m
            .fillMaxWidth()
            .padding(10.dp)
            .padding(bottom = 30.dp), elevation = CardDefaults.cardElevation(13.dp),
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(PurpleGrey80)
    ) {
        TextSemiBold(
            stringResource(id = R.string.suggest_me_now),
            Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            true, Color.Black
        )
    }
}

@Composable
fun SelectFavArtists(artists: MusicData, nav: HomeNavViewModel, click: () -> Unit) {
    Column(
        Modifier
            .padding(6.dp)
            .clickable {
                click()
            }) {
        AsyncImage(
            artists.thumbnail, artists.name,
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .border(
                    if (nav.selectArtists.contains(artists.name?.lowercase())) 2.dp else 0.dp,
                    if (nav.selectArtists.contains(artists.name?.lowercase())) Color.Red else Color.Transparent,
                    RoundedCornerShape(50)
                )
        )

        TextThin(
            artists.name ?: "",
            Modifier
                .fillMaxWidth()
                .padding(4.dp), true, singleLine = true
        )
    }
}
