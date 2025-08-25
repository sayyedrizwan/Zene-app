package com.rizwansayyed.zene.ui.settings.importplaylists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponseItem
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.MiniWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextAlertDialog
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ImportPlaylistViewModel
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel

@Composable
fun ImportPlaylistDialogView(viewModel: ImportPlaylistViewModel) {
    Dialog(
        {
            viewModel.setDialogTitleAndSong(null, emptyList())
        }, DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        var showLikedDialog by remember { mutableStateOf(false) }
        var showCurrentPlaylistDialog by remember { mutableStateOf(false) }
        var showNewPlaylistDialog by remember { mutableStateOf(false) }

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MainColor)
                .padding(horizontal = 7.dp, vertical = 20.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            TextViewBold(stringResource(R.string.import_playlists), 19)
            Spacer(Modifier.height(20.dp))
            TextViewNormal(stringResource(R.string.import_playlists_desc), 15, center = true)
            Spacer(Modifier.height(20.dp))

            if (viewModel.isSyncing) {
                CircularLoadingView()
            } else {
                MiniWithImageAndBorder(R.drawable.ic_thumbs_up, R.string.import_to_liked_songs) {
                    showLikedDialog = true
                }
                Spacer(Modifier.height(20.dp))
                MiniWithImageAndBorder(
                    R.drawable.ic_playlist, R.string.import_to_current_playlist
                ) {
                    showCurrentPlaylistDialog = true
                }
                Spacer(Modifier.height(20.dp))
                MiniWithImageAndBorder(R.drawable.ic_layer_add, R.string.import_as_new_playlist) {
                    showNewPlaylistDialog = true
                }
                Spacer(Modifier.height(20.dp))
                MiniWithImageAndBorder(R.drawable.ic_cancel_close, R.string.close) {
                    viewModel.setDialogTitleAndSong(null, emptyList())
                }
            }

            Spacer(Modifier.height(20.dp))

            if (showLikedDialog) TextAlertDialog(
                R.string.are_you_sure,
                R.string.are_you_sure_import_to_liked_songs,
                {
                    showLikedDialog = false
                },
                {
                    viewModel.syncLikedImport()
                    showLikedDialog = false
                })

            if (showNewPlaylistDialog) TextAlertDialog(
                R.string.are_you_sure,
                R.string.are_you_sure_import_to_new_playlist,
                {
                    showNewPlaylistDialog = false
                },
                {
                    showNewPlaylistDialog = false
                    viewModel.importToCurrentNamePlaylist()
                })


            if (showCurrentPlaylistDialog) ShowAllPlaylistsToSelect {
                showCurrentPlaylistDialog = false
                viewModel.importToPlaylist(it)
            }
        }
    }
}

@Composable
fun ShowAllPlaylistsToSelect(close: (String?) -> Unit) {
    Dialog({ close(null) }, DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel: MyLibraryViewModel = hiltViewModel()

        var confirmAlert by remember { mutableStateOf(false) }
        var selectedID by remember { mutableStateOf("") }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            item { Spacer(Modifier.height(20.dp)) }

            when (val v = viewModel.userAllPlaylist) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> item { CircularLoadingView() }
                is ResponseResult.Success -> items(v.data) {
                    ImportPlaylistItem(it) {
                        selectedID = it.id!!
                        confirmAlert = true
                    }
                }
            }

            item { Spacer(Modifier.height(120.dp)) }
            item { Spacer(Modifier.height(20.dp)) }
        }

        LaunchedEffect(Unit) {
            viewModel.myAllPlaylistsList()
        }

        if (confirmAlert) TextAlertDialog(
            R.string.are_you_sure,
            R.string.are_you_sure_import_all_song_to_playlist,
            {
                confirmAlert = false
            }) {
            close(selectedID)
            confirmAlert = false
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImportPlaylistItem(item: SavedPlaylistsPodcastsResponseItem, click: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        GlideImage(
            item.img, item.name,
            Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 9.dp)
        ) {
            TextViewBold(item.name ?: "", 15)
        }

        ImageIcon(R.drawable.ic_arrow_right, 24)
    }
}