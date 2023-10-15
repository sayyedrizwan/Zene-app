package com.rizwansayyed.zene.presenter.ui.home.online


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

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

            val pager = rememberPagerState(pageCount = { v.item.size })
            HorizontalPager(
                pager, contentPadding = PaddingValues(
                    end = if ((pager.currentPage + 1) == pager.pageCount) 0.dp else 60.dp,
                    start = if ((pager.currentPage + 1) == pager.pageCount) 60.dp else 0.dp
                )
            ) { page ->
                Column {
                    v.item[page].forEach { i ->
                        GlobalTrendingPagerItems(i)
                    }
                }
            }
        }
    }
}

@Composable
fun GlobalTrendingPagerItems(i: MusicData?) {
    Row(
        Modifier
            .padding(vertical = 3.dp, horizontal = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BlackColor)
            .padding(5.dp),
        verticalAlignment = CenterVertically
    ) {
        AsyncImage(
            i?.thumbnail,
            "",
            Modifier
                .padding(vertical = 5.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(17.dp)),
            contentScale = ContentScale.Crop
        )

        Column(Modifier.weight(1f)) {
            TextSemiBold(
                i?.name ?: "",
                Modifier.padding(start = 8.dp), singleLine = true, size = 15
            )

            TextThin(
                i?.artists ?: "",
                Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                singleLine = true,
                size = 11
            )
        }

        MenuIcon {

        }
    }
}

@Composable
fun GlobalSongsItemsImg(items: MusicData?) {
    AsyncImage(
        items?.thumbnail,
        "",
        Modifier.size(170.dp),
        contentScale = ContentScale.Crop
    )
}