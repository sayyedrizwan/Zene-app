package com.rizwansayyed.zene.presenter.ui.home.feed.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.theme.PurpleGrey80
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel


@Composable
fun PinnedArtistsList(artists: PinnedArtistsEntity) {
    val navViewModel: HomeNavViewModel = hiltViewModel()

    Column(
        Modifier
            .padding(11.dp)
            .clickable { navViewModel.setArtists(artists.name) },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            artists.thumbnail, artists.name,
            Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(100))
                .background(PurpleGrey80),
            error = painterResource(R.drawable.singer_icon),
            contentScale = ContentScale.Crop
        )

        Box(
            Modifier
                .offset(y = (-10).dp)
                .clip(RoundedCornerShape(100))
                .background(MainColor)
        ) {
            SmallIcons(R.drawable.ic_pin, 16, 5)
        }

        TextSemiBold(v = artists.name.substringBefore(" "))
    }
}