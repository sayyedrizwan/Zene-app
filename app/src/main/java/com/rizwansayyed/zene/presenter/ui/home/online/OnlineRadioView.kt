package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.generateRadioName
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun CityRadioViewList() {
    val homeApi: HomeApiViewModel = hiltViewModel()

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
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .height(430.dp)
            ) {
                items(v.item) { OnlineRadioItem(it) }
            }
        }

    }
}

@Composable
fun OnlineRadioItem(radio: OnlineRadioResponseItem) {
    Box(Modifier.padding(5.dp)) {
        if (radio.favicon?.isEmpty() == true)
            Image(
                painterResource(id = R.drawable.ic_radio), "",
                Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .size(170.dp, 290.dp)
                    .padding(65.dp),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        else
            AsyncImage(
                radio.favicon, "",
                Modifier
                    .align(Alignment.Center)
                    .size(170.dp, 290.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

        Spacer(
            Modifier
                .size(170.dp, 290.dp)
                .alpha(0.8f)
                .align(Alignment.Center)
                .background(LightBlack)
        )

        Column(Modifier.size(170.dp, 290.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            TextSemiBold(radio.name ?: generateRadioName(), doCenter = true)
        }
    }
}
