package com.rizwansayyed.zene.presenter.ui.home.online

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.OfflineSongsDetailsResult
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

@Composable
fun LocalSongsTop() {
    val offlineSongsViewModel: OfflineSongsViewModel = hiltViewModel()

    if (offlineSongsViewModel.top20Songs.isNotEmpty())
        Column(verticalArrangement = Arrangement.Center) {
            Spacer(Modifier.height(80.dp))

            TopInfoWithSeeMore(R.string.device_songs, R.string.switch_to_local_songs) {
            }

            LazyRow {
                items(offlineSongsViewModel.top20Songs) {
                    LocalSongsItems(it)
                }
            }
        }

    LaunchedEffect(Unit) {
        offlineSongsViewModel.latest20Songs()
    }
}

@Composable
fun LocalSongsItems(songs: OfflineSongsDetailsResult) {

}