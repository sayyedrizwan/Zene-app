package com.rizwansayyed.zene.ui.settings.importplaylists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.importplaylists.model.TrackItemCSV
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ImportPlaylistViewModel

@Composable
fun ImportSongSyncView(viewModel: ImportPlaylistViewModel) {
    val noValidFile = stringResource(R.string.not_a_valid_csv_file)

    LazyColumn(Modifier.fillMaxSize()) {
        item { Spacer(Modifier.height(80.dp)) }

        if (viewModel.songList.isEmpty()) item {
            TextViewBold(stringResource(R.string.no_song_found_in_playlist), 16)
        }
        else viewModel.songList.entries.forEach { (title, tracks) ->
            item {
                Row(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .background(MainColor)
                        .padding(horizontal = 18.dp),
                    Arrangement.Center,
                    Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Spacer(Modifier.height(15.dp))
                        TextViewBold(title, 25)
                        TextViewNormal("${tracks.size} Tracks", 13)
                        Spacer(Modifier.height(15.dp))
                    }

                    Column(Modifier
                        .clickable { viewModel.setDialogTitleAndSong(title, tracks) }
                        .padding(horizontal = 5.dp)) {
                        ImageIcon(R.drawable.ic_layer_add, 24)
                    }
                }
            }

            items(
                if (viewModel.isShowingFullSongs.getOrDefault(
                        title, false
                    )
                ) tracks else tracks.take(5)
            ) {
                Column(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .background(MainColor)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    if (tracks.isEmpty()) {
                        TextViewNormal(stringResource(R.string.no_song_found_in_playlist), 18)
                    } else {
                        TextViewNormal(it.trackName, 16, line = 2)
                        TextViewLight(it.artistName, 14, line = 2)
                    }
                }
            }

            if (!viewModel.isShowingFullSongs.getOrDefault(title, false)) item {
                ImportSongItem(tracks) {
                    viewModel.isShowingFullSong(title)
                }
            }

            item { Spacer(Modifier.height(35.dp)) }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }

    LaunchedEffect(viewModel.selectedFile) {
        if (viewModel.selectedFile == null) noValidFile.toast()
    }
}

@Composable
fun ImportSongItem(tracks: List<TrackItemCSV>, click: () -> Unit) {
    if (tracks.size > 6) {
        val track = tracks[6]
        Box(Modifier.fillMaxWidth(), Alignment.Center) {
            Column(
                Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth()
                    .background(MainColor)
                    .padding(horizontal = 18.dp, vertical = 10.dp)
            ) {
                TextViewNormal(track.trackName, 16, line = 2)
                TextViewLight(track.artistName, 14, line = 2)
            }

            if (tracks.size > 6) Box(
                Modifier
                    .padding(horizontal = 6.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MainColor.copy(0.5f), MainColor, MainColor
                            )
                        )
                    ), Alignment.BottomCenter
            ) {
                ButtonWithBorder(R.string.show_all) {
                    click()
                }
            }
        }
    }
}
