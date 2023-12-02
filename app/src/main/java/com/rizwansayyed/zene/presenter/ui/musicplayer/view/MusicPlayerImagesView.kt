package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.artists.ArtistPhotoAlbum
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.tempEmptyList
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@Composable
fun MusicPlayerImages(playerViewModel: PlayerViewModel) {

    when (val v = playerViewModel.musicImages) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.song_related_photos, null) {}

            ArtistPhotoAlbum(tempEmptyList, true)
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithSeeMore(R.string.song_related_photos, null) {}

                ArtistPhotoAlbum(v.item, false)
            }
        }
    }
}