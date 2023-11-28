package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlaylistDialog(v: MusicPlayerList, close: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), sheetState,
        containerColor = MainColor, contentColor = BlackColor
    ) {
        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
        ) {
            MusicPlaylistSheetView()
        }
    }
}

@Composable
fun MusicPlaylistSheetView() {
    val playerViewModel: PlayerViewModel = hiltViewModel()

    Spacer(Modifier.height(40.dp))
    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        TextSemiBold(stringResource(R.string.playlists), Modifier.weight(1f), size = 36)

        SmallIcons(R.drawable.ic_layer_add, 26) {

        }
    }
    Spacer(Modifier.height(40.dp))
}