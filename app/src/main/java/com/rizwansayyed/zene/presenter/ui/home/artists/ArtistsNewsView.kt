package com.rizwansayyed.zene.presenter.ui.home.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.getFavIcon
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.getMainDomain
import com.rizwansayyed.zene.domain.news.GoogleNewsResponse
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextRegularNews
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel

@Composable
fun ArtistsNews() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()

    when (val v = artistsViewModel.artistsNews) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(R.string.artist_news, null) {}
        }

        is DataResponse.Success -> {
            TopInfoWithSeeMore(R.string.artist_news, null) {}
            ArtistsNewsLists(v.item)
        }
    }
}

@Composable
fun ArtistsNewsLists(item: List<List<GoogleNewsResponse.Channel.Item>>) {
    val width = LocalConfiguration.current.screenWidthDp.dp - 40.dp

    LazyRow(Modifier.fillMaxWidth()) {
        items(item) {
            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .width(width)
            ) {
                it.forEach { n ->
                    ArtistsNewsItems(n)
                }
            }
        }
    }
}

@Composable
fun ArtistsNewsItems(n: GoogleNewsResponse.Channel.Item) {
    Row(
        Modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12))
            .background(MainColor),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        AsyncImage(
            getFavIcon(n.source?.url ?: ""), "",
            Modifier
                .padding(horizontal = 7.dp)
                .size(50.dp)
        )

        TextRegularNews(n.title ?: "")
    }
}