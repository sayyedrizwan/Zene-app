package com.rizwansayyed.zene.presenter.ui.home.artists

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import kotlin.math.absoluteValue

@Composable
fun ArtistsImagesView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.artistsImages) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.artist_photos, null) {}

            ArtistPhotoAlbum(listOf("", "", "", "", "", "", "", "", "", ""), true)
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.artist_photos, null) {}

            ArtistPhotoAlbum(v.item, false)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistPhotoAlbum(item: List<String>, isLoading: Boolean) {
    val height = LocalConfiguration.current.screenHeightDp.dp / 2
    val width = (LocalConfiguration.current.screenWidthDp.absoluteValue / 1.3).toInt()
    val paddingReminder = (LocalConfiguration.current.screenWidthDp.absoluteValue - width) / 2
    val pagerState = rememberPagerState(pageCount = { item.size })


    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = paddingReminder.dp)
    ) { page ->
        Card({},
            Modifier
                .padding(top = if (page == pagerState.currentPage) 0.dp else 60.dp)
                .padding(7.dp)
                .size(width.dp, height)
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                }
        ) {
            AsyncImage(
                item[page],
                "",
                if (isLoading) Modifier
                    .background(shimmerBrush())
                    .fillMaxSize()
                else Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(item.size / 2)
    }
}

@Composable
fun ArtistsSocialMediaProfile() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    ArtistsProfileLoading()
    when (val v = artistsViewModel.artistSocialProfile) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> ArtistsProfileLoading()
        is DataResponse.Success -> {}
    }
}


@Composable
fun ArtistsProfileLoading() {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
        repeat(3) {
            Image(
                painterResource(R.drawable.ic_instagram), "",
                Modifier
                    .size(35.dp)
                    .background(shimmerBrush())
            )
        }
    }
}