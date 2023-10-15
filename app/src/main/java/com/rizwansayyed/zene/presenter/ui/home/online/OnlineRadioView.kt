package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.presenter.util.UiUtils.toCapitalFirst
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CityRadioViewList() {
    val homeApi: HomeApiViewModel = hiltViewModel()

    TopInfoWithSeeMore(R.string.radio_station_in_city, R.string.view_all) {
        "see all radio".toast()
    }

    LazyRow {
        items(3) {
            Row(
                Modifier
                    .padding(horizontal = 8.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(30))
                    .clip(RoundedCornerShape(30))
                    .background(Color.White),
                Arrangement.Center, Alignment.CenterVertically
            ) {
                AsyncImage(
                    "https://1.bp.blogspot.com/-zqFqeHIezoQ/XWwTN0Jl0OI/AAAAAAAAAck/JiBc3HIncUIIrenqBm_BaIfLAGLxCQF4ACLcBGAs/s1600/free%2Bfm%2Brock.jpg",
                    "",
                    Modifier
                        .padding(5.dp)
                        .size(35.dp)
                        .clip(RoundedCornerShape(100)),
                    contentScale = ContentScale.Crop
                )
                TextThin(
                    "Free FM Rock Bombay",
                    Modifier.padding(end = 12.dp),
                    color = Color.Black,
                    size = 14
                )
            }
        }
    }

    Spacer(Modifier.height(10.dp))

    when (val v = homeApi.onlineRadio) {
        DataResponse.Empty -> {}
        is DataResponse.Error ->
            TextThin(v.throwable.message ?: "", doCenter = true)

        DataResponse.Loading -> RadioItemLoading()

        is DataResponse.Success -> {
            val radioPagerState = rememberPagerState(pageCount = { v.item.size })

            HorizontalPager(
                radioPagerState,
                Modifier.fillMaxWidth(),
                PaddingValues(
                    end = if ((radioPagerState.currentPage + 1) == radioPagerState.pageCount) 0.dp else 90.dp,
                    start = if ((radioPagerState.currentPage + 1) == radioPagerState.pageCount) 90.dp else 0.dp
                ),
            ) { page ->
                RadiosItems(page, radioPagerState, v.item[page])
            }
        }
    }
}

@Composable
fun RadioItemLoading() {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        repeat(2) {
            Column(
                Modifier
                    .padding(horizontal = 12.dp)
                    .width((LocalConfiguration.current.screenWidthDp - 80).dp)
                    .height((LocalConfiguration.current.screenWidthDp).dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(shimmerBrush(targetValue = 2300f, showShimmer = true))
            ) {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadiosItems(
    page: Int, radioPagerState: PagerState, radio: OnlineRadioResponseItem
) {
    Card(
        Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenWidthDp).dp)
            .graphicsLayer {
                val pageOffset =
                    ((radioPagerState.currentPage - page) + radioPagerState.currentPageOffsetFraction).absoluteValue
                alpha = lerp(0.5f, 1f, 0.8f - pageOffset.coerceIn(0f, 1f))
            },
        RoundedCornerShape(12.dp), CardDefaults.cardColors(MainColor)
    ) {
        Box(
            Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {
            MenuIcon(Modifier.align(Alignment.TopStart)) {

            }

            Column(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
                AsyncImage(
                    radio.favicon,
                    radio.name,
                    Modifier
                        .size(95.dp)
                        .clip(RoundedCornerShape(50)),
                    contentScale = ContentScale.Crop
                )

                TextSemiBold(
                    radio.name ?: "",
                    Modifier
                        .padding(horizontal = 6.dp)
                        .padding(top = 15.dp),
                    size = 23
                )

                if (radio.tags?.trim()?.isNotEmpty() == true) Row(
                    Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    radio.tags.split(",").forEach {
                        TextThin(
                            it,
                            Modifier
                                .padding(horizontal = 7.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White)
                                .padding(5.dp),
                            color = Color.Black,
                            size = 13
                        )
                    }
                }
                if (radio.language?.trim()?.isNotEmpty() == true) TextThin(
                    radio.language.toCapitalFirst(),
                    Modifier.padding(horizontal = 7.dp, vertical = 12.dp),
                    size = 17
                )

            }
        }
    }
}
