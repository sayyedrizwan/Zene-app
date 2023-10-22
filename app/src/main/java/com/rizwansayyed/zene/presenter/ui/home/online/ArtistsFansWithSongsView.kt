package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.ArtistsFanData
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlin.math.absoluteValue


@Composable
fun ArtistsYouMayLikeWithSongs() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.artistsFans) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.your_favourite_artists), null) {}

            ArtistsFanLoading()
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(stringResource(id = R.string.your_favourite_artists), null) {}

            ArtistsFanItems(v.item)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistsFanItems(item: List<ArtistsFanData>) {
    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })
    val w = (LocalConfiguration.current.screenWidthDp / 1.4).dp
    val l = ((LocalConfiguration.current.screenWidthDp - w.value) / 2).dp

    HorizontalPager(
        pagerState, contentPadding = PaddingValues(horizontal = l)
    ) { page ->
        val i = item[page % item.size]

        Column(
            Modifier
                .width(w)
                .padding(horizontal = 10.dp)
                .graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }, Arrangement.Center, Alignment.CenterHorizontally
        ) {
            AsyncImage(
                i.artistsImg, i.artistsName,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4)),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(17.dp))

            TextSemiBold(
                i.artistsName,
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                doCenter = true, size = if (page == pagerState.currentPage) 28 else 18
            )
        }
    }

    Spacer(Modifier.height(17.dp))

    PageWithHorizontal(item[pagerState.currentPage % item.size].list.chunked(3))

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(Int.MAX_VALUE / 2)
    }
}

@Composable
fun ArtistsFanLoading() {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(
            Modifier
                .padding(bottom = 20.dp)
                .size(LocalConfiguration.current.screenWidthDp.dp / 2)
                .background(shimmerBrush())
        )
    }
}