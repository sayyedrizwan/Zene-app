package com.rizwansayyed.zene.ui.view.myplaylist

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.BlackGray
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomImageOnMyPlaylist(data: ZeneMusicData, close: (Boolean) -> Unit) {
    Dialog(
        { close(false) }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val viewModel: MyLibraryViewModel = hiltViewModel()
        val focusManager = LocalFocusManager.current
        var showSearchBar by remember { mutableStateOf(false) }
        var searchText by remember { mutableStateOf("") }
        var playlistThumbnail by remember { mutableStateOf(data.thumbnail?.toUri()) }
        val state = rememberLazyListState()
        val coroutine = rememberCoroutineScope()

        val cropImage = rememberLauncherForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val croppedImageUri = result.uriContent
                croppedImageUri?.let { p -> playlistThumbnail = p }
            }
        }

        val pickMedia =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) cropImage.launch(
                    CropImageContractOptions(uri, CropImageOptions(fixAspectRatio = true))
                )
            }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MainColor), state,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(Modifier.height(25.dp))
                Row(Modifier.fillMaxWidth()) {
                    Box(
                        Modifier
                            .padding(start = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { close(false) }, Alignment.Center
                    ) {
                        ImageIcon(R.drawable.ic_cancel_close, 25)
                    }

                    Spacer(Modifier.weight(1f))

                    Box(
                        Modifier
                            .padding(end = 10.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (data.thumbnail == playlistThumbnail.toString())
                                    close(false)
                                else
                                    viewModel.updateMyPlaylistImage(data.id, playlistThumbnail)
                            }, Alignment.Center
                    ) {
                        ImageIcon(R.drawable.ic_tick, 25)
                    }
                }
            }

            item {
                Spacer(Modifier.height(25.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .fillMaxWidth(0.7f)
                        .aspectRatio(1f)
                        .clipToBounds()
                ) {
                    GlideImage(
                        playlistThumbnail, data.name, Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.TopCenter
                    )
                }
            }

            item {
                Spacer(Modifier.height(25.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                    Box(Modifier.clickable {
                        pickMedia.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) {
                        ImageIcon(R.drawable.ic_folder, 25)
                    }
                    Spacer(Modifier.width(15.dp))
                    Box(Modifier.clickable {
                        showSearchBar = !showSearchBar
                    }) {
                        ImageIcon(R.drawable.ic_search, 25)
                    }
                }
            }
            if (showSearchBar) {
                item {
                    Spacer(Modifier.height(15.dp))

                    TextField(
                        searchText,
                        {
                            if (it.length <= 30) {
                                if (searchText.length >= 3) coroutine.launch {
                                    viewModel.searchImage(it)
                                    state.animateScrollToItem(5)
                                }
                                searchText = it
                            }
                        },
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions {
                            focusManager.clearFocus()
                        },
                        placeholder = {
                            TextViewNormal(stringResource(R.string.search_images), 14)
                        },
                        trailingIcon = {
                            if (searchText.length > 3) {
                                IconButton({
                                    focusManager.clearFocus()
                                    viewModel.searchImage(searchText)
                                }) {
                                    ImageIcon(R.drawable.ic_search, 24)
                                }
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = BlackGray,
                            unfocusedContainerColor = BlackGray,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            cursorColor = Color.White
                        ),
                        singleLine = true
                    )

                    when (val v = viewModel.searchImages) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> CircularLoadingView()
                        is ResponseResult.Success -> LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .size(250.dp)
                                        .clipToBounds()
                                ) {
                                    GlideImage(
                                        it, searchText, Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                coroutine.launch {
                                                    focusManager.clearFocus()
                                                    playlistThumbnail = it.toUri()
                                                    state.animateScrollToItem(0)
                                                }
                                            },
                                        contentScale = ContentScale.Crop,
                                        alignment = Alignment.TopCenter
                                    )
                                }
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(350.dp)) }
                item { Spacer(Modifier.height(200.dp)) }
            }
        }
    }
}