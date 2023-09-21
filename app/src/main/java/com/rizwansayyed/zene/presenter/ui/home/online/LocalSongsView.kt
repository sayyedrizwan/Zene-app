package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.TextLight
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

@Composable
fun LocalSongsTop() {
    val offlineSongsViewModel: OfflineSongsViewModel = hiltViewModel()

    if (offlineSongsViewModel.top20Songs.isNotEmpty())
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(Modifier.height(80.dp))

            TopInfoWithSeeMore(R.string.recent_device_songs, R.string.switch_) {
            }

            LazyRow {
                items(offlineSongsViewModel.top20Songs) {
                    LocalSongsItems(it)
                }
            }
        }

    LaunchedEffect(Unit) {
        offlineSongsViewModel.latestSongs()
    }
}

@Composable
fun LocalSongsItems(song: OfflineSongsDetailsResult) {
    val width = LocalConfiguration.current.screenWidthDp

    Column(Modifier.padding(horizontal = 5.dp)) {
        var failed by remember { mutableStateOf(false) }

        if (failed)
            Image(
                painterResource(id = R.drawable.ic_music_note),
                "",
                Modifier.background(LightBlack).size((width / 1.3).dp).padding((width / 3.2).dp),
                 colorFilter = ColorFilter.tint(Color.White)
            )
        else
            AsyncImage(
                song.art, "", Modifier.size((width / 1.3).dp),
                contentScale = ContentScale.Crop,
                onError = { failed = true }
            )

        Spacer(Modifier.height(14.dp))

        TextSemiBold(song.title, Modifier.width((width / 1.4).dp), singleLine = true)

        Spacer(Modifier.height(4.dp))

        TextThin(song.artist, Modifier.width((width / 1.4).dp), singleLine = true)

        Spacer(Modifier.height(16.dp))
    }
}