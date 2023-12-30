package com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.home.online.MostPlayedSongsLoading
import com.rizwansayyed.zene.viewmodel.PlaylistImportViewModel

@Composable
fun PlaylistListView(viewModel: PlaylistImportViewModel, offset: Int, click: () -> Unit) {
    when (val v = viewModel.usersPlaylists) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {
            Spacer(Modifier.height(50.dp))

            TextSemiBold(
                v = stringResource(R.string.error_loading_playlists),
                Modifier.fillMaxSize(), true
            )

            Spacer(Modifier.height(50.dp))
        }

        DataResponse.Loading -> MostPlayedSongsLoading()
        is DataResponse.Success -> {
            Spacer(Modifier.height(15.dp))

            Row {
                Spacer(Modifier.width(7.dp))

                TextSemiBold(v = stringResource(R.string.playlists))

                Spacer(Modifier.weight(1f))

                if (offset == 0)
                    SmallIcons(icon = R.drawable.ic_arrow_down_sharp, 20, 0, click)
                else
                    Box(Modifier.rotate(180f)) {
                        SmallIcons(icon = R.drawable.ic_arrow_down_sharp, 20, 0, click)
                    }

                Spacer(Modifier.width(7.dp))
            }

            Spacer(Modifier.height(10.dp))

            LazyRow(Modifier.fillMaxWidth()) {
                item {
                    Spacer(Modifier.height(10.dp))
                }

                items(v.item) {
                    PlaylistImportItems(it, viewModel.selectedPlaylist?.id == it.id) {
                        it.let { it1 -> viewModel.spotifyPlaylistTrack(it1) }
                    }
                }

                item {
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(25.dp))
        }
    }
}


@Composable
fun PlaylistImportItems(item: ImportPlaylistInfoData, b: Boolean, click: () -> Unit) {
    Column(
        Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 5.dp)
            .clickable { click() }
            .clip(RoundedCornerShape(12.dp))
            .background(if (b) Color.Gray.copy(0.4f) else MainColor)
            .padding(horizontal = 10.dp)
            .padding(top = 10.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            item.thumbnail, item.name,
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(100.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(10.dp))

        TextSemiBold(
            if ((item.name?.length ?: 0) > 15) "${item.name?.substring(0, 15)}..." else item.name
                ?: "",
            size = 14
        )
        item.desc?.let {
            Spacer(Modifier.height(5.dp))
            TextThin(if (it.length > 10) "${it.substring(0, 10)}..." else it, size = 13)
        }
        Spacer(Modifier.height(10.dp))
    }
}