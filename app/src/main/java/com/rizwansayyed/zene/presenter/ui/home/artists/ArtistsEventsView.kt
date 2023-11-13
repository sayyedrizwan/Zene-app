package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore

@Composable
fun ArtistsEvents() {
    TopInfoWithSeeMore(R.string.upcoming_events, null) {}

    LazyRow(Modifier.fillMaxWidth()) {
        items(9) {
            EventsItems()
        }
    }
}

@Composable
fun EventsItems() {

}