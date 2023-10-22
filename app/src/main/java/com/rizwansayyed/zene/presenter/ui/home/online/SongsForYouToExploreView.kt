package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun SongsForYouToExplore() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.songsSuggestionForUsers) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.song_for_you_to_explore), null) {}

            ArtistsFanLoading()
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(stringResource(id = R.string.song_for_you_to_explore), null) {}
        }
    }
}