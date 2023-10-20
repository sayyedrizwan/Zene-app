package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun RelatedAlbums() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.albumsYouMayLike) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithImage(stringResource(id = R.string.albums_for_you), null) {}

                v.item.forEach {
                    TextThin(v = it.name ?: "noo")
                }
            }
        }
    }
}