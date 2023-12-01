package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.yt.MerchandiseItems
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TextThinArtistsDesc
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.utils.Utils.customBrowser
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@Composable
fun MusicPlayerArtistsMerchandise(playerViewModel: PlayerViewModel) {
    when (val v = playerViewModel.artistsMerchandise) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {
            TopInfoWithSeeMore(stringResource(id = R.string.artist_merchandise), null) {}
            LazyRow(Modifier.fillMaxWidth()) {
                items(9) {
                    ArtistsMerchandiseLoading()
                }
            }
        }

        is DataResponse.Success -> if (v.item.isNotEmpty()) {
            TopInfoWithSeeMore(stringResource(id = R.string.artist_merchandise), null) {}
            LazyRow(Modifier.fillMaxWidth()) {
                items(v.item) {
                    ArtistsMerchandiseItems(it)
                }
            }
        }
    }
}

@Composable
fun ArtistsMerchandiseItems(merchandise: MerchandiseItems) {
    val width = (LocalConfiguration.current.screenWidthDp / 1.8).dp

    Column(
        Modifier
            .padding(6.dp)
            .width(width + 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                Uri
                    .parse(merchandise.link)
                    .customBrowser()
            },
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            merchandise.thumbnail, merchandise.title, Modifier.size(width)
        )
        Spacer(Modifier.height(5.dp))
        TextSemiBold(merchandise.title ?: "", singleLine = true)
        Spacer(Modifier.height(7.dp))
        TextThin(merchandise.price ?: "")

    }
}

@Composable
fun ArtistsMerchandiseLoading() {
    val width = (LocalConfiguration.current.screenWidthDp / 1.8).dp

    Spacer(
        Modifier
            .padding(6.dp)
            .size(width)
            .clip(RoundedCornerShape(12.dp))
            .background(shimmerBrush())
    )
}