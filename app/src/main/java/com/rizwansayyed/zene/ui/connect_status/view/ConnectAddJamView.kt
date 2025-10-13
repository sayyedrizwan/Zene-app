package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ItemCardViewConnect
import com.rizwansayyed.zene.ui.view.MoviesImageCardConnect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.VideoCardViewConnect
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ConnectAddJam(viewModel: ConnectViewModel) {
    var showAlert by remember { mutableStateOf(false) }

    Spacer(Modifier.height(30.dp))
    SettingsViewSimpleItems(R.drawable.ic_music_note, R.string.add_a_jam) {
        showAlert = true
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AddJamDialog {
            it?.let { z -> viewModel.updateVibejamInfo(z) }
            showAlert = false
        }
    }
}

@Composable
fun AddJamDialog(clicked: (ZeneMusicData?) -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()

    var search by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item {
            TextField(
                search,
                { if (it.length <= 20) search = it },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                    if (search.length > 3) viewModel.searchZene(search)
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_s_p_a_etc), 14)
                },
                trailingIcon = {
                    if (search.length > 3) {
                        IconButton({
                            focusManager.clearFocus()
                            viewModel.searchZene(search)
                        }) {
                            ImageIcon(R.drawable.ic_search, 24)
                        }
                    }
                },
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
        }

        when (val v = viewModel.searchData) {
            ResponseResult.Empty -> {}
            is ResponseResult.Error -> {}
            ResponseResult.Loading -> item { CircularLoadingView() }
            is ResponseResult.Success -> {
                if (v.data.songs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.songs), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.songs) {
                            ItemCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.aiSongs?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.ai_music), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.aiSongs) {
                            ItemCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.videos?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.videos), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.videos) {
                            VideoCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.news?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.news), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.news) {
                            VideoCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.podcast?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.podcasts), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.podcast) {
                            ItemCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.movies?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.movies), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.movies) {
                            MoviesImageCardConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.playlists?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.playlists), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.playlists) {
                            ItemCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }

                if (v.data.albums?.isNotEmpty() == true) item {
                    Spacer(Modifier.height(30.dp))
                    Box(Modifier.padding(horizontal = 6.dp)) {
                        TextViewBold(stringResource(R.string.albums), 23)
                    }
                    Spacer(Modifier.height(12.dp))
                    LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data.albums) {
                            ItemCardViewConnect(it) {
                                clicked(it)
                            }
                        }
                    }
                }
            }
        }


        item { Spacer(Modifier.height(100.dp)) }
    }
}
