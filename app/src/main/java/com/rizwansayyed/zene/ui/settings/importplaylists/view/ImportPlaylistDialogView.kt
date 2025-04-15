package com.rizwansayyed.zene.ui.settings.importplaylists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.MiniWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ImportPlaylistViewModel

@Composable
fun ImportPlaylistDialogView(viewModel: ImportPlaylistViewModel) {
    Dialog({
        viewModel.setDialogTitleAndSong(null, emptyList())
    }, DialogProperties(usePlatformDefaultWidth = false)) {

        var showLikedDialog by remember { mutableStateOf(false) }
        var showNewPlaylistDialog by remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MainColor)
                .padding(horizontal = 7.dp, vertical = 20.dp),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            TextViewBold(stringResource(R.string.import_playlists), 19)
            Spacer(Modifier.height(20.dp))
            TextViewNormal(stringResource(R.string.import_playlists_desc), 15, center = true)
            Spacer(Modifier.height(20.dp))

            MiniWithImageAndBorder(R.drawable.ic_thumbs_up, R.string.import_to_liked_songs) {
                showLikedDialog = true
            }
            Spacer(Modifier.height(20.dp))
            MiniWithImageAndBorder(R.drawable.ic_playlist, R.string.import_to_current_playlist) {

            }
            Spacer(Modifier.height(20.dp))
            MiniWithImageAndBorder(R.drawable.ic_layer_add, R.string.import_as_new_playlist) {
                showNewPlaylistDialog = true
            }

            Spacer(Modifier.height(20.dp))

            if (showLikedDialog) TextAlertDialog(
                R.string.are_you_sure, R.string.are_you_sure_import_to_liked_songs,
                {
                    showLikedDialog = false
                }, {
                    showLikedDialog = false
                })

            if (showNewPlaylistDialog) TextAlertDialog(
                R.string.are_you_sure, R.string.are_you_sure_import_to_new_playlist,
                {
                    showNewPlaylistDialog = false
                }, {
                    showNewPlaylistDialog = false
                })
        }
    }
}