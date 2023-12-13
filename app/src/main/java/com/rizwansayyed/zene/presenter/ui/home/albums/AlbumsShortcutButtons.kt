package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlArtistsShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel

@Composable
fun AlbumsShortcutButton() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()
    val isAlbumPresent by playlistAlbum.isAlbumPresent.collectAsState(initial = 0)

    when (val v = playlistAlbum.playlistAlbum) {
        DataResponse.Loading -> {}
        is DataResponse.Success -> Row(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            AlbumsButtonsView(stringResource(id = R.string.share)) {
                shareTxt(appUrlArtistsShare(homeNav.selectedAlbum))
            }
            AlbumsButtonsView(stringResource(id = if (isAlbumPresent > 0) R.string.remove_from_saved_List else R.string.save_album)) {
                playlistAlbum.saveAlbumsLocally(v.item, homeNav.selectedAlbum, isAlbumPresent > 0)
            }
            AlbumsButtonsView("Download") {}
        }

        else -> {}
    }
}

@Composable
fun AlbumsButtonsView(text: String, click: () -> Unit) {
    Row(
        Modifier
            .padding(5.dp)
            .clickable {
                click()
            }
            .border(1.dp, Color.Gray, RoundedCornerShape(100))
            .padding(vertical = 9.dp, horizontal = 14.dp)
    ) {
        TextThin(text)
    }
}