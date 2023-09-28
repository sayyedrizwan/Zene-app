package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun SongsYouMayLike() {

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