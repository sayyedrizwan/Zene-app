package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun TopArtistsCountryList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    val screenWidth = LocalConfiguration.current.screenWidthDp

    val country by userIpDetails.collectAsState(initial = null)


    when (val v = homeViewModel.topCountryArtists) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(
                String.format(
                    stringResource(id = R.string.trending_artists_in_country),
                    country?.country
                ),
                null
            ) {}

            ArtistsLoadingCards(screenWidth)
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(
                String.format(
                    stringResource(id = R.string.trending_artists_in_country),
                    country?.country
                ),
                null
            ) {}


            LazyHorizontalGrid(
                GridCells.Fixed(2),
                Modifier
                    .fillMaxWidth()
                    .height((250 * 2).dp)
            ) {
                items(v.item ?: emptyList()) {
                    TopArtistsItems(it, screenWidth)
                }
            }
        }
    }
}