package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@Composable
fun MusicPlayerSimilarView(viewModel: PlayerViewModel) {
    when (val v = viewModel.similarSongs) {
        ResponseResult.Empty -> {}
        is ResponseResult.Error -> {}
        ResponseResult.Loading -> CircularLoadingView()
        is ResponseResult.Success -> {
            if (v.data.songs?.isNotEmpty() == true) {
                Spacer(Modifier.height(50.dp))
                Box(Modifier.padding(horizontal = 6.dp)) {
                    TextViewBold(stringResource(R.string.similar_songs), 23)
                }
                Spacer(Modifier.height(12.dp))
                v.data.songs.chunked(7).forEach { m ->
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(m) { ItemCardView(it) }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }

            if (v.data.albums?.isNotEmpty() == true) {
                Spacer(Modifier.height(50.dp))
                Box(Modifier.padding(horizontal = 6.dp)) {
                    TextViewBold(stringResource(R.string.similar_albums), 23)
                }
                Spacer(Modifier.height(12.dp))
                LazyRow(Modifier.fillMaxWidth()) {
                    items(v.data.albums) { ItemCardView(it) }
                }
            }

            if (v.data.playlists?.isNotEmpty() == true) {
                Spacer(Modifier.height(50.dp))
                Box(Modifier.padding(horizontal = 6.dp)) {
                    TextViewBold(stringResource(R.string.similar_playlists), 23)
                }
                Spacer(Modifier.height(12.dp))
                LazyRow(Modifier.fillMaxWidth()) {
                    items(v.data.playlists) { ItemCardView(it) }
                }
            }
        }
    }
}