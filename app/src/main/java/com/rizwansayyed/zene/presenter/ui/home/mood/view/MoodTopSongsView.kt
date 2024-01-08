package com.rizwansayyed.zene.presenter.ui.home.mood.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.online.ArtistsLoadingCards
import com.rizwansayyed.zene.presenter.ui.home.online.PageWithHorizontal
import com.rizwansayyed.zene.presenter.ui.home.online.TopArtistsItems
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.MoodViewModel

@Composable
fun MoodTopSongs(viewModel: MoodViewModel) {
    when (val v = viewModel.topSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.top_trending_songs_for_this_mood, null) {}

            Spacer(
                Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(shimmerBrush())
            )
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.top_trending_songs_for_this_mood, null) {}

            PageWithHorizontal(v.item)
        }
    }
}