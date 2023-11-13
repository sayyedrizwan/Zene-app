package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@Composable
fun ArtistsEvents() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    TopInfoWithSeeMore(R.string.upcoming_events, null) {}

    when (val v = artistsViewModel.artistsEvents) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> TextThin(v = "Loading")
        is DataResponse.Success -> LazyRow(Modifier.fillMaxWidth()) {
            items(v.item ?: emptyList()) {
                EventsItems()
            }
        }
    }
}

@Composable
fun EventsItems() {

}