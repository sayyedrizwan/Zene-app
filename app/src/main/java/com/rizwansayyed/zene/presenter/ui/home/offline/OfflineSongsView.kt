package com.rizwansayyed.zene.presenter.ui.home.offline

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

@Composable
fun OfflineSongsView() {
    val offlineSongViewModel : OfflineSongsViewModel = hiltViewModel()
}