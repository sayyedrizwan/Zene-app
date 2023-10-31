package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.toMusicDataList
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtists
import com.rizwansayyed.zene.presenter.ui.TextLight
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.utils.Utils
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FreshAddedSongsList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()

    when (val v = homeViewModel.freshAddedSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.fresh_added_songs, null) {}

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenWidthDp.dp / 2)
                    .background(shimmerBrush())
            )
        }

        is DataResponse.Success -> {
            val pager = rememberPagerState(pageCount = { v.item.size })

            TopInfoWithSeeMore(R.string.fresh_added_songs, null) {}

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(BlackColor)
            ) {
                Spacer(Modifier.height(20.dp))

                HorizontalPager(state = pager) { page ->
                    v.item[page]?.let {
                        FreshAddedItems(it) {
                            addAllPlayer(v.item.toTypedArray(), page)
                        }
                    }
                }
                Spacer(Modifier.height(25.dp))

                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    for (i in 0 until pager.pageCount) {
                        Column(
                            Modifier
                                .animateContentSize()
                                .padding(bottom = 14.dp)
                                .padding(horizontal = 4.dp)
                                .size(if (pager.currentPage == i) 26.dp else 3.dp, 3.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(if (pager.currentPage == i) MainColor else Color.White)
                        ) {}
                    }
                }

                Spacer(Modifier.height(30.dp))
            }

        }
    }
}

@Composable
fun FreshAddedItems(music: MusicData, click: () -> Unit) {
    val homeNavViewModel: HomeNavViewModel = hiltViewModel()

    Row(
        Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth()
            .clickable {
                click()
            }
    ) {
        Column(
            Modifier
                .weight(1f)
                .height(LocalConfiguration.current.screenWidthDp.dp / 2)
        ) {
            TextSemiBold(
                music.name ?: "",
                Modifier.fillMaxWidth(),
                size = if ((music.name?.length ?: 0) > 11) 23 else 38
            )

            Spacer(Modifier.height(8.dp))

            TextLight(music.artists ?: "", Modifier.fillMaxWidth(), size = 12)

            Spacer(Modifier.weight(1f))

            MenuIcon {
                homeNavViewModel.setSongDetailsDialog(music)
            }
        }

        AsyncImage(
            music.thumbnail,
            music.name,
            Modifier.size(LocalConfiguration.current.screenWidthDp.dp / 2)
        )
    }
}
