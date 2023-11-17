package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@Composable
fun ArtistsNews() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.artistsNews) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.artist_news, null) {}
        }
        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.artist_news, null) {}
            TopInfoWithSeeMore("${v.item.channel?.items?.size}", null) {}

        }
    }
}