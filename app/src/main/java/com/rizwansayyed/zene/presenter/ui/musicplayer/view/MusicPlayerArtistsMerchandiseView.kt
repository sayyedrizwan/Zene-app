package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@Composable
fun MusicPlayerArtistsMerchandise(playerViewModel: PlayerViewModel) {
    when (val v = playerViewModel.artistsMerchandise) {
        DataResponse.Empty -> {}
        is DataResponse.Error -> {
            TextThin(v = v.throwable.message ?: "Error")
        }
        DataResponse.Loading -> {}
        is DataResponse.Success -> {
            LazyRow(Modifier.fillMaxWidth()) {
                items(v.item) {
                    AsyncImage(it.thumbnail, it.title, Modifier.size(80.dp))
                }
            }
        }
    }
}