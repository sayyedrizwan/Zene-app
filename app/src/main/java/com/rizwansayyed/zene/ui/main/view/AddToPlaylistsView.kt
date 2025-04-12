package com.rizwansayyed.zene.ui.main.view

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddToPlaylistsView(info: ZeneMusicData?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        var addNewPlaylists by remember { mutableStateOf(false) }
        val playerViewModel: PlayerViewModel = hiltViewModel(key = info?.id)

        val state = rememberLazyListState()
        var isBottomTriggered by remember { mutableStateOf(false) }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            stickyHeader {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 8.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        Box(Modifier
                            .padding(start = 5.dp)
                            .rotate(180f)
                            .align(Alignment.CenterStart)
                            .clickable {
                                close()
                            }) {
                            ImageIcon(R.drawable.ic_arrow_right, 25)
                        }
                        TextViewBold(stringResource(R.string.add_to_playlist), 19, center = true)
                    }

                    Spacer(Modifier.height(30.dp))
                }
            }

            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    ButtonWithBorder(R.string.create_playlist) { addNewPlaylists = true }
                    Spacer(Modifier.height(20.dp))
                }
            }
            item { Spacer(Modifier.height(20.dp)) }

            items(playerViewModel.checksPlaylistsSongLists) {
                AddPlaylistItems(it, info, playerViewModel)
            }

            if (playerViewModel.checksPlaylistsSongListsLoading) item {
                Spacer(Modifier.height(40.dp))
                CircularLoadingView()
                Spacer(Modifier.height(40.dp))
            }
        }

        LaunchedEffect(Unit) {
            info?.id?.let { playerViewModel.playlistSongCheckList(it) }
        }
        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo }.collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                    isBottomTriggered = true
                    info?.id?.let { playerViewModel.playlistSongCheckList(it) }
                } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                    isBottomTriggered = false
                }
            }
        }

        if (addNewPlaylists) CreateAPlaylistsView(playerViewModel, info) {
            addNewPlaylists = false
            if (it) {
                playerViewModel.clearPlaylistCheckList()
                info?.id?.let { playerViewModel.playlistSongCheckList(it) }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AddPlaylistItems(
    playlist: UserPlaylistResponse, info: ZeneMusicData?, viewModel: PlayerViewModel
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .clickable {
                val value =
                    if (viewModel.itemAddedToPlaylists.contains(playlist.id))
                        viewModel.itemAddedToPlaylists[playlist.id] ?: false else playlist.exists
                        ?: false

                viewModel.addMediaToPlaylist(playlist.id ?: "", !value, info)
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        GlideImage(
            playlist.img,
            playlist.name,
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
            TextViewBold(playlist.name ?: "", 15)
            TextViewLight(
                "${playlist.track_count} ${stringResource(R.string.items)}", 14
            )
        }

        RadioButton(
            selected = if (viewModel.itemAddedToPlaylists.contains(playlist.id)) viewModel.itemAddedToPlaylists[playlist.id]
                ?: false else playlist.exists ?: false,
            onClick = null,
            colors = RadioButtonColors(MainColor, Color.White, Color.White, Color.White)
        )
    }
}

@Composable
fun CreateAPlaylistsView(
    playerViewModel: PlayerViewModel, info: ZeneMusicData?, close: (Boolean) -> Unit
) {
    Dialog({ close(false) }, DialogProperties(usePlatformDefaultWidth = false)) {
        val context = LocalContext.current.applicationContext
        val focusManager = LocalFocusManager.current
        var search by remember { mutableStateOf("") }

        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MainColor, MainColor, MainColor.copy(0.5f), Color.Black, Color.Black
                        )
                    )
                ), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TextViewSemiBold(
                stringResource(R.string.give_your_playlist_a_name), 24, line = 1, center = true
            )
            Spacer(Modifier.height(10.dp))

            TextField(
                search,
                { search = if (it.length <= 100) it else it.take(100) },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions { focusManager.clearFocus() },
                placeholder = {
                    TextViewNormal(
                        stringResource(R.string.playlist_name), 17, Color.Gray, center = true
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 17.sp),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MainColor,
                    unfocusedContainerColor = MainColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )

            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                ButtonWithBorder(R.string.cancel) { close(false) }
                if (search.length > 3) {
                    Spacer(Modifier.width(10.dp))
                    when (val v = playerViewModel.createPlaylist) {
                        ResponseResult.Empty -> ButtonWithBorder(R.string.create) {
                            if (search.length <= 3) return@ButtonWithBorder
                            playerViewModel.createNewPlaylists(search, info)
                        }

                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> CircularLoadingViewSmall()
                        is ResponseResult.Success -> {
                            if (v.data.isExpire == true) {
                                LaunchedEffect(Unit) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        ProcessPhoenix.triggerRebirth(context)
                                    }
                                }
                            }

                            if (v.data.playlistID != null) {
                                LaunchedEffect(Unit) {
                                    close(true)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}