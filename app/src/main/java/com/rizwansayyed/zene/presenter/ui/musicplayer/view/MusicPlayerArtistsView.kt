package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.viewmodel.PlayerViewModel


@Composable
fun MusicPlayerArtists() {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val width = (LocalConfiguration.current.screenWidthDp / 2.3).dp

    playerViewModel.artistsInfo.toList().forEach {
        Column(Modifier.size(width).background(BlackColor)) {

        }
    }
}