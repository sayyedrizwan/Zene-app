package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItemsShort
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@Composable
fun AlbumTopInfoDetails() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val width = LocalConfiguration.current.screenWidthDp.dp

    Spacer(Modifier.height(80.dp))

    when (val v = artistsViewModel.playlistAlbum) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {

        }

        is DataResponse.Success -> {
            AsyncImage(
                v.item.thumbnail, v.item.name,
                Modifier
                    .padding(10.dp)
                    .size(width),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(5.dp))

            TextSemiBold(v.item.name ?: "", Modifier.fillMaxSize(), true, size = 26)

            Spacer(Modifier.height(5.dp))

            TextThin(v.item.artistsName ?: "", Modifier.fillMaxSize(), true, size = 20)
        }
    }

    Spacer(Modifier.height(80.dp))
}

@Composable
fun AlbumsSongsList(music: MusicData, menu:() -> Unit, play:() -> Unit) {
    Row(
        Modifier
            .padding(vertical = 4.dp)
            .padding(bottom = 12.dp)
            .fillMaxSize()
            .clickable {
                play()
            },
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))

        AsyncImage(
            music.thumbnail, music.name,
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4))
        )

        Spacer(Modifier.width(8.dp))
        
        TextRegular(music.name ?: "", Modifier.weight(1f), size = 15)

        MenuIcon {
            menu()
        }
    }
}

@Composable
fun ArtistsDesc() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    
    when (val v = artistsViewModel.playlistAlbum) {
        is DataResponse.Success -> TextThin(v.item.description ?: "", Modifier.fillMaxWidth(), true, size = 16)

        else -> {}
    }
}

@Composable
fun SimilarArtistsAlbums() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    Column(Modifier.fillMaxWidth()) {
        TopInfoWithSeeMore(stringResource(id = R.string.albums), null) {}
        when (val v = artistsViewModel.similarAlbumPlaylistAlbum) {
            is DataResponse.Success -> LazyRow(Modifier.fillMaxWidth()) {
                items(v.item) { album ->
                    AlbumsItemsShort(album) {
                        homeNav.setAlbum(album.pId ?: "")
                    }
                }
            }

            else -> {}
        }
    }
}