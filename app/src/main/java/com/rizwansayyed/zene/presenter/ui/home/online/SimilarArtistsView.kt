package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel


@Composable
fun SimilarArtists() {
    val homeNav: HomeNavViewModel = hiltViewModel()
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.artistsSuggestionForUsers) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.similar_artists), null) {}

            LazyRow(Modifier.fillMaxWidth()) {
                items(15) {
                    SimilarArtistsItemsLoading()
                }
            }
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithSeeMore(stringResource(id = R.string.similar_artists), null) {}

                LazyRow(Modifier.fillMaxWidth()) {
                    items(v.item) {
                        SimilarArtistsItems(it) {
                            homeNav.setArtists(it.name ?: "")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SimilarArtistsItemsLoading() {
    Column(Modifier.width(200.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(
            Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(100))
                .background(shimmerBrush())
        )

        Spacer(Modifier.height(9.dp))

        Spacer(
            Modifier
                .size(60.dp, 9.dp)
                .background(shimmerBrush())
        )

        Spacer(Modifier.height(9.dp))
    }
}

@Composable
fun SimilarArtistsItems(music: MusicData, click: () -> Unit) {
    Column(
        Modifier
            .width(200.dp)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            music.thumbnail, "",
            Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(100))
        )

        Spacer(Modifier.height(9.dp))

        TextRegular(music.name ?: "", doCenter = true, size = 14)

        Spacer(Modifier.height(9.dp))
    }
}