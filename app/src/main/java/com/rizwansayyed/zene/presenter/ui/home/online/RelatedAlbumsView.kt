package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun RelatedAlbums() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.albumsYouMayLike) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithImage(stringResource(id = R.string.albums_for_you), null) {}
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithImage(stringResource(id = R.string.albums_for_you), null) {}
            }
        }
    }
}

@Composable
fun LoadingAlbumsCards() {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(190.dp)
                .background(shimmerBrush())
        )

        Spacer(Modifier.height(3.dp))

        Spacer(
            Modifier
                .padding(5.dp)
                .size(24.dp, 6.dp)
                .background(shimmerBrush())
        )

        Spacer(Modifier.height(8.dp))
    }
}


@Composable
fun AlbumsItems(albums: MusicData) {
    Column(Modifier.fillMaxWidth()) {
        AsyncImage(
            albums.thumbnail, "",
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(6.dp))

        TextSemiBold(albums.name ?: "", Modifier.fillMaxWidth(), doCenter = true, size = 14)

        Spacer(Modifier.height(12.dp))
    }
}