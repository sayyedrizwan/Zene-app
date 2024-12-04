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
import com.rizwansayyed.zene.ui.view.ArtistsCardView
import com.rizwansayyed.zene.ui.view.CardRoundLoading
import com.rizwansayyed.zene.ui.view.CardRoundTextOnly
import com.rizwansayyed.zene.ui.view.CardSmallWithListeningNumber
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.LoadingArtistsCardView
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.LoadingNewsCardView
import com.rizwansayyed.zene.ui.view.NewsItemCard
import com.rizwansayyed.zene.ui.view.SimpleCardsView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.VideoCardsViewWithSong

enum class TextSize {
    BIG, MEDIUM, SMALL
}

enum class StyleSize {
    HIDE_AUTHOR, SHOW_AUTHOR, ONLY_TEXT, SONG_WITH_LISTENER
}

@Composable
fun TextTitleHeader(header: Pair<TextSize, Any>) {
    Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
        when (header.first) {
            TextSize.BIG ->
                TextPoppins(
                    if (header.second is Int) stringResource(header.second as Int) else header.second as String,
                    size = 30,
                    lineHeight = 35
                )

            TextSize.MEDIUM -> TextPoppins(
                if (header.second is Int) stringResource(header.second as Int) else header.second as String,
                size = 24
            )

            TextSize.SMALL -> TextPoppinsSemiBold(
                if (header.second is Int) stringResource(header.second as Int) else header.second as String,
                size = 15
            )
        }
    }
}

@Composable
fun HorizontalSongView(
    data: APIResponse<ZeneMusicDataResponse>,
    header: Pair<TextSize, Any>,
    cardStyle: StyleSize,
    showGrid: Boolean,
    isAPlaylists: Boolean
) {
    when (data) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            TextTitleHeader(header)

            if (showGrid) LazyHorizontalGrid(
                GridCells.Fixed(2),
                Modifier
                    .fillMaxWidth()
                    .height(if (cardStyle == StyleSize.ONLY_TEXT) 160.dp else 600.dp)
            ) {
                items(9) {
                    when (cardStyle) {
                        StyleSize.HIDE_AUTHOR, StyleSize.SHOW_AUTHOR -> LoadingCardView()
                        StyleSize.ONLY_TEXT -> LoadingCardView()
                        StyleSize.SONG_WITH_LISTENER -> CardRoundLoading()
                    }
                }
            }
            else LazyRow {
                items(9) {
                    when (cardStyle) {
                        StyleSize.HIDE_AUTHOR, StyleSize.SHOW_AUTHOR -> LoadingCardView()
                        StyleSize.ONLY_TEXT -> LoadingCardView()
                        StyleSize.SONG_WITH_LISTENER -> CardRoundLoading()
                    }
                }
            }

        }

        is APIResponse.Success -> {
            if (data.data.isNotEmpty()) {
                TextTitleHeader(header)

                if (showGrid) LazyHorizontalGrid(
                    GridCells.Fixed(2),
                    Modifier
                        .fillMaxWidth()
                        .height(if (cardStyle == StyleSize.ONLY_TEXT) 160.dp else if (cardStyle == StyleSize.SONG_WITH_LISTENER) 250.dp else 600.dp)
                ) {
                    items(data.data) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR -> SimpleCardsView(it, data.data)
                            StyleSize.SHOW_AUTHOR -> CardsViewDesc(
                                it, if (isAPlaylists) data.data else listOf(it)
                            )

                            StyleSize.ONLY_TEXT -> CardRoundTextOnly(it)
                            StyleSize.SONG_WITH_LISTENER ->
                                CardSmallWithListeningNumber(it, listOf(it))
                        }
                    }
                }
                else LazyRow {
                    items(data.data) {
                        when (cardStyle) {
                            StyleSize.HIDE_AUTHOR -> SimpleCardsView(it, data.data)
                            StyleSize.SHOW_AUTHOR -> CardsViewDesc(
                                it, if (isAPlaylists) data.data else listOf(it)
                            )

                            StyleSize.ONLY_TEXT -> CardRoundTextOnly(it)
                            StyleSize.SONG_WITH_LISTENER ->
                                CardSmallWithListeningNumber(it, listOf(it))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalVideoView(homeViewModel: APIResponse<ZeneMusicDataResponse>, txt: Int) {
    when (homeViewModel) {
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
            if (homeViewModel.data.isNotEmpty()) {
                Row(Modifier.padding(start = 5.dp, bottom = 7.dp)) {
                    TextPoppinsSemiBold(stringResource(txt), size = 15)
                }

                LazyRow {
                    items(homeViewModel.data) {
                        VideoCardsViewWithSong(it, homeViewModel.data)
                    }
                }
            }
        }
    }
}


@Composable
fun HorizontalArtistsView(
    data: APIResponse<ZeneMusicDataResponse>, header: Pair<TextSize, Int>, showGrid: Boolean
) {
    when (data) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            TextTitleHeader(header)

            if (showGrid) LazyHorizontalGrid(
                GridCells.Fixed(2),
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                items(10) {
                    LoadingArtistsCardView()
                }
            }
            else LazyRow {
                items(10) {
                    LoadingArtistsCardView()
                }
            }

        }

        is APIResponse.Success -> {
            if (data.data.isNotEmpty()) {
                TextTitleHeader(header)

                if (showGrid) LazyHorizontalGrid(
                    GridCells.Fixed(2),
                    Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                ) {
                    items(data.data) {
                        ArtistsCardView(it, data.data)
                    }
                }
                else LazyRow {
                    items(data.data) {
                        ArtistsCardView(it, data.data)
                    }
                }

            }
        }
    }
}


@Composable
fun HorizontalNewsView(
    data: APIResponse<ZeneMusicDataResponse>, header: Pair<TextSize, Int>, showGrid: Boolean
) {
    when (data) {
        APIResponse.Empty -> {}
        is APIResponse.Error -> {}
        APIResponse.Loading -> {
            TextTitleHeader(header)

            if (showGrid) LazyHorizontalGrid(
                GridCells.Fixed(2),
                Modifier
                    .fillMaxWidth()
                    .height(450.dp)
            ) {
                items(10) {
                    LoadingNewsCardView()
                }
            }
            else LazyRow {
                items(10) {
                    LoadingNewsCardView()
                }
            }

        }

        is APIResponse.Success -> {
            if (data.data.isNotEmpty()) {
                TextTitleHeader(header)

                if (showGrid) LazyHorizontalGrid(
                    GridCells.Fixed(2),
                    Modifier
                        .fillMaxWidth()
                        .height(680.dp)
                ) {
                    items(data.data) {
                        NewsItemCard(it)
                    }
                }
                else LazyRow {
                    items(data.data) {
                        NewsItemCard(it)
                    }
                }

            }
        }
    }

}