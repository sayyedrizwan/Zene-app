package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun CityRadioViewList() {
    val homeApi: HomeApiViewModel = hiltViewModel()

    val state = rememberLazyListState()

    Column {
        Spacer(Modifier.height(80.dp))

        TopInfoWithSeeMore(R.string.radio_station_in_city, R.string.view_all) {
            "see all radio".toast()
        }
        when (val v = homeApi.onlineRadio) {
            DataResponse.Empty -> {}
            is DataResponse.Error ->
                TextThin(stringResource(R.string.error_loading_radio_station), doCenter = true)

            DataResponse.Loading -> LoadingStateBar()
            is DataResponse.Success -> LazyHorizontalGrid(
                GridCells.Fixed(2),
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 100.dp)
            ) {
                items(v.item) { OnlineRadioItem(it) }
            }
        }

    }
}

@Composable
fun OnlineRadioItem(radio: OnlineRadioResponseItem) {
    AsyncImage(radio.favicon, "", Modifier.size(30.dp))
}
