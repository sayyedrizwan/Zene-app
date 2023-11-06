package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun TrendingSongsCountryList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val country by userIpDetails.collectAsState(initial = null)

    when (val v = homeViewModel.topCountryTrendingSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            if (country?.city != null)
                TopInfoWithSeeMore(
                    String.format(stringResource(id = R.string.top_songs_in_c), country?.country),
                    null
                ) {}

            repeat(6) {
                Spacer(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 7.dp)
                        .fillMaxWidth().height(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shimmerBrush())
                        .padding(5.dp)
                )
            }
        }

        is DataResponse.Success -> if (country?.city != null)
            TopInfoWithSeeMore(
                String.format(stringResource(id = R.string.top_songs_in_c), country?.country),
                null
            ) {}
    }
}