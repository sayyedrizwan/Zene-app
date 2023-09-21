package com.rizwansayyed.zene.presenter.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.theme.Pink40
import com.rizwansayyed.zene.presenter.ui.SongsTitleAndArtists
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.ui.home.offline.albums
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

@Composable
fun OfflineDownloadHeader() {
    val offlineSongsViewModel: OfflineSongsViewModel = hiltViewModel()
    val list by offlineSongsViewModel.offlineDownloadedSongs.collectAsState(emptyList())

//    if (list.isNotEmpty())
    Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

//        if (list.isNotEmpty())
        TopInfoWithSeeMore(R.string.offline_downloaded_songs, null) {}

        LazyRow(Modifier.fillMaxWidth()) {
            items(8) {
                OfflineDownloadedSongsItem()
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineDownloadedSongsItem() {
    Card(
        onClick = { },
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(200.dp)
            .height(300.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(Pink40)
    ) {
        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            AsyncImage(
                albums, "",
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(50))
            )

            SongsTitleAndArtists("Willow", "Taylor Swift", Modifier, true)
        }
    }
}