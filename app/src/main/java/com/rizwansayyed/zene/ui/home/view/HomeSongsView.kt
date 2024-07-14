package com.rizwansayyed.zene.ui.home.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.ui.view.CardRoundLoading
import com.rizwansayyed.zene.ui.view.CardRoundTextOnly
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.SimpleCardsView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.VideoCardsViewWithSong

enum class TextSize {
    BIG, MEDIUM, SMALL
}

enum class StyleSize {
    HIDE_AUTHOR, SHOW_AUTHOR, ONLY_TEXT,
}

@Composable
fun HorizontalSongView(
    data: APIResponse<ZeneMusicDataResponse>,
    header: Pair<TextSize, Int>,
    cardStyle: StyleSize,
    showGrid: Boolean
) {
    when (data) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                when (header.first) {
                    TextSize.BIG -> TextPoppins(stringResource(header.second), size = 30)
                    TextSize.MEDIUM -> TextPoppins(stringResource(header.second), size = 24)
                    TextSize.SMALL -> TextPoppinsSemiBold(stringResource(header.second), size = 15)
                }
            }

            if (showGrid)
                LazyHorizontalGrid(
                    GridCells.Fixed(2),
                    Modifier
                        .fillMaxWidth()
                        .height(if (cardStyle == StyleSize.ONLY_TEXT) 160.dp else 600.dp)
                ) {
                    items(9) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR, StyleSize.SHOW_AUTHOR -> LoadingCardView()
                            StyleSize.ONLY_TEXT -> LoadingCardView()
                        }
                    }
                }
            else
                LazyRow {
                    items(9) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR, StyleSize.SHOW_AUTHOR -> LoadingCardView()
                            StyleSize.ONLY_TEXT -> LoadingCardView()
                        }
                    }
                }

        }

        is APIResponse.Success -> {
            if (data.data.isNotEmpty()) {
                Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                    when (header.first) {
                        TextSize.BIG ->
                            TextPoppins(stringResource(header.second), size = 30)

                        TextSize.MEDIUM ->
                            TextPoppins(stringResource(header.second), size = 24)

                        TextSize.SMALL ->
                            TextPoppinsSemiBold(stringResource(header.second), size = 15)
                    }
                }

                if (showGrid) LazyHorizontalGrid(
                    GridCells.Fixed(2),
                    Modifier
                        .fillMaxWidth()
                        .height(if (cardStyle == StyleSize.ONLY_TEXT) 160.dp else 600.dp)
                ) {
                    items(data.data) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR -> SimpleCardsView(it)
                            StyleSize.SHOW_AUTHOR -> CardsViewDesc(it)
                            StyleSize.ONLY_TEXT -> CardRoundTextOnly(it)
                        }
                    }
                }
                else LazyRow {
                    items(data.data) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR -> SimpleCardsView(it)
                            StyleSize.SHOW_AUTHOR -> CardsViewDesc(it)
                            StyleSize.ONLY_TEXT -> CardRoundTextOnly(it)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalVideoView(homeViewModel: APIResponse<ZeneMusicDataResponse>, txt: Int) {
    when (val v = homeViewModel) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                TextPoppinsSemiBold(stringResource(txt), size = 15)
            }
            LazyRow {
                items(9) {
                    LoadingCardView()
                }
            }
        }

        is APIResponse.Success -> {
            if (v.data.isNotEmpty()) {
                Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                    TextPoppinsSemiBold(stringResource(txt), size = 15)
                }

                LazyRow {
                    items(v.data) {
                        VideoCardsViewWithSong(it)
                    }
                }
            }
        }
    }
}