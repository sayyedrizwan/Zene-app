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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun SongsForYouToExplore() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.songsSuggestionForUsers) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.song_for_you_to_explore), null) {}
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(stringResource(id = R.string.song_for_you_to_explore), null) {}
        }
    }
}

@Composable
fun SongsExploreItems(music: MusicData, click: () -> Unit) {
    val width = (LocalConfiguration.current.screenWidthDp / 2).dp
    val homeNav: HomeNavViewModel = hiltViewModel()

    Column(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .padding(5.dp)
                .clip(RoundedCornerShape(2))
                .fillMaxWidth()
                .clickable {
                    click()
                }
        ) {
            AsyncImage(
                music.thumbnail,
                music.name,
                Modifier
                    .clip(RoundedCornerShape(2))
                    .fillMaxWidth()
            )

            Spacer(
                Modifier
                    .size(width)
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
                    .padding(5.dp)
            ) {
                homeNav.setSongDetailsDialog(music)
            }
        }

        TextSemiBold(
            music.name ?: "",
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxWidth(),
            singleLine = true,
            size = 15
        )

        Spacer(Modifier.height(7.dp))

        TextThin(
            music.artists ?: "",
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxWidth(),
            singleLine = true,
            size = 13
        )

        Spacer(Modifier.height(10.dp))
    }
}