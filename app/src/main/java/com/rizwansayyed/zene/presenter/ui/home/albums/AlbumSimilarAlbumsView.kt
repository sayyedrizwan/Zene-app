package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.home.online.AlbumsItems
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel


@Composable
fun SimilarAlbums() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    Spacer(Modifier.height(30.dp))

    when (val v = playlistAlbum.similarAlbumPlaylistAlbum) {
        DataResponse.Loading -> {}
        is DataResponse.Success -> LazyRow(Modifier.fillMaxWidth()) {
            items(v.item) {
                AlbumsItems(it) {
                    homeNav.setAlbum(it.pId ?: "")
                }
            }
        }

        else -> {}
    }
}