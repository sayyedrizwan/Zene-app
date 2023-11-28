package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TransparentEditTextView
import com.rizwansayyed.zene.presenter.ui.dashedBorder
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.THREE_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.PlayerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlaylistDialog(v: MusicPlayerList, close: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), sheetState,
        containerColor = MainColor, contentColor = BlackColor
    ) {

        MusicPlaylistSheetView()
    }
}


@Composable
fun MusicPlaylistSheetView() {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    val listState = rememberLazyGridState()
    val list = playerViewModel.playlistLists.collectAsLazyPagingItems()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier.padding(horizontal = 5.dp), listState
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(40.dp))
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                TextSemiBold(stringResource(R.string.playlists), Modifier.weight(1f), size = 36)

                SmallIcons(R.drawable.ic_layer_add, 26, 0) {
                }
                Spacer(Modifier.width(5.dp))
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(40.dp))
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            AddPlaylistItems(playerViewModel)
        }

        if (list.itemCount == 0)
            item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                NoPlaylistFound()
            }
        else
            items(list.itemSnapshotList, span = { GridItemSpan(THREE_ITEMS_GRID) }) {
                TextSemiBold("${it?.id} ---- ${it?.name}")
            }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(80.dp))
        }
    }

    LaunchedEffect(listState.isScrollInProgress){
        val layoutInfo = listState.layoutInfo
        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        val lastVisibleItemIndex = visibleItemsInfo.lastOrNull()?.index ?: -1

        if (lastVisibleItemIndex + visibleItemsInfo.size >= list.itemCount) {
           "user is going to reach".toast()
        }

        if (!listState.canScrollForward) "at end".toast()
    }
}

@Composable
fun NoPlaylistFound() {
    Column {
        Spacer(Modifier.height(100.dp))

        TextSemiBold(
            stringResource(R.string.no_playlist_found), Modifier.fillMaxWidth(), true
        )

        Spacer(Modifier.height(100.dp))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddPlaylistItems(playerViewModel: PlayerViewModel) {
    val p = stringResource(R.string.playlist_title)
    val validName = stringResource(R.string.enter_a_valid_playlist_name)
    var text by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current

    val error = stringResource(R.string.error_creating_playlist)
    val nameAlreadyUsed = stringResource(R.string.playlist_name_already_in_use)

    Row(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .dashedBorder(1.dp, Color.Gray, 8.dp)
            .clip(RoundedCornerShape(12.dp)), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray)
                .padding(10.dp)
        ) {
            SmallIcons(R.drawable.ic_playlist)
        }
        Column(Modifier.weight(1f)) {
            TransparentEditTextView(p, text, {
                if (it.length > 50) return@TransparentEditTextView
                text = it
            }, {
                keyboard?.hide()
            })
        }
        SmallIcons(R.drawable.ic_plus_sign_square, 25, 0) {
            if (text.length < 4) {
                validName.toast()
                return@SmallIcons
            }
            playerViewModel.addPlaylist(text)
            keyboard?.hide()
        }
        Spacer(Modifier.width(9.dp))
    }

    LaunchedEffect(playerViewModel.addingPlaylist) {
        when (val v = playerViewModel.addingPlaylist) {
            DataResponse.Empty -> {}
            is DataResponse.Error -> error.toast()
            DataResponse.Loading -> {}
            is DataResponse.Success -> if (v.item) {
                text = ""
                keyboard?.hide()
            } else
                nameAlreadyUsed.toast()
        }
    }
}