package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@Composable
fun AlbumTopInfoDetails() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when(artistsViewModel.playlistAlbum){
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {

        }
        is DataResponse.Success -> {

        }
    }
}