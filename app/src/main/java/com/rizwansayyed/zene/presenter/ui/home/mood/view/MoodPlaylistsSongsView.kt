package com.rizwansayyed.zene.presenter.ui.home.mood.view

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.MoodViewModel
import java.util.UUID

@Composable
fun MoodPlaylistsSongs(viewModel: MoodViewModel) {
    Column {
        Spacer(Modifier.height(20.dp))

        when (val v = viewModel.songMood) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> {}
            DataResponse.Loading -> {
                TopInfoWithSeeMore(stringResource(id = R.string.top_playlists), null) {}
                LazyRow(Modifier.fillMaxWidth()) {
                    items(20) {
                        MoodPlaylistLoadings()
                    }
                }
            }

            is DataResponse.Success -> {
                TopInfoWithSeeMore(stringResource(id = R.string.top_playlists), null) {}

                LazyRow(Modifier.fillMaxWidth()) {
                    itemsIndexed(v.item, key = { _, m -> m.songId ?: UUID.randomUUID() }) { i, m ->
                        MoodPlaylistImage(m) {
                            addAllPlayer(listOf(m).toTypedArray(), 0)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoodPlaylistLoadings() {
    val size = (LocalConfiguration.current.screenWidthDp / 1.8).dp

    Spacer(
        Modifier
            .padding(horizontal = 9.dp)
            .size(size)
            .background(shimmerBrush())
    )
}


@Composable
fun MoodPlaylistImage(m: MusicData, click: () -> Unit) {
    val size = (LocalConfiguration.current.screenWidthDp / 1.5).dp

    Column(
        Modifier
            .width(size)
            .padding(horizontal = 9.dp)
            .clickable { click() },
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        AsyncImage(
            m.thumbnail, m.name, Modifier
                .padding(5.dp)
                .size(size),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(10.dp))

        Row(
            Modifier
                .clip(RoundedCornerShape(13.dp))
                .background(MainColor)
                .padding(7.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(20.dp))

            SmallIcons(R.drawable.ic_play, 20, 0)
            Spacer(Modifier.width(5.dp))
            TextThin(v = stringResource(R.string.play))

            Spacer(Modifier.width(20.dp))
        }

        Spacer(Modifier.height(10.dp))
    }
}