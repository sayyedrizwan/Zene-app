package com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.viewmodel.PlaylistImportViewModel


@Composable
fun ImportPlaylistView(viewModel: PlaylistImportViewModel) {
    val width = LocalConfiguration.current.screenWidthDp / 1.3
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))

        AsyncImage(
            viewModel.selectedPlaylist?.thumbnail,
            viewModel.selectedPlaylist?.name,
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(width.dp)
        )

        Spacer(Modifier.height(10.dp))
        TextSemiBold(v = viewModel.selectedPlaylist?.name ?: "")
        viewModel.selectedPlaylist?.desc?.let {
            Spacer(Modifier.height(5.dp))
            TextThin(v = viewModel.selectedPlaylist?.desc ?: "")
        }
        Spacer(Modifier.height(15.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistTrackList(
    track: ImportPlaylistTrackInfoData, i: Int, click: (isMenu: Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(vertical = 30.dp)
            .combinedClickable(onClick = { click(false)}, onLongClick = {})
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextSemiBold(v = "${i + 1}", Modifier.padding(horizontal = 8.dp), doCenter = true)
        Spacer(Modifier.height(3.dp))
        AsyncImage(
            track.thumbnail, "",
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(3.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
        ) {
            TextSemiBold(v = track.songName ?: "", singleLine = true, size = 15)
            Spacer(Modifier.height(5.dp))
            TextThin(v = track.artistsName ?: "", singleLine = true, size = 14)
        }

        MenuIcon {
            click(true)
        }
    }
}