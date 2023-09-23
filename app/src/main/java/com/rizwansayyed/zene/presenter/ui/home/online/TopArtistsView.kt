package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.TopArtistsResult
import com.rizwansayyed.zene.presenter.theme.LightBlack
import com.rizwansayyed.zene.presenter.ui.LoadingStateBar
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.JsoupScrapViewModel


@Composable
fun TopArtistsList() {
    val jsoupViewModel: JsoupScrapViewModel = hiltViewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column {
        if (jsoupViewModel.showGlobalArtistInfo) {
            Spacer(Modifier.height(80.dp))

            TopInfoWithSeeMore(R.string.global_top_trending_artists, null) {}
        }

        when (val v = jsoupViewModel.topArtists) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading -> LoadingStateBar()
            is DataResponse.Success -> LazyHorizontalGrid(
                GridCells.Fixed(3), Modifier
                    .fillMaxWidth()
                    .height((screenWidth / 1.8 * 3).dp)
            ) {
                items(v.item) { TopArtistsItems(it, screenWidth) }
            }
        }
    }
}

@Composable
fun TopArtistsItems(artists: TopArtistsResult, width: Int) {
    Box(
        Modifier
            .padding(4.dp)
            .size((width / 2).dp, (width / 2 + 90).dp)
    ) {
        AsyncImage(
            artists.thumbnail, artists.name,
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent, Color.Transparent, Color.Black
                        )
                    )
                )
        )


        TextSemiBold(artists.name, Modifier.padding(8.dp).align(Alignment.BottomStart))

    }
}