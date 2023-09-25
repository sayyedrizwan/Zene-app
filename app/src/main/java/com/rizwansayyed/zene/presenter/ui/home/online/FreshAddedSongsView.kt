package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtists
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtistsSmall
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun FreshAddedSongsList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    if (homeViewModel.freshAddedSongs.isNotEmpty()) {
        TopInfoWithSeeMore(R.string.fresh_added_songs, null) {}

        LazyRow {
            items(homeViewModel.freshAddedSongs) {
                it?.let { m -> FreshAddedItems(m) }
            }
        }
    }
}

@Composable
fun FreshAddedItems(music: MusicData) {
    val width = LocalConfiguration.current.screenWidthDp
    Column(
        Modifier
            .padding(5.dp)
            .width((width / 1.8).dp)
            .background(LightBlack)
            .clip(RoundedCornerShape(12.dp)),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            music.thumbnail, music.name,
            Modifier
                .padding(5.dp)
                .size((width / 2).dp, (width / 2 + 30).dp),
            contentScale = ContentScale.Crop
        )

        SongsTitleAndArtists(
            music.name ?: "",
            music.artists ?: "",
            Modifier.padding(horizontal = 5.dp).fillMaxWidth(),
            true
        )
    }
}