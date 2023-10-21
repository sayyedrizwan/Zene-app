package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel

@Composable
fun SongsSuggestionsForYou() {
    val roomDbViewModel: RoomDbViewModel = hiltViewModel()

    when (val v = roomDbViewModel.songsSuggestionForUsersTop) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.songs_you_may_like_to_listen), null) {}

            SongsYouMayLikeLoading()
        }

        is DataResponse.Success -> {
            if (v.item.isNotEmpty()) {
                TopInfoWithSeeMore(
                    stringResource(id = R.string.songs_you_may_like_to_listen), null
                ) {}

                SongsYouMayLikeList(v.item)
            }
        }
    }
}

@Composable
fun SongsYouMayLikeList(item: List<List<MusicData>>) {
    val width = LocalConfiguration.current.screenWidthDp

    item.forEach { i ->
        LazyRow(Modifier.fillMaxWidth()) {
            items(i) {
                Column(
                    Modifier
                        .padding(start = 10.dp, end = 20.dp, bottom = 20.dp)
                        .width((width / 3 + 25).dp)
                ) {

                    Box(
                        Modifier
                            .clip(RoundedCornerShape(2))
                            .size((width / 3 + 20).dp)
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

                        MenuIcon(Modifier.align(Alignment.TopEnd).padding(5.dp)) {

                        }
                    }

                    Spacer(Modifier.height(7.dp))

                    TextSemiBold(it.name ?: "", singleLine = true, size = 15)

                    Spacer(Modifier.height(7.dp))

                    TextThin(it.artists ?: "", singleLine = true, size = 13)

                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun SongsYouMayLikeLoading() {
    val width = LocalConfiguration.current.screenWidthDp
    repeat(5) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            repeat(3) {
                Spacer(
                    Modifier
                        .padding(6.dp)
                        .size((width / 3 + 20).dp)
                        .clip(RoundedCornerShape(2))
                        .background(shimmerBrush())
                )
            }
        }
    }
}