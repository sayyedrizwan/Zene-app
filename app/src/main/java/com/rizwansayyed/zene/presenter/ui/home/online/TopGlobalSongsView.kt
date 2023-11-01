package com.rizwansayyed.zene.presenter.ui.home.online


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopGlobalSongsList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    when (val v = homeViewModel.topGlobalTrendingSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            TopInfoWithImage(R.string.gt_trending_songs, null) {}

            PageWithHorizontal(v.item)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageWithHorizontal(item: List<List<MusicData?>>) {
    val pager = rememberPagerState(pageCount = { item.size })
    HorizontalPager(
        pager, contentPadding = PaddingValues(
            end = if ((pager.currentPage + 1) == pager.pageCount) 0.dp else 60.dp,
            start = if ((pager.currentPage + 1) == pager.pageCount) 60.dp else 0.dp
        )
    ) { page ->
        Column {
            item[page].forEach { i ->
                GlobalTrendingPagerItems(i, true) {
                    addAllPlayer(item.flatten().toTypedArray(), page)
                }
            }
        }
    }
}

@Composable
fun GlobalTrendingPagerItems(i: MusicData?, horizontal: Boolean, click: () -> Unit) {
    val homeNavViewModel: HomeNavViewModel = hiltViewModel()

    val gradient = Brush.linearGradient(
        colors = if (horizontal) listOf(BlackColor, BlackColor)
        else listOf(BlackColor, BlackColor, BlackColor, BlackColor, MainColor),
        start = Offset.Infinite, end = Offset.Zero
    )
    Row(
        Modifier
            .padding(
                vertical = if (horizontal) 3.dp else 8.dp,
                horizontal = if (horizontal) 2.dp else 7.dp
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
            .padding(5.dp)
            .clickable {
                click()
            },
        verticalAlignment = CenterVertically
    ) {
        AsyncImage(
            i?.thumbnail,
            "",
            Modifier
                .padding(vertical = 5.dp)
                .size(if (horizontal) 80.dp else 105.dp)
                .clip(RoundedCornerShape(17.dp)),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.weight(1f)) {
            TextSemiBold(
                i?.name ?: "",
                Modifier.padding(start = 8.dp), singleLine = true, size = if (horizontal) 15 else 19
            )

            TextThin(
                i?.artists ?: "",
                Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                singleLine = true,
                size = if (horizontal) 11 else 15
            )
        }

        MenuIcon {
            homeNavViewModel.setSongDetailsDialog(i)
        }
    }
}
