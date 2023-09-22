package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.TopArtistsResult
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel


@Composable
fun TopArtistsList() {
    val jsoupViewModel: JsoupScrapViewModel = hiltViewModel()

    var showTop by remember { mutableStateOf(true) }

    Column {
        if (showTop) {
            Spacer(Modifier.height(80.dp))

            TopInfoWithSeeMore(R.string.global_top_artists, null) {}
        }

        when (val v = jsoupViewModel.topArtists) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> showTop = false
            DataResponse.Loading -> LoadingStateBar()
            is DataResponse.Success -> LazyHorizontalGrid(
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .height(430.dp)
            ) {
                items(v.item) { TopArtistsItems(it) }
            }
        }
    }
}

@Composable
fun TopArtistsItems(artists: TopArtistsResult) {
    AsyncImage(
        artists.thumbnail, artists.name,
        Modifier
            .size(170.dp, 290.dp)
            .clip(RoundedCornerShape(6.dp)),
        contentScale = ContentScale.Crop
    )
}