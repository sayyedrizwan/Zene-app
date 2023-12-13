package com.rizwansayyed.zene.presenter.ui.home.artists

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.Utils.tempEmptyList
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import java.io.File
import kotlin.math.absoluteValue


@Composable
fun ArtistsImagesView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    var isLoading by remember { mutableStateOf(false) }

    if (isLoading) {
        TopInfoWithSeeMore(R.string.artist_photos, null) {}

        ArtistPhotoAlbum(tempEmptyList, true)
    }

    if (artistsViewModel.artistsImages.distinct().size >= 2) {
        TopInfoWithSeeMore(R.string.artist_photos, null) {}

        ArtistPhotoAlbum(artistsViewModel.artistsImages.distinct(), false)
    }

    LaunchedEffect(artistsViewModel.artistsImages.toList()) {
        isLoading = try {
            artistsViewModel.artistsImages.first() == "loading"
        } catch (e: Exception) {
            false
        }

    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistPhotoAlbum(item: List<String>, isLoading: Boolean) {
    val homeNavViewModel: HomeNavViewModel = hiltViewModel()
    val height = LocalConfiguration.current.screenHeightDp.dp / 2
    val width = (LocalConfiguration.current.screenWidthDp.absoluteValue / 1.3).toInt()
    val paddingReminder = (LocalConfiguration.current.screenWidthDp.absoluteValue - width) / 2
    val pagerState = rememberPagerState(pageCount = { item.size })


    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = paddingReminder.dp)
    ) { page ->
        Card({ if (!isLoading) homeNavViewModel.setImageAsWallpaper(item[page]) },
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
