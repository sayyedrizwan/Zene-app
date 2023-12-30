package com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.view


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.MenuIcon
import com.rizwansayyed.zene.presenter.ui.RoundBorderButtonsView
import com.rizwansayyed.zene.presenter.ui.SearchEditTextView
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.PlaylistImportViewModel


@Composable
fun ImportPlaylistView(viewModel: PlaylistImportViewModel) {
    val width = LocalConfiguration.current.screenWidthDp / 1.3

    var addPlaylistData by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))

        AsyncImage(
            viewModel.selectedPlaylist?.thumbnail,
            viewModel.selectedPlaylist?.name,
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(width.dp)
        )

        Spacer(Modifier.height(10.dp))
        TextSemiBold(v = viewModel.selectedPlaylist?.name ?: "")
        viewModel.selectedPlaylist?.desc?.let {
            Spacer(Modifier.height(5.dp))
            TextThin(v = viewModel.selectedPlaylist?.desc ?: "")
        }
        Spacer(Modifier.height(25.dp))

        RoundBorderButtonsView(stringResource(id = R.string.save_as_playlist)) {
            addPlaylistData = true
        }

        Spacer(Modifier.height(25.dp))
    }

    if (addPlaylistData) AddedPlaylistDialog(viewModel.selectedPlaylist) {
        addPlaylistData = false
    }
}


@Composable
fun AddedPlaylistDialog(p: ImportPlaylistInfoData?, close: () -> Unit) {
    val height = LocalConfiguration.current.screenHeightDp / 1.5

    val text by remember { mutableStateOf(p?.name ?: "") }

    val placeholder = stringResource(id = R.string.enter_playlist_name)

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(height.dp),
            RoundedCornerShape(16.dp),
            CardDefaults.cardColors(MainColor)
        ) {
            Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                Spacer(Modifier.height(10.dp))
                TextSemiBold(
                    v = stringResource(R.string.are_you_sure_want_to_save_it_as_a_playlist),
                    doCenter = true, size = 18
                )
                Spacer(Modifier.height(25.dp))
                TextSemiBold(
                    v = stringResource(R.string.save_it_as_a_playlist_desc),
                    size = 14, doCenter = true
                )

                Spacer(Modifier.height(25.dp))

                Box(Modifier.padding(horizontal = 6.dp)) {
                    SearchEditTextView(placeholder, text, null, {

                    }, {

                    })
                }

                Spacer(Modifier.height(25.dp))

                Row {
                    RoundBorderButtonsView(stringResource(id = R.string.cancel), close)

                    RoundBorderButtonsView(stringResource(id = R.string.save_as_playlist)) {

                    }

                }

            }

        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistTrackList(
    track: ImportPlaylistTrackInfoData, i: Int, click: (isMenu: Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(vertical = 30.dp)
            .combinedClickable(onClick = { click(false) }, onLongClick = {})
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        TextSemiBold(v = "${i + 1}", Modifier.padding(horizontal = 8.dp), doCenter = true)
        Spacer(Modifier.height(3.dp))
        AsyncImage(
            track.thumbnail, "",
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(Modifier.height(3.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 6.dp)
        ) {
            TextSemiBold(v = track.songName ?: "", singleLine = true, size = 15)
            Spacer(Modifier.height(5.dp))
            TextThin(v = track.artistsName ?: "", singleLine = true, size = 14)
        }

        MenuIcon {
            click(true)
        }
    }
}