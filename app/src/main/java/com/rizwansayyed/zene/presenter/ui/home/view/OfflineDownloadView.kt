package com.rizwansayyed.zene.presenter.ui.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.presenter.ui.TopInfoWithSeeMore
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

@Composable
fun OfflineDownloadHeader() {
    val offlineSongsViewModel: OfflineSongsViewModel = hiltViewModel()
    val list by offlineSongsViewModel.offlineDownloadedSongs.collectAsState(emptyList())

    if (list.isNotEmpty()) Column(verticalArrangement = Arrangement.Center) {
        Spacer(Modifier.height(80.dp))

        if (list.isNotEmpty())
            TopInfoWithSeeMore(R.string.offline_downloaded_songs, null) {}


    }
}