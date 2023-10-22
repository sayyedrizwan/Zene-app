package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
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
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        Column(
            Modifier
                .width(w)
                .padding(horizontal = 10.dp)
                .graphicsLayer {
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

    ArtistsFanItemsSongs(item[pagerState.currentPage % item.size].list)

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(Int.MAX_VALUE / 2)
    }
}

@Composable
fun ArtistsFanItemsSongs(list: List<MusicData>) {
    val width = LocalConfiguration.current.screenWidthDp

    LazyRow(
        Modifier
            .padding(start = 14.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(MainColor)
    ) {
        items(list) {
            ArtistsFanItemsSongsItems(it, width)
        }
    }
}


@Composable
fun ArtistsFanItemsSongsItems(it: MusicData, width: Int) {
    Column(
        Modifier
            .padding(start = 10.dp, end = 15.dp)
            .width((width / 2 + 25).dp)
    ) {
        Spacer(Modifier.height(10.dp))

        Box(
            Modifier
                .clip(RoundedCornerShape(2))
                .size((width / 2).dp)
        ) {
            AsyncImage(
                it.thumbnail,
                "",
                Modifier
                    .clip(RoundedCornerShape(2))
                    .fillMaxSize()
            )

            Spacer(
                Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            listOf(Color.Transparent, Color.Transparent, MainColor),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
            )

            MenuIcon(
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(5.dp)) {

            }
        }

        Spacer(Modifier.height(7.dp))

        TextSemiBold(it.name ?: "", singleLine = true, size = 15)

        Spacer(Modifier.height(10.dp))
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