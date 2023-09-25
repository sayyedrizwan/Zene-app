package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun TopArtistsCountryList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    val country by userIpDetails.collectAsState(initial = null)

    TopInfoWithSeeMore(
        String.format(stringResource(id = R.string.trending_artists_in_country), country?.country),
        null
    ) {}

    LazyRow {
        items(homeViewModel.topCountryArtists) {
            TextBold(it, Modifier.padding(9.dp))
        }
    }
}