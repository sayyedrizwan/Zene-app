package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.globalSongIsFull
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtistsSmall
import com.rizwansayyed.zene.presenter.ui.TopInfoWithImage
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlinx.coroutines.flow.flowOf

@Composable
fun TopGlobalSongsList() {
    val homeViewModel: HomeApiViewModel = hiltViewModel()
    val fullView by globalSongIsFull.collectAsState(false)

    when (val v = homeViewModel.topGlobalTrendingSongs) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {}
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            if (fullView)
                TopInfoWithImage(R.string.gt_trending_songs, R.drawable.ic_layout_bottom) {
                    globalSongIsFull = flowOf(false)
                }
            else
                TopInfoWithImage(R.string.gt_trending_songs, R.drawable.ic_border_all) {
                    globalSongIsFull = flowOf(true)
                }

            LazyHorizontalGrid(
                GridCells.Fixed(2), Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .height(((if (fullView) 245 else 170) * 2).dp)
            ) {
                items(v.item) {
                    AnimatedContent(
                        fullView, Modifier, {
                            fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith fadeOut(
                                animationSpec = tween(durationMillis = 500)
                            )
                        }, label = ""
                    ) { targetState ->
                        if (targetState) GlobalSongsItemsFull(it)
                        else GlobalSongsItemsImg(it)
                    }
                }
            }
        }
    }
}


@Composable
fun GlobalSongsItemsFull(items: MusicData?) {
    Column(
        Modifier
            .size(180.dp)
            .padding(4.dp)
            .padding(horizontal = 4.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            items?.thumbnail, "",
            Modifier
                .size(170.dp)
                .clip(RoundedCornerShape(17.dp)), contentScale = ContentScale.Crop
        )

        SongsTitleAndArtistsSmall(
            items?.name ?: "",
            items?.artists ?: "",
            Modifier
                .width(170.dp)
                .padding(3.dp),
            true
        )
    }

}

@Composable
fun GlobalSongsItemsImg(items: MusicData?) {
    AsyncImage(
        items?.thumbnail,
        "",
        Modifier.size(170.dp),
        contentScale = ContentScale.Crop
    )
}