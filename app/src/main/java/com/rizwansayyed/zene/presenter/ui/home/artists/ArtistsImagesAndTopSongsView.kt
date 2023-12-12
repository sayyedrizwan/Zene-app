package com.rizwansayyed.zene.presenter.ui.home.artists

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.utils.Utils.tempEmptyList
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import kotlin.math.absoluteValue

@Composable
fun ArtistsImagesView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    if (artistsViewModel.artistsImages.distinct().isNotEmpty()) {
        TopInfoWithSeeMore(R.string.artist_photos, null) {}

        ArtistPhotoAlbum(artistsViewModel.artistsImages.distinct(), false)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ArtistPhotoAlbum(item: List<String>, isLoading: Boolean) {
    val height = LocalConfiguration.current.screenHeightDp.dp / 2
    val width = (LocalConfiguration.current.screenWidthDp.absoluteValue / 1.3).toInt()
    val paddingReminder = (LocalConfiguration.current.screenWidthDp.absoluteValue - width) / 2
    val pagerState = rememberPagerState(pageCount = { item.size })
    var showDialog by remember { mutableStateOf<String?>(null) }


    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = paddingReminder.dp)
    ) { page ->
        Card({ showDialog = item[page] },
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

    showDialog?.let { ImageActionDialog(it) { showDialog = null } }

    LaunchedEffect(Unit) {
        pagerState.scrollToPage(item.size / 2)
    }
}

@Composable
fun ImageActionDialog(s: String, close: () -> Unit) {
    val width = LocalConfiguration.current.screenHeightDp / 1.4

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(Modifier.fillMaxSize(), RoundedCornerShape(16.dp), Color.Black) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(BlackColor),
                Arrangement.Center, Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    s, "",
                    Modifier
                        .fillMaxWidth()
                        .height(width.dp)
                )
            }
        }
    }
}
