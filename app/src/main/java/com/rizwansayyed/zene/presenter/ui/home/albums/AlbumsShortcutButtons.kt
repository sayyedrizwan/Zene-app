package com.rizwansayyed.zene.presenter.ui.home.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.AppUrl.appUrlArtistsShare
import com.rizwansayyed.zene.utils.Utils.shareTxt
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import com.rizwansayyed.zene.viewmodel.PlaylistAlbumViewModel

@Composable
fun AlbumsShortcutButton() {
    val playlistAlbum: PlaylistAlbumViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    when (val v = playlistAlbum.playlistAlbum) {
        DataResponse.Loading -> {}
        is DataResponse.Success -> Row(
            Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            AlbumsButtonsView(stringResource(id = R.string.share)) {
                shareTxt(appUrlArtistsShare(homeNav.selectedAlbum))
            }
            AlbumsButtonsView("Save Playlist") {}
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
            .border(1.dp, Color.Gray, RoundedCornerShape(100))
            .padding(vertical = 9.dp, horizontal = 14.dp)
            .clickable {
                click()
            }
    ) {
        TextThin(text)
    }
}