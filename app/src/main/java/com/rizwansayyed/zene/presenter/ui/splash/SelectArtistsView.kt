package com.rizwansayyed.zene.presenter.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextLight
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel

@Composable
fun SelectArtistsCard(btn: (Boolean) -> Unit, close: () -> Unit) {
    val home: HomeApiViewModel = hiltViewModel()

    val selectArtists = remember { mutableStateListOf<String?>(null) }
    val wholeArtists = remember { mutableStateListOf<MusicData?>(null) }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(DarkGreyColor)
            .height((LocalConfiguration.current.screenHeightDp - 20).dp)
    ) {
        Row(
            Modifier
                .padding(top = 25.dp)
                .padding(horizontal = 6.dp)
                .fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
        ) {
            Column(
                Modifier
                    .padding(end = 9.dp)
                    .weight(1f)
            ) {
                TextBold(stringResource(R.string.select_artists), size = 30)

                Spacer(Modifier.height(5.dp))

                TextLight(stringResource(R.string.select_from_favourite).lowercase(), size = 14)
            }

            TextLight(stringResource(R.string.skip).lowercase(), size = 15)
        }


        when (val v = home.topArtistsSelect) {
            DataResponse.Empty -> {}
            is DataResponse.Error ->
                TextRegular(
                    stringResource(R.string.artists_lists_try_again_later).lowercase(),
                    Modifier.fillMaxWidth(),
                    doCenter = true,
                    size = 19
                )

            DataResponse.Loading -> LoadingStateBar()

            is DataResponse.Success -> {
                wholeArtists.addAll(v.item ?: emptyList())

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    Modifier
                        .padding(top = 7.dp)
                        .fillMaxWidth()
                ) {
                    item(span = { GridItemSpan(3) }) {
                        Spacer(Modifier.height(60.dp))
                    }

                    items(wholeArtists.toSet().toList()) {
                        if ((it?.artists ?: "").isNotEmpty())
                            SelectArtistsView(
                                it!!,
                                Modifier
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selectArtists.contains(it.artists ?: ""))
                                            MainColor else BlackColor
                                    )
                            ) { m ->
                                if (selectArtists.contains(m.artists))
                                    selectArtists.remove(m.artists)
                                else {
                                    selectArtists.add(m.artists)
                                    m.artists?.let { it1 -> home.topArtistsSearch(it1) }
                                }

                                btn(selectArtists.isNotEmpty())
                            }
                    }

                    item(span = { GridItemSpan(3) }) {
                        Spacer(Modifier.height(160.dp))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        home.topArtistsSelects()
    }
}

@Composable
fun SelectArtistsView(musicData: MusicData, modifier: Modifier, click: (MusicData) -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable {
                click(musicData)
            }) {
        Spacer(Modifier.height(20.dp))

        AsyncImage(
            musicData.thumbnail,
            "",
            Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
        )

        Spacer(Modifier.height(7.dp))

        TextRegular(
            musicData.name ?: "",
            Modifier.fillMaxWidth(),
            doCenter = true,
            size = 13,
            singleLine = true
        )

        Spacer(Modifier.height(20.dp))
    }
}