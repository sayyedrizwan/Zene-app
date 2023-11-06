package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun SongsYouMayLikeView() {
    val roomDb: RoomDbViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    when (val v = roomDb.songsYouMayLike) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.songs_you_may_like), null) {}

            SongsYouMayLikeLoading(screenWidth)
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithSeeMore(stringResource(id = R.string.songs_you_may_like), null) {}

                LazyHorizontalGrid(
                    GridCells.Fixed(2), Modifier
                        .fillMaxWidth()
                        .height((screenWidth / 1.9 * 2).dp)
                ) {
                    itemsIndexed(v.item) { i, item ->
                        SongsYouMayLikeItems(item, screenWidth, homeNav) {
                            addAllPlayer(v.item.toTypedArray(), i)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SongsYouMayLikeItems(
    music: MusicData?, screenWidth: Int, homeNav: HomeNavViewModel, click: () -> Unit
) {
    Box(
        Modifier
            .padding(4.dp)
            .size((screenWidth / 2).dp, (screenWidth / 1.9).dp)
            .clip(RoundedCornerShape(18.dp))
            .background(BlackColor)
            .clickable {
                click()
            }
    ) {
        AsyncImage(
            music?.thumbnail, music?.name, Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Spacer(
            Modifier
                .fillMaxSize()
                .background(MainColor.copy(0.3f))
        )

        MenuIcon(
            Modifier
                .padding(5.dp)
                .align(Alignment.TopEnd)
        ) {
            homeNav.setSongDetailsDialog(music)
        }

        Column(
            Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            TextSemiBold(
                music?.name ?: "",
                Modifier.padding(start = 8.dp),
                singleLine = true, size = 15
            )

            TextThin(
                music?.artists ?: "",
                Modifier.padding(vertical = 3.dp, horizontal = 8.dp),
                singleLine = true, size = 11
            )

            Spacer(Modifier.padding(5.dp))
        }
    }
}

@Composable
fun SongsYouMayLikeLoading(screenWidth: Int) {
    LazyHorizontalGrid(
        GridCells.Fixed(2), Modifier
            .fillMaxWidth()
            .height((screenWidth / 1.5 * 2).dp)
    ) {
        items(40) {
            Spacer(
                Modifier
                    .padding(4.dp)
                    .size((screenWidth / 1.4).dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(shimmerBrush())
            )
        }
    }
}