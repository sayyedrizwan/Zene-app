package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import com.rizwansayyed.zene.presenter.ui.TextRegularTwoLine
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun PlaylistList() {
    val roomDb: RoomDbViewModel = hiltViewModel()
    val playlists by roomDb.savePlaylists.collectAsState(initial = emptyList())

    if (playlists.isNotEmpty()) Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

        TopInfoWithSeeMore(R.string.saved_playlists, null) {}

        LazyRow {
            items(playlists) {
                PlaylistItems(it)
            }
        }
    }
}

@Composable
fun PlaylistItems(playlist: SavedPlaylistEntity) {
    val width = LocalConfiguration.current.screenWidthDp

    Column(
        Modifier
            .padding(end = 36.dp)
            .width((width / 2.3).dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {

        if (playlist.thumbnail.isNotEmpty()) Box {
            Image(
                painterResource(id = R.drawable.ic_cd_blue), "",
                Modifier
                    .align(Alignment.Center)
                    .width((width / 2).dp)
                    .offset(x = 60.dp, y = 0.dp)
                    .padding(35.dp)
            )

            AsyncImage(
                playlist.thumbnail, playlist.name,
                Modifier
                    .align(Alignment.Center)
                    .width((width / 2.2 - 40).dp)
                    .clip(RoundedCornerShape(15.dp))
            )
        }
        else
            Image(
                painterResource(id = R.drawable.ic_cd_floating), playlist.name,
                Modifier
                    .width((width / 2.2 - 40).dp)
                    .clip(RoundedCornerShape(15.dp))
                    .padding(20.dp)
            )

        TextRegularTwoLine(playlist.name, doCenter = true)
    }
}