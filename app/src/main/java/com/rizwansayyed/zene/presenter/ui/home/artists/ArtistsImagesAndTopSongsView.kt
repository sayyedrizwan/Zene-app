package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import kotlin.math.absoluteValue

@Composable
fun ArtistsImagesView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.artistsImages) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.artists_photos, null) {}
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.artists_photos, null) {}

            ArtistPhotoAlbum(v.item)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArtistPhotoAlbum(item: List<String>) {
    val pagerState = rememberPagerState(pageCount = { item.size })
    HorizontalPager(state = pagerState) { page ->
        Card(
            Modifier
                .size(200.dp)
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            AsyncImage(item[page], "", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        }
    }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(item.size / 2)
    }
}